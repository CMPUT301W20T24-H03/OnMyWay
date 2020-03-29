package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.content.Context;
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
    private static final String TAG = "OMW/ShowRiderRequest...";  // Use this tag for calling Log.d()
    private CancelRideButtonListener cancelRideButtonListener;
    private FragmentManager fm;


    public ShowRiderRequestFragment() {
        // Empty constructor required here
        // Use newInstance() to create a new instance of the dialog
    }


    public static ShowRiderRequestFragment newInstance(Request request) {
        ShowRiderRequestFragment fragment = new ShowRiderRequestFragment();
        Bundle args = new Bundle();

        args.putString("driverId", request.getDriverUserName());
        args.putString("startLocationName", request.getStartLocationName());
        args.putString("endLocationName", request.getEndLocationName());

        fragment.setArguments(args);

        return fragment;
    }


    /// StackOverflow post by Marco RS
    /// Author: https://stackoverflow.com/users/683658/marco-rs
    /// Answer: https://stackoverflow.com/questions/12659747/call-an-activity-method-from-a-fragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            cancelRideButtonListener = (CancelRideButtonListener) context;
        } catch (ClassCastException castException) {
            // The activity does not implement the listener.
        }
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

        Button cancelRideButton = view.findViewById(R.id.buttonCancelRide);
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
        String startLocation = bundle.getString("startLocationName", "None");
        String endLocation = bundle.getString("endLocationName", "None");

        textStartLocation.setText(startLocation);
        textEndLocation.setText(endLocation);

        if (driverId == null) {
            textDriver.setText(R.string.text_waiting_for_driver);
            textDriver.setTextColor(getResources().getColor(R.color.colorError));
        }
        else {
            DBManager dbManager = new DBManager();

            // Use the listener we made to listen for when the function finishes
            dbManager.setUserInfoPulledListener(new UserInfoPulledListener() {
                @Override
                public void onUserInfoPulled(User fetchedUser) {
                    String driverName = fetchedUser.getFullName();

                    /// StackOverflow post by yugidroid
                    /// Author: https://stackoverflow.com/users/828728/yugidroid
                    /// Answer: https://stackoverflow.com/questions/11820526/format-textview-to-look-like-link
                    SpannableString string = new SpannableString(driverName);
                    string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
                    textDriver.setText(string);
                    textDriver.setClickable(true);

                    textDriver.setOnClickListener((View v) -> {
                        ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(fetchedUser);
                        showProfileFragment.show(fm);
                    });
                }
            });

            // Fetch the user info of a test driver user
            dbManager.fetchUserInfo(driverId);
        }
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

        if (viewId == R.id.buttonBack) {
            Log.d(TAG, "Back button pressed");

            this.dismiss(); // Close the dialog
        }
        else if (viewId == R.id.buttonCancelRide) {
            Log.d(TAG, "Cancel Ride button pressed");

            // TODO: Implement cancelling of rides here
            // Call the listener implemented in the parent activity
            if (cancelRideButtonListener == null) {
                Log.d(TAG, "No cancelRideButtonListener implemented");
            }
            else {
                cancelRideButtonListener.onCancelClick();
            }
        }
        else {
            throw new NullPointerException("No button with the correct ID was found");
        }
    }
}