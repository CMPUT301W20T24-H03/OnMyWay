package com.CMPUT301W20T24.OnMyWay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.CMPUT301W20T24.OnMyWay.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;


/**
 * An activity for quick testing of activities and fragments. WIll be removed before release
 * @author John
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OMW/MainActivity";   // Use this tag for Log.d()
    private static final int BARCODE_READER_REQUEST_CODE = 1 ;
    private TextView statusTextCurrentUser;
    private User currentUser;
    private DBManager dbManager;
    private FragmentManager fm;
    private ShowProfileFragment showProfileFragment;
    private ShowQRFragment showQRFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Test Activity");

        dbManager = new DBManager();
        fm = getSupportFragmentManager();

        statusTextCurrentUser = findViewById(R.id.statusTextCurrentUser);
        currentUser = UserRequestState.getCurrentUser();

        if (currentUser != null) {
            statusTextCurrentUser.setText(currentUser.getUserId());
            statusTextCurrentUser.setTextColor(ContextCompat.getColor(this, R.color.colorSuccess));
        }
    }


    // Called when the user presses a button
    public void openSplashScreenActivity(View view) {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }


    // Called when the user presses a button
    public void openMainDriverMapActivity(View view) {
        Intent intent = new Intent(this, DriverMapActivity.class);
        startActivity(intent);
    }


    // Called when the user presses a button
    public void openMainRiderMapActivity(View view) {
        Intent intent = new Intent(this, RiderMapActivity.class);
        startActivity(intent);
    }


    // Called when the user presses a button
    public void openEditProfileActivity(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }


    // Called when the user presses a button
    public void showCurrentUserProfile(View view) {
        showProfileFragment = ShowProfileFragment.newInstance(null);
        showProfileFragment.show(fm);
    }

    public void showQRbuck(View view){
        showQRFragment = ShowQRFragment.newInstance(null);
        showQRFragment.show(fm);

    }

    public void QRScan(View view){
        Intent intent = new Intent(MainActivity.this.getApplicationContext(), BarcodeCaptureActivity.class);
        MainActivity.this.startActivityForResult(intent, MainActivity.BARCODE_READER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("Barcode");
                    Point[] p = barcode.cornerPoints;
                    String info = barcode.displayValue;
                } else {
                    Log.d(TAG, "No QR code captured");
                }
            } else {
                Log.e(TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
                }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
            }

    }
}
