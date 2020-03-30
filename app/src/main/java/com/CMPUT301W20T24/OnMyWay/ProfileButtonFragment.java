package com.CMPUT301W20T24.OnMyWay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileButtonFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "OMW/ProfileButtonFra...";  // Use this tag for calling Log.d()


    public ProfileButtonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileButtonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileButtonFragment newInstance(String param1, String param2) {
        return new ProfileButtonFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_button, container, false);
        ImageView profilePhoto = view.findViewById(R.id.imageCurrentProfilePhoto);

        User currentUser = UserRequestState.getCurrentUser();
        view.setOnClickListener(this);

        if (currentUser != null) {
            // Download the profile photo for the user and display it
            Log.d(TAG, "Profile image loaded");
            Picasso.get().load(currentUser.getProfilePhotoUrl()).noFade().into(profilePhoto);
        }
        else {
            Log.d(TAG, "There is no current user");
        }

        return view;
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "Button pressed");

        ShowProfileFragment showProfileFragment = ShowProfileFragment.newInstance(null);
        showProfileFragment.show(getFragmentManager());
    }
}
