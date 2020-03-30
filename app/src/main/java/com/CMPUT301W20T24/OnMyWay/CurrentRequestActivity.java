package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class CurrentRequestActivity extends AppCompatActivity {
    private static final String TAG = "OMW/CurrentRequestAc...";  // Use this tag for calling Log.d()


    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(CurrentRequestActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_request);

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
