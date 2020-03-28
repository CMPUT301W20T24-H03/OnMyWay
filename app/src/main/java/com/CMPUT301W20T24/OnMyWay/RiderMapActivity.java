package com.CMPUT301W20T24.OnMyWay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A map for the user to create a request and specify the start and end locations.
 * @author Manpreet Grewal and Payas Singh
 */
public class RiderMapActivity extends AppCompatActivity implements OnMapReadyCallback, CancelRideButtonListener {
    private static final String TAG = "OMW/RiderMapActivity";

    private GoogleMap mMap;
    private GeoApiContext geoApi;

    private LinearLayout searchLocationLayout;
    private LinearLayout editPriceLayout;
    private LinearLayout viewCurrentRequestLayout;

    private SearchView startSearchView;
    private SearchView endSearchView;

    private FragmentManager fm;
    ShowRiderRequestFragment showRiderRequestFragment;

    String startLocationName;
    String endLocationName;

    private Marker startLocationMarker;
    private Marker endLocationMarker;
    private Polyline polyline_rider;
    private Polyline polyline_destination;

    // Instantiating DBManager()
    private DBManager dbManager;

    private String newCost;

    Request riderRequest;


    // Disable back button for this activity
    @Override
    public void onBackPressed() {
        // Literally nothing
    }


    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(RiderMapActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_map);

        dbManager = new DBManager();
        fm = getSupportFragmentManager();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.riderMap);
        mapFragment.getMapAsync(this);

        startSearchView = findViewById(R.id.startLocationSearchBar);
        endSearchView = findViewById(R.id.endLocationSearchBar);

        searchLocationLayout = findViewById(R.id.searchLocationLayout);
        editPriceLayout = findViewById(R.id.editPriceLayout);
        viewCurrentRequestLayout = findViewById(R.id.viewCurrentRequestLayout);

        Button viewCurrentRequestButton = findViewById(R.id.viewCurrentRequestButton);
        Button confirmRequestButton = findViewById(R.id.confirmRequestButton);

        confirmRequestButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Generates a request and stores it in the 'riderRequests' collection of the project Firebase database.
             * If request does NOT include valid START and END location marker coordinate(s), user will be re-directed to MainActivity.
             * @param view
             * @return void
             * @author Payas Singh, Manpreet Grewal, Bard Samimi, John
             */
            @Override
            public void onClick(View view) {
                if (startLocationMarker == null || endLocationMarker == null) {
                    if (setStartPinPosition() && setEndPinPosition()) { }
                    else {
                        Log.d(TAG, "Request invalid. START or END location not specified/stored.");
                        Toast.makeText(getApplicationContext(), "Request Invalid. You must specify a start and end location!", Toast.LENGTH_SHORT).show();

                        return;
                    }
                }

                riderRequest = new Request(
                        UserRequestState.getCurrentUser().getUserId(),
                        "dYG5SQAAGVbmglT5k8dUhufAnpq1", // TODO: HARDCODED DRIVER ID FOR NOW
                        startLocationName,
                        startLocationMarker.getPosition().longitude,
                        startLocationMarker.getPosition().latitude,
                        endLocationName,
                        endLocationMarker.getPosition().longitude,
                        endLocationMarker.getPosition().latitude
                );

                // Calculate a price estimate for the ride depending on the start and end locations
                String priceEstimate = calculatePrice(
                        riderRequest.getStartLongitude(),
                        riderRequest.getStartLatitude(),
                        riderRequest.getEndLongitude(),
                        riderRequest.getEndLatitude()
                );

                // Once "request ride" button is clicked, create a dialogue which shows the
                // rider a price estimate and gives the rider the option to edit the price
                showEditPriceLayout();

                EditText editPrice = findViewById(R.id.editPrice);
                editPrice.setText(priceEstimate);

                // When "confirm" button is clicked, store the new price in firestore
                final Button confirmButton = findViewById(R.id.confirmPriceButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newCost = editPrice.getText().toString();

                        dbManager.pushRequestInfo(riderRequest, newCost);

                        Toast.makeText(getApplicationContext(), "Woo! Your ride is confirmed", Toast.LENGTH_SHORT).show();

                        showCurrentRequestLayout();
                    }
                });
            }
        });

        startSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setStartPinPosition();
                endSearchView.requestFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        endSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setEndPinPosition();
                confirmRequestButton.requestFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Listen for clicks on viewCurrentRequestButton
        viewCurrentRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Fetching driver info");

                dbManager.setUserInfoPulledListener(new UserInfoPulledListener() {
                    @Override
                    public void onUserInfoPulled(User driverUser) {
                        Log.d(TAG, "Opening ShowRiderRequestFragment");
                        showRiderRequestFragment = ShowRiderRequestFragment
                                .newInstance(driverUser.getFullName(), riderRequest);
                        showRiderRequestFragment.show(fm);
                    }
                });

                // Fetch the user info of the driver
                dbManager.fetchUserInfo(riderRequest.getDriverUserName());
            }
        });
    }


    public void cancelRide() {
        mMap.clear();
        startLocationMarker = null;
        endLocationMarker = null;

        // TODO: UPDATE STATE HERE
        // TODO: PUSH CHANGES TO FIREBASE

        Toast.makeText(getApplicationContext(), "Your request has been cancelled", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Request cancelled");
    }


    @Override
    public void onCancelClick(){
        Log.d(TAG, "Cancel event received from fragment");

        cancelRide();
        showSearchLocationLayout(); // Modify UI so user can start another ride
    }


    private void showEditPriceLayout() {
        editPriceLayout.setVisibility(View.VISIBLE);
        searchLocationLayout.setVisibility(View.GONE);
    }


    private void showCurrentRequestLayout() {
        viewCurrentRequestLayout.setVisibility(View.VISIBLE);
        editPriceLayout.setVisibility(View.GONE);
    }


    private void showSearchLocationLayout() {
        showRiderRequestFragment.dismiss(); // Dismiss dialog fragment
        searchLocationLayout.setVisibility(View.VISIBLE);
        viewCurrentRequestLayout.setVisibility(View.GONE);
    }


    // Get text from EditText and find the location on a map
    private boolean setStartPinPosition() {
        startLocationName = startSearchView.getQuery().toString();
        List<Address> startLocationList;

        Geocoder geocoder = new Geocoder(RiderMapActivity.this);
        try {
            startLocationList = geocoder.getFromLocationName(startLocationName, 1);
            if (startLocationList.size() != 0) {
                Address startAddress = startLocationList.get(0);
                LatLng latLng = new LatLng(startAddress.getLatitude(), startAddress.getLongitude());
                startLocationMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(startLocationName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_location_marker))
                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            } else if (startLocationMarker != null && startLocationList.size() != 0) {
                startLocationMarker.remove();
                Address startAddress = startLocationList.get(0);
                LatLng latLng = new LatLng(startAddress.getLatitude(), startAddress.getLongitude());
                startLocationMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(startLocationName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_location_marker))
                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            } else {
                Toast.makeText(getApplicationContext(), "Invalid start location.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (endLocationMarker != null && startLocationMarker != null) {
            calculateDirections();
            calculateDirectionsDestination();
        }

        return startLocationMarker != null;   // Return true if startLocationMarker exists now
    }


    // Get text from EditText and find the location on a map
    private boolean setEndPinPosition() {
        endLocationName = endSearchView.getQuery().toString();
        List<Address> endLocationList;

        Geocoder geocoder = new Geocoder(RiderMapActivity.this);
        try {
            endLocationList = geocoder.getFromLocationName(endLocationName, 1);
            if (endLocationMarker == null && endLocationList.size() != 0) {
                Address endAddress = endLocationList.get(0);
                LatLng latLng = new LatLng(endAddress.getLatitude(), endAddress.getLongitude());
                endLocationMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(endLocationName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_location_marker))
                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            } else if (endLocationMarker != null && endLocationList.size() != 0) {
                endLocationMarker.remove();
                Address endAddress = endLocationList.get(0);
                LatLng latLng = new LatLng(endAddress.getLatitude(), endAddress.getLongitude());
                endLocationMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(endLocationName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_location_marker))
                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            } else {
                Toast.makeText(getApplicationContext(), "Invalid end location.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (endLocationMarker != null && startLocationMarker != null) {
            calculateDirections();
            calculateDirectionsDestination();
        }

        return endLocationMarker != null;   // Return true if endLocationMarker exists now
    }


    // Default method introduced by Intelli-Sense.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    /// YouTube video by CodingWithMitch: Calculating Directions with Google Directions API
    /// https://www.youtube.com/watch?v=f47L1SL5S0o&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=19
    private void calculateDirections() {
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(endLocationMarker.getPosition().latitude, endLocationMarker.getPosition().longitude);

        geoApi = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApi);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(startLocationMarker.getPosition().latitude, startLocationMarker.getPosition().longitude));

        directions.destination(String.valueOf(destination)).setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, e.toString());
            }
        });
    }


    private void calculateDirectionsDestination() {
        // Setting current request for sliding menu view
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(endLocationMarker.getPosition().latitude, endLocationMarker.getPosition().longitude);

        geoApi = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApi);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(startLocationMarker.getPosition().latitude, startLocationMarker.getPosition().longitude));

        directions.destination(String.valueOf(destination)).setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMapDestination(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, e.toString());
            }
        });
    }


    /// YouTube video by CodingWithMitch: Adding Polylines to a Google Map
    /// https://www.youtube.com/watch?v=xl0GwkLNpNI&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=20
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

                    polyline_rider = mMap.addPolyline(
                            new PolylineOptions()
                                    .addAll(newDecodedPath)
                                    .color(getApplicationContext()
                                            .getResources()
                                            .getColor(R.color.colorPolyline)
                                    )
                                    .clickable(true)
                                    .width(10));

                }
            }
        });
    }


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
                    polyline_destination = mMap.addPolyline(
                            new PolylineOptions()
                                    .addAll(newDecodedPath)
                                    .color(getApplicationContext()
                                            .getResources()
                                            .getColor(R.color.colorPolylineDest)
                                    )
                                    .clickable(true)
                                    .width(10));
                }
            }
        });
    }


    // This method calculates a price estimate for the rider depending on the distance
    // between the start and end locations
    public String calculatePrice(Double startLong, Double startLat, Double endLong, Double endLat) {
        // define the start and end locations
        Location startLocation = new Location("startLocation");
        startLocation.setLatitude(startLat);
        startLocation.setLongitude(startLong);

        Location endLocation = new Location("endLocation");
        endLocation.setLatitude(endLat);
        endLocation.setLongitude(endLong);

        // calculate the price estimate
        double distanceInKms;
        distanceInKms = startLocation.distanceTo(endLocation) / 1000;

        if (distanceInKms <= 10) {
            distanceInKms = 7.99;
        }

        return String.format("%.2f", distanceInKms);
    }
}