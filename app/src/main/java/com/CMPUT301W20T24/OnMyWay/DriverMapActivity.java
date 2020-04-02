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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

    private String driverId;

    private static final int REQUEST_CODE = 101;
    private View mapView;
    private DBManager dbManager;
    private MarkerStoreObject currentRide;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference requests = db.collection("riderRequests");

    private ArrayList<Marker> destinationMarkers = new ArrayList<>();
    private ArrayList<Marker> pickupMarkers = new ArrayList<>();

    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;


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
     * @param savedInstanceState
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

        // Toast.makeText(DriverMapActivity.this, "DriverMapActivity", Toast.LENGTH_LONG).show();

        driverId = UserRequestState.getCurrentUser().getUserId();

    }

    /**
     * Finds the drivers current location, 'currentLocation'
     * @author Neel
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
                    // Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
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
    /**
     * Loads potential ride requests on the map
     * @author Payas, Bard, Neel
     */
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
                                    BitmapDescriptor startLocationIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_blue_location_marker);
                                    LatLng latlng = new LatLng(Double.parseDouble(documentSnapshot.getString("startLatitude")), Double.parseDouble(documentSnapshot.getString("startLongitude")));
//                                  Toast.makeText(getApplicationContext(), documentSnapshot.getString("startLatitude")+","+documentSnapshot.getString("startLongitude"), Toast.LENGTH_LONG).show();

                                    String documentId = documentSnapshot.getId();
                                    String requestId = documentSnapshot.getString("requestID");
                                    String riderUser = documentSnapshot.getString("riderId");
                                    String driverUser = documentSnapshot.getString("driverId");
                                    Double startLat = documentSnapshot.getDouble("startLatitude");
                                    Double startLon = documentSnapshot.getDouble("startLongitude");
                                    Double endLat = documentSnapshot.getDouble("endLatitude");
                                    Double endLon = documentSnapshot.getDouble("endLongitude");
                                    float paymentAmount = Float.parseFloat(documentSnapshot.getString("paymentAmount"));
                                    String status = documentSnapshot.getString("status");
                                    String startAddr = documentSnapshot.getString("startLocationName");
                                    String endAddr = documentSnapshot.getString("endLocationName");
                                    Long timeCreated = documentSnapshot.getLong("timeCreated");

                                    if(status.equals("INCOMPLETE")) {
                                        Marker my_marker = mMap.addMarker(
                                                new MarkerOptions()
                                                        .position(latlng)
                                                        .title("Bid: $" + documentSnapshot.getString("paymentAmount"))
                                                        .icon(startLocationIcon)
                                                        .snippet("Username: " + documentSnapshot.getString("riderUserName"))
                                        );

                                        MarkerStoreObject markerStoreObject = new MarkerStoreObject(documentId, driverUser, endLat, endLon, paymentAmount, requestId, riderUser, startLat, startLon, status, startAddr, endAddr);
                                        my_marker.setTag((MarkerStoreObject) markerStoreObject);
                                        pickupMarkers.add(my_marker);
                                    }


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
     * @author Neel
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
     * @author Neel
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
     * @param result The directions object containing the directional data
     * @author Neel
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
     * @param result The directions object containing the directional data
     * @author Neel
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
     * @author Neel
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
        View compassButton = mapView.findViewWithTag("GoogleMapCompass");
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(30,30,30,120);
        RelativeLayout.LayoutParams rlp2 = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
        rlp2.leftMargin = 185;

        loadMarkers();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                calculateDirections(marker);
                calculateDirectionsDestination(marker);
                showDialogue(marker);
                return true;
            }
        });
    }

    /**
     * Remove markers from the array and the map
     * @author Neel
     */
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


    /**
     * The dialogue that is created when a marker is clicked
     * @author Neel, Bard
     */
    public void showDialogue(Marker marker){

        // Show the appropriate markers on the map when a marker is clicked and the dialogue loads:
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
        //

        // Create the pop-up window
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup confirm_dialogue = (ViewGroup) layoutInflater.inflate(R.layout.dialog_confirm_ride_driver, null);
        popupWindow = new PopupWindow(confirm_dialogue);
        popupWindow.setWindowLayoutMode(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(1);
        popupWindow.setWidth(1);
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), Gravity.BOTTOM, 0 ,0);

        TextView pickupTextview = confirm_dialogue.findViewById(R.id.ride_pickup_text);
        String pickup = ((MarkerStoreObject) marker.getTag()).getStartAddressName();
        pickupTextview.setText(pickup);

        TextView destinationTextview = confirm_dialogue.findViewById(R.id.ride_destination_text);
        String destination = ((MarkerStoreObject) marker.getTag()).getEndAddressName();
        destinationTextview.setText(destination);

        TextView paymentTextview = confirm_dialogue.findViewById(R.id.ride_payment_text);
        String payment_amount = Float.toString(((MarkerStoreObject) marker.getTag()).getPaymentAmount());
        paymentTextview.setText(payment_amount);

        // "Deny" button on the pop-up window
        Button denyButton = confirm_dialogue.findViewById(R.id.deny_ride_button);
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                removeDestinationMarkers();
                loadMarkers();
            }
        });

        // Accept button on the pop-up window
        Button acceptButton = confirm_dialogue.findViewById(R.id.confirm_ride_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               db.collection("riderRequests").document(currentRide.getDocumentId()).update(
                       "driverId", driverId, "status", "ACTIVE").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Log.d(TAG, "Confirm button driver updated database correctly");
                           popupWindow.dismiss();
                           pickupRider(marker);
                       }
                       else{
                           Log.d(TAG, "Confirm button driver did not update");
                       }
                   }
               });

            }
        });

    }


    /**
     * The dialogue that is created when a driver is en-route to pick up a rider
     * @author Neel
     */
    public void pickupRider(Marker marker){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup pickup_dialogue = (ViewGroup) layoutInflater.inflate(R.layout.dialog_pickup_rider, null);
        popupWindow = new PopupWindow(pickup_dialogue);
        popupWindow.setWindowLayoutMode(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(1);
        popupWindow.setWidth(1);
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), Gravity.BOTTOM, 0 ,0);

        TextView pickupTextview = pickup_dialogue.findViewById(R.id.ride_pickup_text);
        String pickup = ((MarkerStoreObject) marker.getTag()).getStartAddressName();
        pickupTextview.setText(pickup);

        TextView destinationTextview = pickup_dialogue.findViewById(R.id.ride_destination_text);
        String destination = ((MarkerStoreObject) marker.getTag()).getEndAddressName();
        destinationTextview.setText(destination);

        TextView paymentTextview = pickup_dialogue.findViewById(R.id.ride_payment_text);
        String payment_amount = Float.toString(((MarkerStoreObject) marker.getTag()).getPaymentAmount());
        paymentTextview.setText(payment_amount);

        // "Deny" button on the pop-up window
        Button confirm_pickup_button = pickup_dialogue.findViewById(R.id.confirm_pickup_button);
        confirm_pickup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                dropoffRider(marker);
            }
        });

    }

    /**
     * The dialogue that is created when a driver is en-route to the rider's destination
     * @author Neel
     */
    public void dropoffRider(Marker marker){
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup dropoff_dialogue = (ViewGroup) layoutInflater.inflate(R.layout.dialog_destination_driver, null);
        popupWindow = new PopupWindow(dropoff_dialogue);
        popupWindow.setWindowLayoutMode(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(1);
        popupWindow.setWidth(1);
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), Gravity.BOTTOM, 0 ,0);

        TextView pickupTextview = dropoff_dialogue.findViewById(R.id.ride_pickup_text);
        String pickup = ((MarkerStoreObject) marker.getTag()).getStartAddressName();
        pickupTextview.setText(pickup);

        TextView destinationTextview = dropoff_dialogue.findViewById(R.id.ride_destination_text);
        String destination = ((MarkerStoreObject) marker.getTag()).getEndAddressName();
        destinationTextview.setText(destination);

        TextView paymentTextview = dropoff_dialogue.findViewById(R.id.ride_payment_text);
        String payment_amount = Float.toString(((MarkerStoreObject) marker.getTag()).getPaymentAmount());
        paymentTextview.setText(payment_amount);

        // "Deny" button on the pop-up window
        Button confirm_pickup_button = dropoff_dialogue.findViewById(R.id.confirm_dropoff_button);
        confirm_pickup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                db.collection("riderRequests").document(currentRide.getDocumentId()).update(
                        "driverId", driverId,
                        "status", "COMPLETE").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Confirm dropoff updated database correctly");
                            popupWindow.dismiss();
                        }
                        else{
                            Log.d(TAG, "Confirm dropoff did not update");
                        }
                    }
                });

                removeDestinationMarkers();
                loadMarkers();
                // TODO start payment activity here!

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
