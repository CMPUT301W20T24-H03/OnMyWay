package com.CMPUT301W20T24.OnMyWay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A map for the user to create a request and specify the start and end locations.
 * @author Manpreet Grewal and Payas Singh
 */
public class RiderMapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "OMW/RiderMapActivity";
    private DBManager dbManager;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private SearchView searchView;
    private Button switchModeButton;
    private Button confirmRequestButton;
    private RiderMode currentMode;

    double startLocLat;
    double startLocLon;

    double endLocLat;
    double endLocLon;

    private NavigationView navigationView;
    private FragmentManager fm;

    private String searchStartLocation;
    private String searchEndLocation;
    private Marker startLocationMarker;
    private Marker endLocationMarker;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_map);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        dbManager = new DBManager();
        fm = getSupportFragmentManager();

        currentMode = RiderMode.End;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.riderMap);
        searchView = findViewById(R.id.locationSearchBar);
        switchModeButton = findViewById(R.id.switchModeButton);
        confirmRequestButton = findViewById(R.id.confirmRequestButton);
        database = FirebaseFirestore.getInstance();

        confirmRequestButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Generates a request and stores it in the 'riderRequests' collection of the project Firebase database.
             * IF request does NOT include valid START and END location marker coordinate(s), user will be re-directed to MainActivity.
             * @param view
             * @return void
             * @author Payas Singh, Manpreet Grewal, Bard Samimi
             */
            @Override
            public void onClick(View view) {
                if (confirmRequestButton.getText().equals("REQUEST RIDE")) {
                    if (startLocationMarker == null || endLocationMarker == null) {
                        Log.d(TAG, "Request invalid. START or END location not specified/stored.");
                        Toast.makeText(getApplicationContext(), "Request Invalid. You must specify a start and end location!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Request riderRequest = new Request(startLocationMarker.getPosition().longitude, startLocationMarker.getPosition().latitude, endLocationMarker.getPosition().longitude,
                                endLocationMarker.getPosition().latitude);

                        HashMap<String, String> data = new HashMap<>();
                        data.put("endLatitude", String.valueOf(riderRequest.getEndLatitude()));
                        data.put("endLongitude", String.valueOf(riderRequest.getEndLongitude()));
                        data.put("requestID", riderRequest.getRequestId());
                        data.put("startLatitude", String.valueOf(riderRequest.getStartLatitude()));
                        data.put("startLongitude", String.valueOf(riderRequest.getStartLongitude()));

                        //Adds a new record the requet to the 'riderRequests' collection.
                        database.collection("riderRequests")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "Data addition successful" + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Data addition failed." + e.toString());
                                    }
                                });

                        Toast.makeText(getApplicationContext(), "Woo! Your ride is confirmed, check Active Request on the drawer pull menu by swiping right from the left side of the screen!", Toast.LENGTH_SHORT).show();
                        confirmRequestButton.setText("CANCEL RIDE");
                    }

                } else {
                    mMap.clear();
                    startLocationMarker = null;
                    endLocationMarker = null;
                    Toast.makeText(getApplicationContext(), "Your request has been cancelled", Toast.LENGTH_SHORT).show();
                    confirmRequestButton.setText("REQUEST RIDE");
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * Logic to search, store and display the START and END locations on the map fragment. Adds marker(s) to represent the locations that have been selected.
             * WILL LIKELY require this functionality to be split into separate function(s) and re-work some of the logic. But for now, it does execute.
             * @param query
             * @return boolean
             * @author Manpreet Grewal
             */
            //https://www.youtube.com/watch?v=mYHuFEaltkk
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Grab the user query and store it as a 'location' String and a list of Address(es) i.e. 'addressList'.
                String location = searchView.getQuery().toString();
                List<Address> addressList;

                //If the startLocationMarker is NOT null and the user is currently in the mode to search for a START location.
                //NOTE: Besides the following 'if', 'if else' and 'else' condition(s) that precede this line of code, the internal logic to generate markers is the same. Unless a marker must FIRST be removed.
                if (startLocationMarker != null && currentMode == RiderMode.Start) {
                    startLocationMarker.remove();
                    //Geocoder will take an address and return an array of viable addresse(s). These 'addresses' will be stored in the addressList, declared above.
                    Geocoder geocoder = new Geocoder(RiderMapActivity.this);
                    try {
                        //Return and grab ONLY ONE result from the results 'geocoder' retrieves.
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);

                        //Create an object of 'latLng' type, and store as an array of coordinates that can be provided to the marker and displayed.
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        startLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //If the startLocationMarker is null and the user is currently in the mode to search for an END location.
                else if (startLocationMarker == null && currentMode == RiderMode.Start) {
                    try {
                        Geocoder geocoder = new Geocoder(RiderMapActivity.this);
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        startLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                //If the endLocationMarker is NOT null and the user is currently in the mode to search for an END location.
                else if (endLocationMarker != null && currentMode == RiderMode.End) {
                    endLocationMarker.remove();
                    Geocoder geocoder = new Geocoder(RiderMapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        endLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //If the endLocationMarker is null and the user is currently in the mode to search for an END location.
                else if (endLocationMarker == null && currentMode == RiderMode.End) {
                    try {
                        Geocoder geocoder = new Geocoder(RiderMapActivity.this);
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        endLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //If an END and START location have been specified then draw a line between the two locations.
                if (endLocationMarker != null && startLocationMarker != null) {
                    calculateDirections();
                    calculateDirectionsDestination();
                }
                //Return a boolean 'false' as the default Intelli-Sense suggested creating a function that returned boolean.
                return false;
            }

            //Default method introduced by Intelli-Sense. Called everytime the 'query' changes.
            //https://blog.fossasia.org/tag/onquerytextchange/
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //Default method introduced by Intelli-Sense.
        mapFragment.getMapAsync(this);
    }

    /**
     * Upon pressing the 'MODE' button on the map, the user will be able to switch whether they are searching for the
     * @param view
     * @author Manpreet Grewal
     * @return void
     */
    public void switchModeActivate(View view) {
        if (currentMode == RiderMode.Start) {
            //Will store the current 'searchStartLocation' query, change the mode to 'RiderMode.End' and set the query for the new mode.
            searchStartLocation = searchView.getQuery().toString();
            currentMode = RiderMode.End;
            searchView.setQuery(searchEndLocation, false);

        } else {
            //Will store the current 'searchEndLocation' query, change the mode to 'RiderMode.Start' and set the query for the new mode.
            searchEndLocation = searchView.getQuery().toString();
            currentMode = RiderMode.Start;
            searchView.setQuery(searchStartLocation, false);
        }
    }
    //Default method introduced by Intelli-Sense.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /**
     * Enumerates the value(s) of Start and End to avoid utilizing a dictionary OR array.
     * @author Manpreet Grewal
     */
    private enum RiderMode {
        Start,
        End
    }

    /// YouTube video by CodingWithMitch: Calculating Directions with Google Directions API
    /// https://www.youtube.com/watch?v=f47L1SL5S0o&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=19
    private GeoApiContext my_geoApi;

    private void calculateDirections() {

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(endLocationMarker.getPosition().latitude, endLocationMarker.getPosition().longitude);

        my_geoApi = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(my_geoApi);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(startLocationMarker.getPosition().latitude, startLocationMarker.getPosition().longitude));

        directions.destination(String.valueOf(destination)).setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println(e.toString());
            }

        });
    }

    private void calculateDirectionsDestination() {

        // setting current request for sliding menu view
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(endLocationMarker.getPosition().latitude, endLocationMarker.getPosition().longitude);

        my_geoApi = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(my_geoApi);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(startLocationMarker.getPosition().latitude, startLocationMarker.getPosition().longitude));

        directions.destination(String.valueOf(destination)).setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMapDestination(result);
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println(e.toString());
            }

        });
    }

    /// YouTube video by CodingWithMitch: Adding Polylines to a Google Map
    /// https://www.youtube.com/watch?v=xl0GwkLNpNI&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=20
    Polyline polyline_rider;

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {
                        newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    if (polyline_rider != null) {
                        polyline_rider.remove();
                    }

                    polyline_rider = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(getApplicationContext().getResources().getColor(R.color.colorPolyline)).clickable(true).width(10));

                }
            }
        });
    }

    Polyline polyline_destination;

    private void addPolylinesToMapDestination(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {
                        newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    if (polyline_destination != null) {
                        polyline_destination.remove();
                    }

                    polyline_destination = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(getApplicationContext().getResources().getColor(R.color.colorPolylineDest)).clickable(true).width(10));

                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_rider:
		showRiderTestProfile(this.getCurrentFocus());
                break;
            case R.id.current_request_rider:
                if (startLocationMarker != null && endLocationMarker != null) {

                    Intent intent = new Intent(this, CurrentRequest.class);
                    intent.putExtra("REQUEST_LATITUDE", startLocLat);
                    intent.putExtra("REQUEST_LONGITUDE", startLocLon);
                    intent.putExtra("REQUEST_PAYMENTAMOUNT", 15.32f);
                    intent.putExtra("REQUEST_LATITUDE_ARRIVAL", endLocLat);
                    intent.putExtra("REQUEST_LONGITUDE_ARRIVAL", endLocLon);

                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No ride confirmed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.see_driver_rating:
                showDriverTestProfile(this.getCurrentFocus());
                break;

        }
        return false;
    }

 	public void showRiderTestProfile(View view) {
        // Use the listener we made to listen for when the function finishes
        dbManager.setUserInfoPulledListener(new UserInfoPulledListener() {
            @Override
            public void onUserInfoPulled(User fetchedUser) {
                ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(fetchedUser);
                showProfileFragment.show(fm);
            }
        });

        // Fetch the user info of a test rider user
        dbManager.fetchUserInfo("pcpzIGU4W7XomSe7o6AUXcFGDJy1");
    }

    public void showDriverTestProfile(View view) {
        // Use the listener we made to listen for when the function finishes
        dbManager.setUserInfoPulledListener(new UserInfoPulledListener() {
            @Override
            public void onUserInfoPulled(User fetchedUser) {
                ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(fetchedUser);
                showProfileFragment.show(fm);
            }
        });

        // Fetch the user info of a test driver user
        dbManager.fetchUserInfo("dYG5SQAAGVbmglT5k8dUhufAnpq1");
    }
}

