package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


// An activity for testing
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OMW/MainActivity";   // Use this tag for Log.d()
    private TextView statusTextCurrentUser;
    private User currentUser;
    private FragmentManager fm;
    private ShowProfileFragment showProfileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Test Activity");

        fm = getSupportFragmentManager();

        statusTextCurrentUser = findViewById(R.id.statusTextCurrentUser);
        currentUser = State.getCurrentUser();

        if (currentUser != null) {
            statusTextCurrentUser.setText(currentUser.getUserID());
            statusTextCurrentUser.setTextColor(ContextCompat.getColor(this, R.color.colorSuccess));
        }
    }


    // Called when the user presses a button
    public void openSplashScreenActivity(View view) {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }


    // Called when the user presses a button
    public void openLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
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
        showProfileFragment = ShowProfileFragment.newInstance(currentUser);
        showProfileFragment.show(fm, "show_profile_fragment");
    }


    // Called when the user presses a button
    // TODO: Implement this. Need to rewrite User and DBManager first
    public void showRiderTestProfile(View view) {
//        FragmentManager fm = getSupportFragmentManager();
//        ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(user);
//        showProfileFragment.show(fm, "show_profile_fragment");
    }


    // Called when the user presses a button
    // TODO: Implement this. Need to rewrite User and DBManager first
    public void showDriverTestProfile(View view) {
//        FragmentManager fm = getSupportFragmentManager();
//        ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(user);
//        showProfileFragment.show(fm, "show_profile_fragment");
    }


    // Called when the user presses a button
    public void logout(View view) {
        String msg = "Logged out";
        new DBManager().logoutUser();
        Log.w(TAG, msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        statusTextCurrentUser.setText("None");
        statusTextCurrentUser.setTextColor(ContextCompat.getColor(this, R.color.colorError));
    }
}
