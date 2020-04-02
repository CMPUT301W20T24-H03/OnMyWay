package com.CMPUT301W20T24.OnMyWay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
*
* shows fragment when driver accepts request
 * author Bardia Samimi
 */

public class ShowRiderSeesDriverAccept extends DialogFragment implements View.OnClickListener {

    private FragmentManager fm;

    public ShowRiderSeesDriverAccept() {
        // Empty constructor required here
        // Use newInstance() to create a new instance of the dialog
    }

    public static ShowRiderSeesDriverAccept newInstance() {

        ShowRiderSeesDriverAccept fragment = new ShowRiderSeesDriverAccept();

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
        View view = inflater.inflate(R.layout.frag_rider_sees_driver_accept, container);

        Button okButton = view.findViewById(R.id.buttonOK);
        okButton.setOnClickListener(this);

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        this.fm = fragmentManager;
        this.show(fragmentManager, "show_rider_sees_driver_accept");
    }

    @Override
    public void onClick(View view) {

        final int viewId = view.getId();

        if (viewId == R.id.buttonOK) {
            this.dismiss(); // Close the dialog
        }
        else {
            throw new NullPointerException("No button with the correct ID was found");
        }
    }
}
