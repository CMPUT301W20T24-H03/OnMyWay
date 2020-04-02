package com.CMPUT301W20T24.OnMyWay;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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


/**
 * A dialog fragment that shows the information for a given request to a driver
 * @author John
 */
/// CodePath, Using DialogFragment
/// https://guides.codepath.com/android/using-dialogfragment
public class ShowDriverRequestFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "OMW/ShowDriverReques...";  // Use this tag for calling Log.d()
    private CancelRideButtonListener cancelRideButtonListener;
    private FragmentManager fm;
    private TextView textElapsedTime;


    /// StackOverflow post by Dave.B
    /// Author: https://stackoverflow.com/users/303256/dave-b
    /// Answer: https://stackoverflow.com/questions/4597690/how-to-set-timer-in-android
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            textElapsedTime.setText(UserRequestState.getCurrentRequest().getElapsedTime());

            timerHandler.postDelayed(this, 1000);
        }
    };


    public ShowDriverRequestFragment() {
        // Empty constructor required here
        // Use newInstance() to create a new instance of the dialog
    }


    public static ShowDriverRequestFragment newInstance(Request request) {
        ShowDriverRequestFragment fragment = new ShowDriverRequestFragment();
        Bundle args = new Bundle();

        args.putString("riderId", request.getRiderId());
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
        View view = inflater.inflate(R.layout.fragment_show_driver_request, container);

        Button backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get fields from view
        textElapsedTime = view.findViewById(R.id.textElapsedTime);
        TextView textRider = view.findViewById(R.id.textRider);
        TextView textStartLocation = view.findViewById(R.id.textStartLocation);
        TextView textEndLocation = view.findViewById(R.id.textEndLocation);

        // Fetch arguments from bundle and set title
        Bundle bundle = getArguments();
        String riderId = bundle.getString("riderId", null);
        String startLocation = bundle.getString("startLocationName", "None");
        String endLocation = bundle.getString("endLocationName", "None");

        textStartLocation.setText(startLocation);
        textEndLocation.setText(endLocation);

        if (riderId == null) {
            textRider.setText("Oof. There should be a rider here");
            textRider.setTextColor(getResources().getColor(R.color.colorError));
        }
        else {
            DBManager dbManager = new DBManager();

            timerHandler.postDelayed(timerRunnable, 0); // Start the timer runnable

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
                    textRider.setText(string);
                    textRider.setClickable(true);

                    textRider.setOnClickListener((View v) -> {
                        ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(fetchedUser);
                        showProfileFragment.show(fm);
                    });
                }
            });

            // Fetch the user info of a test driver user
            dbManager.fetchUserInfo(riderId);
        }
    }


    // An easier function call to use since we don't need the tag
    public void show(FragmentManager fragmentManager) {
        this.fm = fragmentManager;
        this.show(fragmentManager, "show_driver_request_fragment");
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "Button pressed");

        final int viewId = view.getId();

        if (viewId == R.id.buttonBack) {
            Log.d(TAG, "Back button pressed");

            this.dismiss(); // Close the dialog
        }
        else {
            throw new NullPointerException("No button with the correct ID was found");
        }
    }
}