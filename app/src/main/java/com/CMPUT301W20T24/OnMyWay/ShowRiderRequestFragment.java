package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A dialog fragment that shows the information for a given request to a rider
 * @author John
 */
/// CodePath, Using DialogFragment
/// https://guides.codepath.com/android/using-dialogfragment
public class ShowRiderRequestFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "OMW/ShowRiderRequest...";  // Use this tag for call Log.d()
    FragmentManager fm;
    private Button cancelRideButton;


    public ShowRiderRequestFragment() {
        // Empty constructor required here
        // Use newInstance() to create a new instance of the dialog
    }


    public static ShowRiderRequestFragment newInstance(String driverName, Request request) {
        DBManager dbManager = new DBManager();
        ShowRiderRequestFragment fragment = new ShowRiderRequestFragment();
        Bundle args = new Bundle();

        args.putString("driverId", request.getDriverUserName());
        args.putString("driverName", driverName);
        args.putString("startLocationName", request.getStartLocationName());
        args.putString("endLocationName", request.getEndLocationName());

        fragment.setArguments(args);

        return fragment;
    }


    /// StackOverflow post by jmaculate
    /// Author: https://stackoverflow.com/users/1908451/jmaculate
    /// Answer: https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_rider_request, container);

        Button backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(this);

        cancelRideButton = view.findViewById(R.id.buttonCancelRide);
        cancelRideButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get fields from view
        TextView textDriver = view.findViewById(R.id.textDriver);
        TextView textStartLocation = view.findViewById(R.id.textStartLocation);
        TextView textEndLocation = view.findViewById(R.id.textEndLocation);

        // Fetch arguments from bundle and set title
        Bundle bundle = getArguments();
        String driverId = bundle.getString("driverId", null);
        String driverName = bundle.getString("driverName", "None");
        String startLocation = bundle.getString("startLocationName", "None");
        String endLocation = bundle.getString("endLocationName", "None");

        textStartLocation.setText(startLocation);
        textEndLocation.setText(endLocation);

        /// https://stackoverflow.com/questions/11820526/format-textview-to-look-like-link

        /// StackOverflow post by yugidroid
        /// Author: https://stackoverflow.com/users/828728/yugidroid
        /// Answer: https://stackoverflow.com/questions/11820526/format-textview-to-look-like-link
        SpannableString string = new SpannableString(driverName);
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        textDriver.setText(string);
        textDriver.setClickable(true);

        textDriver.setOnClickListener((View v) -> {
            DBManager dbManager = new DBManager();

            // Use the listener we made to listen for when the function finishes
            dbManager.setUserInfoPulledListener(new UserInfoPulledListener() {
                @Override
                public void onUserInfoPulled(User fetchedUser) {
                    ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(fetchedUser);
                    showProfileFragment.show(fm);
                }
            });

            // Fetch the user info of a test driver user
            dbManager.fetchUserInfo(driverId);
        });
    }


    // An easier function call to use since we don't need the tag
    public void show(FragmentManager fragmentManager) {
        this.fm = fragmentManager;
        this.show(fragmentManager, "show_rider_request_fragment");
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "Button pressed");
        final int viewId = view.getId();
        Activity parentActivity = getActivity();

        if (viewId == R.id.buttonBack) {
            Log.d(TAG, "Back button pressed");

            this.dismiss(); // Close the dialog
        }
        else if (viewId == R.id.buttonCancelRide) {
            Log.d(TAG, "Cancel Ride button pressed");

            // TODO: Implement cancelling of rides here
        }
        else {
            throw new NullPointerException("No button with the correct ID was found");
        }
    }
}