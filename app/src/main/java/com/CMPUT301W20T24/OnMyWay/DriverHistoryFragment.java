package com.CMPUT301W20T24.OnMyWay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DriverHistoryFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Icon made from Google
        View view = inflater.inflate(R.layout.driver_history_button, container, false);

        User currentUser = UserRequestState.getCurrentUser();
        view.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {
        // TODO
    }
}
