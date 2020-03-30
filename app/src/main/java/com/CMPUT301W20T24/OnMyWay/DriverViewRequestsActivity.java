package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class DriverViewRequestsActivity extends AppCompatActivity {
    private static final String TAG = "OMW/DriverViewReques...";   // Use this tag for calling Log.d()
    /**
     * view for requests within 22km for driver to see a list of people within distance , will be
     * adding their destination next week as we implement request class in database
     */


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

    // need to change later when finalize request class, just dummy one for now
    ArrayList<dummyRequest> requests = new ArrayList<dummyRequest>();

    float a = (float) 15.32;

    ArrayList<dummyRequest> requestsin = new ArrayList<dummyRequest>();
    dummyRequest request1 = new dummyRequest("Bob", 53.54,-113.49, a);
    dummyRequest request2 = new dummyRequest("jerry",53.46, -113.52, a);
    dummyRequest request3 = new dummyRequest("bill", 53.9, -113.8, a);
    dummyRequest request4 = new dummyRequest("ali", 53.523089, -113.623933, a);
    dummyRequest request5 = new dummyRequest("jane",53.565421, -113.563956, a);
    dummyRequest request6 = new dummyRequest("joan", 53.537817, -113.476856, a);
    dummyRequest request7 = new dummyRequest("alice",53.52328, -113.5264,a);
    dummyRequest request8 = new dummyRequest("martha",53.52328, -113.5264,a);

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


        requestsin.add(request1);
        requestsin.add(request2);
        requestsin.add(request3);
        requestsin.add(request4);
        requestsin.add(request5);
        requestsin.add(request6);
        requestsin.add(request7);
        requestsin.add(request8);

        // getting driver latitude and longitude from DriverMapActivity intent
        double driverLat = getIntent().getDoubleExtra("DRIVER_LAT",0);
        double driverLon = getIntent().getDoubleExtra("DRIVER_LON", 0);

        for(int i=0 ;i<requestsin.size();i++){

            /* 0.2 in lat long ~ 22km , checking to see if request within 22km of current location and if true add it to a
            new request array which will be used for the listview*/
            if (geoDist(driverLat, driverLon,requestsin.get(i).getLat(),requestsin.get(i).getLon())){
                requests.add(requestsin.get(i));
            }
        }
        arrayAdapter = new CustomListDriverRequest(this, requests);
        requestListView.setAdapter(arrayAdapter);

    }

    public boolean geoDist(double latDriver, double lonDriver, double latRider, double longRider){

        // 0.2 latitude/longitude approximately 22 km, finding rides within this distance
        double dist;
        dist = Math.sqrt((Math.pow((latDriver-latRider),2)) + (Math.pow((lonDriver-longRider),2)));
        return dist<0.2;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
