package com.CMPUT301W20T24.OnMyWay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;


import java.util.ArrayList;
import java.util.List;

public class DriverMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * Global declarations
     */
    private static final String TAG = "OMW/DriverMapActivity";
    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GeoApiContext geoApi;

    private Polyline polyline_rider;
    private Polyline polyline_destination;

    private FragmentManager fm;
    private ShowDriverRequestFragment showDriverRequestFragment;

    private LinearLayout browseRequestsLayout;
    private LinearLayout viewCurrentRequestLayout;
    private BottomSheetDialog bottomSheetDialog;

    private String driverUsername;

    private static final int REQUEST_CODE = 101;
    private View mapView;
    private DBManager dbManager;
    private MarkerStoreObject currentRide;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference requests = db.collection("riderRequests");

    private ArrayList<Marker> destinationMarkers = new ArrayList<>();
    private ArrayList<Marker> pickupMarkers = new ArrayList<>();


    private void removeDestinationMarkers(){
        for(Marker marker : destinationMarkers){
            marker.remove();
        }
        for(Marker marker : pickupMarkers){
            marker.remove();
        }
        destinationMarkers.clear();
        pickupMarkers.clear();
        mMap.clear();
    }



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
            Intent intent = new Intent(DriverMapActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    /**
     * onCreate method. Sets the view, and finds/stores the drivers current location
     * @param savedInstanceState    TODO: WRITE DESCRIPTION HERE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        dbManager = new DBManager();
        fm = getSupportFragmentManager();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Toast.makeText(DriverMapActivity.this, "DriverMapActivity", Toast.LENGTH_LONG).show();

        browseRequestsLayout = findViewById(R.id.browseRequestsLayout);;
        viewCurrentRequestLayout = findViewById(R.id.viewCurrentRequestLayout);

        Button viewPreviousRidesButton = findViewById(R.id.buttonViewPreviousRides);
        Button viewPreviousRidesButton2 = findViewById(R.id.buttonViewPreviousRides2);
        Button viewCurrentRequestButton = findViewById(R.id.buttonViewCurrentRequest);

        driverUsername = UserRequestState.getCurrentUser().getUserId();

        viewPreviousRidesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewPreviousRidesActivity();
            }
        });

        viewPreviousRidesButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewPreviousRidesActivity();
            }
        });

        // Listen for clicks on viewCurrentRequestButton
        viewCurrentRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Opening ShowDriverRequestFragment");

                // TODO: ALL THIS IS HARDCODED. REPLACE WITH THE ACTUAL REQUEST
                Request riderRequest = new Request(
                        currentRide.getRiderUsername(),
                        UserRequestState.getCurrentUser().getUserId(),
                        currentRide.getStartAddressName(),
                        currentRide.getStartLongitude(),
                        currentRide.getStartLatitude(),
                        currentRide.getEndAddressName(),
                        currentRide.getEndLongitude(),
                        currentRide.getEndLatitude(),
                        Float.toString(currentRide.getPaymentAmount()),
                        1585522651
                );

                // TODO: SAVE THIS TO STATE REAL QUICK SO THAT showDriverRequestFragment WILL
                // TODO: WORK CORRECTLY. THIS SHOULDN'T BE HERE IN THE FINAL APP VERSION
                UserRequestState.setCurrentRequest(riderRequest);

                showDriverRequestFragment = ShowDriverRequestFragment.newInstance(riderRequest);
                showDriverRequestFragment.show(fm);
            }
        });
    }


    // CALL THIS WHEN A RIDE IS CONFIRMED TO CHANGE THE LAYOUT
    private void showViewCurrentRequestLayout() {
        viewCurrentRequestLayout.setVisibility(View.VISIBLE);
        browseRequestsLayout.setVisibility(View.GONE);
        bottomSheetDialog.dismiss();    // Close confirm dialog
    }


    private void openViewPreviousRidesActivity() {
        Intent intent = new Intent(this, ViewPreviousRidesActivity.class);
        startActivity(intent);
    }


    /**
     * Finds the drivers current location, 'currentLocation'
     */
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapView = mapFragment.getView();
                    mapFragment.getMapAsync(DriverMapActivity.this);
                }
            }
        });
    }


    /// Youtube video by Coding In Flow: Firestore Tutorial Part 8 - ADD AND RETRIEVE MULTIPLE DOCUMENTS - Android Studio Tutorial
    /// https://www.youtube.com/watch?v=Bh0h_ZhX-Qg&t=349s
    // load requests from the database
    public void loadMarkers(){
        requests.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Log.d(TAG, "List is Empty");
                            return;
                        } else {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                try {
                                    BitmapDescriptor startLocationIcon = BitmapDescriptorFactory
                                            .fromResource(R.drawable.ic_blue_location_marker);
                                    LatLng latlng = new LatLng(Double.parseDouble(documentSnapshot.getString("startLatitude")), Double.parseDouble(documentSnapshot.getString("startLongitude")));
//                                    Toast.makeText(getApplicationContext(), documentSnapshot.getString("startLatitude")+","+documentSnapshot.getString("startLongitude"), Toast.LENGTH_LONG).show();
                                    Marker my_marker = mMap.addMarker(
                                            new MarkerOptions()
                                                    .position(latlng)
                                                    .title("Bid: $" + documentSnapshot.getString("paymentAmount"))
                                                    .icon(startLocationIcon)
                                                    .snippet("Username: " + documentSnapshot.getString("riderUserName"))
                                    );

                                    String documentId = documentSnapshot.getId();
                                    String requestId = documentSnapshot.getString("requestID");
                                    String riderUser = documentSnapshot.getString("riderUserName");
                                    String driverUser = documentSnapshot.getString("driverUserName");
                                    Double startLat = Double.parseDouble(documentSnapshot.getString("startLatitude"));
                                    Double startLon = Double.parseDouble(documentSnapshot.getString("startLongitude"));
                                    Double endLat = Double.parseDouble(documentSnapshot.getString("endLatitude"));
                                    Double endLon = Double.parseDouble(documentSnapshot.getString("endLongitude"));
                                    float paymentAmount = Float.parseFloat(documentSnapshot.getString("paymentAmount"));
                                    String status = documentSnapshot.getString("status");
                                    String startAddr = documentSnapshot.getString("startAddressName");
                                    String endAddr = documentSnapshot.getString("endAddressName");


                                    MarkerStoreObject markerStoreObject = new MarkerStoreObject(documentId, driverUser, endLat, endLon, paymentAmount,requestId,riderUser,startLat,startLon,status,startAddr, endAddr);
                                    my_marker.setTag((MarkerStoreObject) markerStoreObject);
                                    pickupMarkers.add(my_marker);
                                }
                                catch (NullPointerException e) {}
                            }
                        }
                    }
                });
    }


    /// YouTube video by CodingWithMitch: Calculating Directions with Google Directions API
    /// https://www.youtube.com/watch?v=f47L1SL5S0o&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=19
    /**
     * Calculates directions from the driver's current location to a marker (in this case, the rider's location)
     * Calls addPolylinesToMap(result) to draw the resulting directions as a polyline
     * @param marker A marker at the rider's current location
     */
    private void calculateDirections(Marker marker){
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(marker.getPosition().latitude,marker.getPosition().longitude);

        geoApi = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApi);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

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


    /**
     * Calculates directions from the marker (riders current position) to the final destination
     * The location of the final destination is stored as a LatLng object in the marker's tag
     * Calls addPolylinesToMapDestination(result) to draw the resulting directions as a polyline
     * @param marker A marker at the rider's current location
     */
    private void calculateDirectionsDestination(Marker marker){

        MarkerStoreObject markerStoreObject = (MarkerStoreObject) marker.getTag();
        LatLng destination_coordinates = new LatLng(markerStoreObject.getEndLatitude(), markerStoreObject.getEndLongitude());
        // setting current request for sliding menu view
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(destination_coordinates.latitude, destination_coordinates.longitude);

        geoApi = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApi);

        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude));

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
    /**
     * Adds a polyline on the Driver map showing the route to the rider
     */
    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for (DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng: decodedPath){
                        newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    if (polyline_rider!=null){
                        polyline_rider.remove();
                    }

                    polyline_rider = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(getApplicationContext().getResources().getColor(R.color.colorPolyline)).clickable(true).width(10));
                }
            }
        });
    }


    /**
     * Adds a polyline on the Driver's map showing the route from the rider to the final destination
     */
    private void addPolylinesToMapDestination(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for (DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng: decodedPath){
                        newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    if (polyline_destination!=null){
                        polyline_destination.remove();
                    }

                    polyline_destination = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath).color(getApplicationContext().getResources().getColor(R.color.colorPolylineDest)).clickable(true).width(10));
                }
            }
        });
    }


    /**
     * Invoked when the map is ready:
     * Enable current location, moves the camera to the driver's location
     * Shows dialogue when a maker is clicked
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng current_coordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(current_coordinates).title("Marker at current location (DRIVER)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_coordinates));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_coordinates,15));

        /// Stack Overflow post by Adam https://stackoverflow.com/users/6789978/adam
        /// Answer https://stackoverflow.com/questions/36785542/how-to-change-the-position-of-my-location-button-in-google-maps-using-android-st
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(30,30,30,120);

//        addMarkers();


        loadMarkers();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                calculateDirections(marker);
                calculateDirectionsDestination(marker);
                MarkerStoreObject markerStoreObject = (MarkerStoreObject) marker.getTag();

                for(Marker other_markers : pickupMarkers){
                    if(!other_markers.equals(marker)){
                        other_markers.remove();
                    }
                }
                BitmapDescriptor endLocationIcon = BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_end_location_marker);
                Marker my_marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(markerStoreObject.getEndLatitude(), markerStoreObject.getEndLongitude()))
                                .title("Destination")
                                .icon(endLocationIcon)
                );

                currentRide = markerStoreObject;
                destinationMarkers.add(my_marker);
                showDialogue();
                return true;
            }
        });


    }


    /**
     * The dialogue that is created when a marker is clicked
     */
    public void showDialogue(){
        bottomSheetDialog = new BottomSheetDialog(DriverMapActivity.this);
        bottomSheetDialog.setContentView(R.layout.dialog_confirm_ride_driver);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        bottomSheetDialog.show();

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                removeDestinationMarkers();
                loadMarkers();
            }
        });

        Button acceptButton = bottomSheetDialog.findViewById(R.id.confirm_ride_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * TODO: IMPLEMENT CONFIRM RIDE
                 **/

               dbManager.getDatabase().collection("riderRequests").document(currentRide.getDocumentId()).update(
                       "driverUserName", driverUsername,
                       "status", "ACTIVE").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Log.d(TAG, "Confirm button driver updated database correctly");
                       }
                       else{
                           Log.d(TAG, "Confirm button driver did not update");
                       }
                   }
               });


                showViewCurrentRequestLayout(); // CHANGE THE LAYOUT
            }
        });
        Button denyButton = bottomSheetDialog.findViewById(R.id.deny_ride_button);
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
    }


    public void findRider(View view) {
        if (currentLocation != null) {
            Intent intent = new Intent(this, DriverViewRequestsActivity.class);
            double lat = currentLocation.getLatitude();
            double lon = currentLocation.getLongitude();
            intent.putExtra("DRIVER_LAT",lat);
            intent.putExtra("DRIVER_LON",lon);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Unable to find your current location", Toast.LENGTH_SHORT).show();
        }
    }

}
