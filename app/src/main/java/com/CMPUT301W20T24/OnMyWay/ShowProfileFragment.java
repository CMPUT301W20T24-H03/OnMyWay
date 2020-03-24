package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


/**
 * A dialog fragment that shows a given user's profile information
 * @author John
 */
/// CodePath, Using DialogFragment
/// https://guides.codepath.com/android/using-dialogfragment
public class ShowProfileFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "OMW/ShowProfileFragment";  // Use this tag for call Log.d()
    private static boolean isCurrentUser = false;
    private Button editButton;
    private Button logoutButton;


    public ShowProfileFragment() {
        // Empty constructor required here
        // Use newInstance() to create a new instance of the dialog
    }


    public static ShowProfileFragment newInstance(User user) {
        if (user == null) {
            throw new NullPointerException("User can't be null");
        }
        else if (user == State.getCurrentUser()) {
            Log.d(TAG, "Creating profile dialog for current user");
            isCurrentUser = true;
        }
        else {
            Log.d(TAG, "Creating profile dialog for some user");
            isCurrentUser = false;
        }

        ShowProfileFragment fragment = new ShowProfileFragment();
        Bundle args = new Bundle();

        args.putString("profilePhotoUrl", user.getProfilePhotoUrl());
        args.putString("userId", user.getUserId());
        args.putString("userType", (user.isDriver()) ? "Driver" : "Rider");
        args.putString("rating", user.getRating());
        args.putString("fullName", user.getFullName());
        args.putString("email", user.getEmail());
        args.putString("phone", user.getPhoneNumber());

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
        View view = inflater.inflate(R.layout.fragment_show_profile, container);

        Button backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(this);

        editButton = view.findViewById(R.id.buttonEditProfile);
        editButton.setOnClickListener(this);

        logoutButton = view.findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(this);

        if (isCurrentUser) {
            editButton.setVisibility(view.VISIBLE);
            logoutButton.setVisibility(view.VISIBLE);
        }
        else {
            editButton.setVisibility(view.INVISIBLE);
            logoutButton.setVisibility(view.GONE);
        }

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        TextView labelUserId = view.findViewById(R.id.labelUserId);
        TextView labelUserType = view.findViewById(R.id.labelUserType);
        TextView labelUserRating = view.findViewById(R.id.labelRating);
        TextView fullNameText = view.findViewById(R.id.textFullName);
        TextView emailText = view.findViewById(R.id.textEmail);
        TextView phoneText = view.findViewById(R.id.textPhone);
        CircleImageView profilePhotoImage = view.findViewById(R.id.imageProfilePhoto);

        // Fetch arguments from bundle and set title
        Bundle bundle = getArguments();
        String profilePhotoUrl = bundle.getString("profilePhotoUrl", "");
        String userId = bundle.getString("userId", "-");
        String userType = bundle.getString("userType", "-");
        String rating = bundle.getString("rating", "-");
        String fullName = bundle.getString("fullName", "-");
        String email = bundle.getString("email", "-");
        String phone = bundle.getString("phone", "-");

        labelUserId.setText(userId);
        labelUserType.setText(userType);
        labelUserRating.setText(rating);
        fullNameText.setText(fullName);
        emailText.setText(email);
        phoneText.setText(phone);

        // Download the profile photo for the user and display it
        Picasso.get().load(profilePhotoUrl).noFade().into(profilePhotoImage);
    }


    // An easier function call to use since we don't need the tag
    public void show(FragmentManager fragmentManager) {
        this.show(fragmentManager, "show_profile_fragment");
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
        else if (viewId == R.id.buttonEditProfile) {
            Log.d(TAG, "Edit profile button pressed");

            Intent intent = new Intent(parentActivity, EditProfileActivity.class);
            startActivity(intent);
        }
        else if (viewId == R.id.buttonLogout) {
            Log.d(TAG, "Logout button pressed");

            State.logoutUser();
            Intent intent = new Intent(parentActivity, SplashScreenActivity.class);
            intent.putExtra("toastMessage", "Logged out successfully");
            startActivity(intent);
        }
        else {
            throw new NullPointerException("No button with the correct ID was found");
        }
    }
}