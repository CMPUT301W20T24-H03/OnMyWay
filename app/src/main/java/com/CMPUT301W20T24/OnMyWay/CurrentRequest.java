package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CurrentRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currentactiverequest);

        double lat = getIntent().getDoubleExtra("REQUEST_LATITUDE",0);
        double lon = getIntent().getDoubleExtra("REQUEST_LONGITUDE",0);
        float payment = getIntent().getFloatExtra("REQUEST_PAYMENTAMOUNT",0);
        TextView location = (TextView)findViewById(R.id.start_location);
        TextView paymentAmount = (TextView) findViewById(R.id.payment_amount);

        location.setText(Double.toString(lat) + " " + Double.toString(lon));
        paymentAmount.setText(Float.toString(payment));

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
