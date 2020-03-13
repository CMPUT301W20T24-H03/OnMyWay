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

    GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Button switchModeButton;
    Button confirmRequestButton;
    RiderMode currentMode;

    double startLocLat;
    double startLocLon;

    double endLocLat;
    double endLocLon;

    NavigationView navigationView;
    private FragmentManager fm;

    String searchStartLocation;
    String searchEndLocation;
    Marker startLocationMarker;
    Marker endLocationMarker;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_map);

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
             * @author Manpreet Grewal and Payas Singh
             */
            @Override
            public void onClick(View view) {
                if (startLocationMarker == null && endLocationMarker == null) {
                    Log.d(TAG, "Request invalid. START or END location not specified/stored.");
                    Intent intent = new Intent(RiderMapActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                else {
                    //Generate new 'riderRequest'.
                    Request riderRequest = new Request(startLocationMarker.getPosition().longitude, startLocationMarker.getPosition().latitude, endLocationMarker.getPosition().longitude,
                            endLocationMarker.getPosition().latitude);

                    //Add data to the project Firebase database.
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

                    //Remove marker(s) once 'riderRequest' has been added to the database.
                    startLocationMarker.remove();
                    endLocationMarker.remove();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList;

                if (startLocationMarker != null && currentMode == RiderMode.Start) {
                    startLocationMarker.remove();
                    Geocoder geocoder = new Geocoder(RiderMapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        startLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (startLocationMarker == null && currentMode == RiderMode.Start) {
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
                } else if (endLocationMarker != null && currentMode == RiderMode.End) {
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
                } else if (endLocationMarker == null && currentMode == RiderMode.End) {
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
                // if an end and a start location has been specified then
                // draw a lone between the two locations
                if (endLocationMarker != null && startLocationMarker!=null) {
                    calculateDirections();
                    calculateDirectionsDestination();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    public void switchModeActivate(View view) {
        if (currentMode == RiderMode.Start) {
            searchStartLocation = searchView.getQuery().toString();
            currentMode = RiderMode.End;
            searchView.setQuery(searchEndLocation, false);

        } else {
            searchEndLocation = searchView.getQuery().toString();
            currentMode = RiderMode.Start;
            searchView.setQuery(searchStartLocation, false);
        }
    }
    /// YouTube video by CodingWithMitch: Calculating Directions with Google Directions API
    /// https://www.youtube.com/watch?v=f47L1SL5S0o&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=19
    private GeoApiContext my_geoApi;
    private void calculateDirections(){

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(endLocationMarker.getPosition().latitude,endLocationMarker.getPosition().longitude);

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

    private void calculateDirectionsDestination(){

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
    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){
                        newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    if(polyline_rider!=null){
                        polyline_rider.remove();
                    }

                    polyline_rider = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(getApplicationContext().getResources().getColor(R.color.colorPolyline)).clickable(true).width(10));

                }
            }
        });
    }

    Polyline polyline_destination;
    private void addPolylinesToMapDestination(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){
                        newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    if(polyline_destination!=null){
                        polyline_destination.remove();
                    }

                    polyline_destination = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(getApplicationContext().getResources().getColor(R.color.colorPolylineDest)).clickable(true).width(10));

                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_rider:
                Toast.makeText(getApplicationContext(), "profile working", Toast.LENGTH_SHORT).show();
                break;
            case R.id.current_request_rider:
               if(startLocationMarker != null && endLocationMarker != null){

                   Intent intent = new Intent(this, CurrentRequest.class);
                   intent.putExtra("REQUEST_LATITUDE", startLocLat);
                   intent.putExtra("REQUEST_LONGITUDE",startLocLon);
                   intent.putExtra("REQUEST_PAYMENTAMOUNT", 15.32f);
                   intent.putExtra("REQUEST_LATITUDE_ARRIVAL",endLocLat);
                   intent.putExtra("REQUEST_LONGITUDE_ARRIVAL",endLocLon);

                   startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "No ride confirmed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.see_driver_rating:
                showDriverTestProfile(this.getCurrentFocus());
                break;

            }
        return false;
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

    public void confirmRideActivate(View view) {
        if(confirmRequestButton.getText().equals("REQUEST RIDE")) {
            if (startLocationMarker != null && endLocationMarker != null) {
                Toast.makeText(getApplicationContext(),"Woo! Your ride is confirmed, check Active Request on the drawer pull menu by swiping right from the left side of the screen!",Toast.LENGTH_SHORT).show();
                startLocLat = startLocationMarker.getPosition().latitude;
                startLocLon = startLocationMarker.getPosition().longitude;
                endLocLat = endLocationMarker.getPosition().latitude;
                endLocLon = endLocationMarker.getPosition().longitude;
                confirmRequestButton.setText("CANCEL RIDE");

            }

            else {
                Toast.makeText(getApplicationContext(),"Please make sure there is both a start and end location marker, use mode to toggle between the 2",Toast.LENGTH_LONG).show();
            }
        }

        else{
            mMap.clear();
            startLocationMarker = null;
            endLocationMarker = null;
            Toast.makeText(getApplicationContext(),"Your request has been cancelled",Toast.LENGTH_SHORT).show();
            confirmRequestButton.setText("REQUEST RIDE");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    private enum RiderMode {
        Start,
        End
    }
}
