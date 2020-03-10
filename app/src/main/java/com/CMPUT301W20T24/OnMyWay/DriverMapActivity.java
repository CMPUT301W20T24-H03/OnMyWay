package com.CMPUT301W20T24.OnMyWay;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;


import java.util.ArrayList;
import java.util.List;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "OMW/DriverMapActivity";
    private GoogleMap mMap;

    /// Android Coding via YouTube, How to Show Current Location On Map in Android Studio
    /// https://www.youtube.com/watch?v=boyyLhXAZAQ&t=22s
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationListener locationListener;

    private static final int REQUEST_CODE = 101;
    View mapView;

    // HACK TO GO BACK TO THE MAIN ACTIVITY WHEN THE BACK BUTTON IS PRESSED. REMOVE LATER
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Switching to MainActivity");
        Intent intent = new Intent(DriverMapActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Toast.makeText(DriverMapActivity.this, "DriverMapActivity", Toast.LENGTH_LONG).show();
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapView = mapFragment.getView();
                    mapFragment.getMapAsync(DriverMapActivity.this);
                }
            }
        });
    }


    public void addMarkers(){
        ArrayList<LatLng> the_markers = new ArrayList<>();
        the_markers.add(new LatLng(53.523089, -113.623933));
        the_markers.add(new LatLng(53.565421, -113.563956));
        the_markers.add(new LatLng(53.537817, -113.476856));

        for(LatLng i : the_markers){
            mMap.addMarker(new MarkerOptions().position(i).title("Potential Rider"));
        }

    }

    /// YouTube video by CodingWithMitch: Calculating Directions with Google Directions API
    /// https://www.youtube.com/watch?v=f47L1SL5S0o&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=19
    private GeoApiContext my_geoApi;
    private void calculateDirections(Marker marker){

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(marker.getPosition().latitude,marker.getPosition().longitude);

        my_geoApi = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(my_geoApi);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

        directions.destination(String.valueOf(destination)).setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                System.out.println("HELLO");
                System.out.println(e.toString());
            }

        });
    }

    /// YouTube video by CodingWithMitch: Adding Polylines to a Google Map
    /// https://www.youtube.com/watch?v=xl0GwkLNpNI&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=20
    Polyline polyline;
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

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }

                    if(polyline!=null){
                        polyline.remove();
                    }
                    polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(getApplicationContext().getResources().getColor(R.color.colorPolyline)).clickable(true).width(10));

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng current_coordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(current_coordinates).title("Marker at current location (DRIVER)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_coordinates));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_coordinates,15));

        // Stack Overflow post by Adam https://stackoverflow.com/users/6789978/adam
        // Answer https://stackoverflow.com/questions/36785542/how-to-change-the-position-of-my-location-button-in-google-maps-using-android-st
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(30,30,30,120);

        addMarkers();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                calculateDirections(marker);

                BottomSheetDialog dialog = new BottomSheetDialog(DriverMapActivity.this);
                dialog.setContentView(R.layout.confirm_ride_driver);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.show();


                return false;
            }

        });

    }

    public void findRider(View view) {
        if (currentLocation != null) {
            Intent intent = new Intent(this, DriverViewRequests.class);
            double lat = currentLocation.getLatitude();
            double lon = currentLocation.getLongitude();
            intent.putExtra("DRIVER_LAT",lat);
            intent.putExtra("DRIVER_LON",lon);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Unable to find your current location", Toast.LENGTH_SHORT).show();
        }
    }

}
