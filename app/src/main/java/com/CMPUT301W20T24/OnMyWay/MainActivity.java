package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/**
 * An activity for quick testing of activities and fragments. WIll be removed before release
 * @author John
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OMW/MainActivity";   // Use this tag for Log.d()
    private TextView statusTextCurrentUser;
    private User currentUser;
    private OnlineDBManager onlineDbManager;
    private FragmentManager fm;
    private ShowProfileFragment showProfileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Test Activity");

        onlineDbManager = new OnlineDBManager();
        fm = getSupportFragmentManager();

        statusTextCurrentUser = findViewById(R.id.statusTextCurrentUser);
        currentUser = State.getCurrentUser();

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
        showProfileFragment = ShowProfileFragment.newInstance(currentUser);
        showProfileFragment.show(fm);
    }


    // Called when the user presses a button
    // TODO: Implement this. Need to rewrite User and OnlineDBManager first
    public void showRiderTestProfile(View view) {
        // Use the listener we made to listen for when the function finishes
        onlineDbManager.setUserInfoPulledListener(new UserInfoPulledListener() {
            @Override
            public void onUserInfoPulled(User fetchedUser) {
                ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(fetchedUser);
                showProfileFragment.show(fm);
            }
        });

        // Fetch the user info of a test rider user
        onlineDbManager.fetchUserInfo("pcpzIGU4W7XomSe7o6AUXcFGDJy1");
    }


    // Called when the user presses a button
    // TODO: Implement this. Need to rewrite User and OnlineDBManager first
    public void showDriverTestProfile(View view) {
        // Use the listener we made to listen for when the function finishes
        onlineDbManager.setUserInfoPulledListener(new UserInfoPulledListener() {
            @Override
            public void onUserInfoPulled(User fetchedUser) {
                ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(fetchedUser);
                showProfileFragment.show(fm);
            }
        });

        // Fetch the user info of a test driver user
        onlineDbManager.fetchUserInfo("dYG5SQAAGVbmglT5k8dUhufAnpq1");
    }


    // Called when the user presses a button
    public void openRiderCostActivity(View view) {
        Intent intent = new Intent(this, RiderCostActivity.class);
        startActivity(intent);
    }
}
