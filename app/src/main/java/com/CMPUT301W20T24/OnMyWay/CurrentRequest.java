package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class CurrentRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currentactiverequest);

        Bundle extras = getIntent().getExtras();

        if (extras!=null) {

            double lat = extras.getDouble("REQUEST_LATITUDE", 0);
            double lon = extras.getDouble("REQUEST_LONGITUDE", 0);
            float payment = extras.getFloat("REQUEST_PAYMENTAMOUNT", 0);
            double endLat;
            double endLon;

            TextView location = (TextView) findViewById(R.id.start_location);
            TextView paymentAmount = (TextView) findViewById(R.id.payment_amount);
            TextView endLocation = (TextView) findViewById(R.id.end_location);

            location.setText(Double.toString(lat) + " " + Double.toString(lon));
            paymentAmount.setText(Float.toString(payment));

            if(extras.containsKey("REQUEST_LATITUDE_ARRIVAL") && extras.containsKey("REQUEST_LONGITUDE_ARRIVAL")){
                endLat = extras.getDouble("REQUEST_LATITUDE_ARRIVAL");
                endLon = extras.getDouble("REQUEST_LONGITUDE_ARRIVAL");
                endLocation.setText(Double.toString(endLat) + " " + Double.toString(endLon));
            }



        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
