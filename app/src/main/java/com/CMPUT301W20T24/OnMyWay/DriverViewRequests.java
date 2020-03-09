package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class DriverViewRequests extends AppCompatActivity {

    ListView requestListView;

    // need to change later when finalize request class, just dummy one for now
    ArrayList<String> requests = new ArrayList<String>();

    ArrayList<dummyRequest> requestsin = new ArrayList<dummyRequest>();
    dummyRequest request1 = new dummyRequest("Bob", 53.54,-113.49);
    dummyRequest request2 = new dummyRequest("jerry",53.46, -113.52);
    dummyRequest request3 = new dummyRequest("bill", 53.9, -113.8);



    ArrayAdapter arrayAdapter;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_view);

        requestListView = (ListView) findViewById(R.id.requestList);


        requestsin.add(request1);
        requestsin.add(request2);
        requestsin.add(request3);

        double driverLat = getIntent().getDoubleExtra("DRIVER_LAT",0);
        double driverLon = getIntent().getDoubleExtra("DRIVER_LON", 0);

        for(int i=0 ;i<requestsin.size();i++){
            double dist;
            dist = geoDist(driverLat, driverLon,requestsin.get(i).getLat(),requestsin.get(i).getLon());

            // 0.15 in lat long ~ = 11 km, searching within this range for requests
            if (dist < 0.15){
                requests.add(requestsin.get(i).getUsername());
            }
        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,requests);

        requestListView.setAdapter(arrayAdapter);

    }

    public double geoDist(double latDriver, double lonDriver, double latRider, double longRider){

        // 0.1 latitude/longitude approximately 11 km, finding rides within this distance
        double dist;

        dist = Math.sqrt((Math.pow((latDriver-latRider),2)) + (Math.pow((lonDriver-longRider),2)));

        return dist;

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
