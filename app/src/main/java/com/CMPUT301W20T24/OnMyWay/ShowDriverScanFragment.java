package com.CMPUT301W20T24.OnMyWay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.CMPUT301W20T24.OnMyWay.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Text;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;



public class ShowDriverScanFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "OMW/ShowDriverScanFragment";  // Use this tag for call Log.d()
    private static boolean isCurrentUser = false;
    private static final int BARCODE_READER_REQUEST_CODE = 1 ;

    Button backButton;
    Button scanQR;
    TextView money;
    TextView accept_dialog;

    private static Request request;

    public ShowDriverScanFragment() {
        // Empty constructor required here
        // Use newInstance() to create a new instance of the dialog
    }


    @SuppressLint("LongLogTag")
    public static ShowDriverScanFragment newInstance(User user) {
        if (user == null) {
            Log.d(TAG, "User null. checking current user");
            user = UserRequestState.getCurrentUser();
            isCurrentUser = true;
        }
        else {
            Log.d(TAG, "trying to scan now");
            isCurrentUser = false;
        }

        ShowDriverScanFragment fragment = new ShowDriverScanFragment();
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
        View view = inflater.inflate(R.layout.fragment_show_payment, container);
        backButton = view.findViewById(R.id.buttonBack);
        scanQR = view.findViewById(R.id.scan_qr_buck);
        money = view.findViewById(R.id.money_money_money);
        accept_dialog = view.findViewById(R.id.accept_payment);
        backButton.setOnClickListener(this);
        scanQR.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // An easier function call to use since we don't need the tag
    public void show(FragmentManager fragmentManager) {
        this.show(fragmentManager, "show_profile_fragment");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View view) {
        Log.d(TAG, "Button pressed");
        final int viewId = view.getId();
        Activity parentActivity = getActivity();

        if (viewId == R.id.buttonBack) {
            Toast.makeText(parentActivity, "Back button pressed",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Back button pressed");

            this.dismiss(); // Close the dialog
        }
        else if(viewId == R.id.scan_qr_buck ){
            Toast.makeText(parentActivity, "Scan buck pressed", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Scan QR button pressed");
            Intent intent = new Intent(this.getActivity().getApplicationContext(), BarcodeCaptureActivity.class);
            startActivityForResult(intent, ShowDriverScanFragment.BARCODE_READER_REQUEST_CODE);

        }
        else {
            throw new NullPointerException("No button with the correct ID was found");
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("Barcode");
                    Point[] p = barcode.cornerPoints;
                    String info = barcode.displayValue;
                    String[] arrOfStr = info.split(";");
                    money.setText("$" + arrOfStr[1]);
                    scanQR.setVisibility(View.INVISIBLE);
                    accept_dialog.setVisibility(View.VISIBLE);
                    money.setVisibility(View.VISIBLE);
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