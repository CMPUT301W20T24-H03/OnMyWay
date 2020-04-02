package com.CMPUT301W20T24.OnMyWay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

/**
 * view for requests within 22km for driver to see a list of people within distance , will be
 * adding their destination next week as we implement request class in database
 */
public class DriverViewRequestsActivity extends AppCompatActivity {
    private static final String TAG = "OMW/DriverViewReques...";   // Use this tag for calling Log.d()
    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(DriverViewRequestsActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    ListView requestListView;
    ArrayList<Request> requests = new ArrayList<Request>();
    ArrayList<Request> requestsin = new ArrayList<Request>();

    // array adapter for requests
    ArrayAdapter arrayAdapter;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_view);
        requestListView = (ListView) findViewById(R.id.requestList);

        // getting driver latitude and longitude from DriverMapActivity intent
        double driverLat = getIntent().getDoubleExtra("DRIVER_LAT", 0);
        double driverLon = getIntent().getDoubleExtra("DRIVER_LON", 0);

        for (int i = 0; i < requestsin.size(); i++) {
            /* 0.2 in lat long ~ 22km , checking to see if request within 22km of current location and if true add it to a
            new request array which will be used for the listview*/
            if (geoDist(driverLat, driverLon, requestsin.get(i).getStartLatitude(), requestsin.get(i).getEndLatitude())) {
                requests.add(requestsin.get(i));
                requests.add(requestsin.get(i));
            }
        }

        arrayAdapter = new CustomListDriverRequest(this, requests);
        requestListView.setAdapter(arrayAdapter);
        loadRequests();
    }
        public void loadRequests(){
            CollectionReference collectionReference = mDatabase.collection("riderRequests");
            mDatabase.collection("riderRequests")
                    .whereEqualTo("driverUserName", "NONE")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                // FIXME: THIS IS NOT A VALID CONSTRUCTOR ANYMORE. REQUEST NEEDS TO HAVE MANY MORE VALUES
                                Request request = new Request(
                                        doc.getString("riderId"),
                                        doc.getString("driverId"),
                                        doc.getString("startLocationName"),
                                        Utilities.checkDoubleNotNull(doc.getDouble("startLongitude")),
                                        Utilities.checkDoubleNotNull(doc.getDouble("startLatitude")),
                                        doc.getString("endLocationName"),
                                        Utilities.checkDoubleNotNull(doc.getDouble("endLongitude")),
                                        Utilities.checkDoubleNotNull(doc.getDouble("endLatitude")),
                                        "$5.00",
                                        doc.getString("status"),
                                        doc.getLong("timeCreated"),
                                        doc.getLong("timeAccepted")
                                );
                                //request = i.toObject(Request.class);
                                arrayAdapter.add(request);
                            }
                        }
                    });
        }

        public boolean geoDist (double latDriver, double lonDriver, double latRider,
        double longRider){

            // 0.2 latitude/longitude approximately 22 km, finding rides within this distance
            double dist;
            dist = Math.sqrt((Math.pow((latDriver - latRider), 2)) + (Math.pow((lonDriver - longRider), 2)));
            return dist < 0.2;
        }
    }

