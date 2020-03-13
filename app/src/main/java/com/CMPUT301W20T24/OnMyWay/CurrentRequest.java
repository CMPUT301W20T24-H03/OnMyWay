package com.CMPUT301W20T24.OnMyWay;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CurrentRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_active_request);

        double lat = getIntent().getDoubleExtra("REQUEST_LATITUDE", 0);
        double lon = getIntent().getDoubleExtra("REQUEST_LONGITUDE", 0);
        float payment = getIntent().getFloatExtra("REQUEST_PAYMENTAMOUNT", 0);
        TextView location = findViewById(R.id.start_location);
        TextView paymentAmount = findViewById(R.id.payment_amount);

        location.setText(lat + " " + lon);
        paymentAmount.setText(Float.toString(payment));

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
