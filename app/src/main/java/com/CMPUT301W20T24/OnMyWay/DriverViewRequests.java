package com.CMPUT301W20T24.OnMyWay;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


public class DriverViewRequests extends AppCompatActivity {

    ListView requestListView;

    // need to change later when finalize request class, just dummy one for now
    ArrayList<dummyRequest> requests = new ArrayList<dummyRequest>();

    float a = (float) 15.32;

    ArrayList<dummyRequest> requestsin = new ArrayList<dummyRequest>();
    dummyRequest request1 = new dummyRequest("Bob", 53.54, -113.49, a);
    dummyRequest request2 = new dummyRequest("jerry", 53.46, -113.52, a);
    dummyRequest request3 = new dummyRequest("bill", 53.9, -113.8, a);
    dummyRequest request4 = new dummyRequest("ali", 53.523089, -113.623933, a);
    dummyRequest request5 = new dummyRequest("jane", 53.565421, -113.563956, a);
    dummyRequest request6 = new dummyRequest("joan", 53.537817, -113.476856, a);
    dummyRequest request7 = new dummyRequest("alice", 53.52328, -113.5264, a);
    dummyRequest request8 = new dummyRequest("martha", 53.52328, -113.5264, a);


    ArrayAdapter arrayAdapter;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_view);

        requestListView = findViewById(R.id.requestList);


        requestsin.add(request1);
        requestsin.add(request2);
        requestsin.add(request3);
        requestsin.add(request4);
        requestsin.add(request5);
        requestsin.add(request6);
        requestsin.add(request7);
        requestsin.add(request8);

        double driverLat = getIntent().getDoubleExtra("DRIVER_LAT", 0);
        double driverLon = getIntent().getDoubleExtra("DRIVER_LON", 0);

        for (int i = 0; i < requestsin.size(); i++) {

            // 0.15 in lat long ~ = 11 km, searching within this range for requests
            if (geoDist(driverLat, driverLon, requestsin.get(i).getLat(), requestsin.get(i).getLon())) {
                requests.add(requestsin.get(i));
            }
        }


        arrayAdapter = new CustomListDriverRequest(this, requests);

        requestListView.setAdapter(arrayAdapter);

    }


    public boolean geoDist(double latDriver, double lonDriver, double latRider, double longRider) {

        // 0.2 latitude/longitude approximately 22 km, finding rides within this distance
        double dist;

        dist = Math.sqrt((Math.pow((latDriver - latRider), 2)) + (Math.pow((lonDriver - longRider), 2)));

        return dist < 0.2;

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
