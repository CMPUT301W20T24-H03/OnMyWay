package com.CMPUT301W20T24.OnMyWay;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class RiderMapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Button switchModeButton;
    Button confirmRequestButton;
    RiderMode currentMode;

    String searchStartLocation;
    String searchEndLocation;

    Marker startLocationMarker;
    Marker endLocationMarker;

    private static final String TAG = "OMW/Request";
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
//        final CollectionReference collectionReference = database.collection("requests");

        confirmRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request riderRequest = new Request(startLocationMarker.getPosition().longitude, startLocationMarker.getPosition().latitude, endLocationMarker.getPosition().longitude,
                        endLocationMarker.getPosition().latitude);

                HashMap<String, String> data = new HashMap<>();
                data.put("endLatitude", String.valueOf(riderRequest.getEndLatitude()));
                data.put("endLongitude", String.valueOf(riderRequest.getEndLongitude()));
                data.put("requestID", riderRequest.getRequestId());
                data.put("startLatitude",String.valueOf(riderRequest.getStartLatitude()));
                data.put("startLongitude", String.valueOf(riderRequest.getStartLongitude()));

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
                startLocationMarker.remove();
                endLocationMarker.remove();
            }
        });

//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private enum RiderMode {
        Start,
        End
    }
}
