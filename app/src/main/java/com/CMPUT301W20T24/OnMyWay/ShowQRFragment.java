package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
/**
 * Shows the QR-buck barcode for the driver to scan
 * @author mahin
 */
/// CodePath, Using DialogFragment
/// https://guides.codepath.com/android/using-dialogfragment

public class ShowQRFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "OMW/ShowProfileFragment";  // Use this tag for call Log.d()
    private static boolean isCurrentUser = false;

    private Button editButton;
    private Button logoutButton;
    private static Request request;
    private ImageView qrCode;

    public ShowQRFragment() {
        // Empty constructor required here
        // Use newInstance() to create a new instance of the dialog
    }

    public static ShowQRFragment newInstance(User user) {
        if (user == null) {
            Log.d(TAG, "Creating profile dialog for current user");
            user = UserRequestState.getCurrentUser();

            isCurrentUser = true;
        }
        else {
            Log.d(TAG, "Creating profile dialog for some user");
            isCurrentUser = false;
        }

        ShowQRFragment fragment = new ShowQRFragment();
        Bundle args = new Bundle();
        request = UserRequestState.getCurrentRequest();

        args.putString("userId", user.getUserId());
        args.putString("amount", request.getPaymentAmount());
        args.putString("requestID", request.getRequestId());

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
        View view = inflater.inflate(R.layout.fragment_show_qr_buck, container);

        Button backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get imageview
        qrCode= view.findViewById(R.id.QRcode);

        //get the information that you have to put in the barcode and then convert that to a string
        // with ';' separating every new field
        // that string is "text"
        Bundle bundle = getArguments();
        String amount = bundle.getString("amount", "-");
        String requestID = bundle.getString("requestID","");


        String text = requestID + "; " + amount;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
        else {
            throw new NullPointerException("No button with the correct ID was found");
        }
    }
}