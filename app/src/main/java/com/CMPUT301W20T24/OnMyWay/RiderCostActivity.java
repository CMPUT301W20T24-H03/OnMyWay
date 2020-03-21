package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * This screen is displayed after a driver has accepted a request
 * a cost will be calculated on the basis of the distance between locations
 * @author Payas
 */
// This screen will be displayed after a driver has accepted a request
// Rider will be shown an estimate of the cost of the ride and
// will be given the option to edit
public class RiderCostActivity extends AppCompatActivity {
    private static final String TAG = "OMW/RiderCostActivity";  // Use this tag for calling Log.d()
    private String cost;
    LinearLayout editField;
    EditText newPrice;
    private FirebaseFirestore db;


    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(RiderCostActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_cost);
        editField = findViewById(R.id.field_priceEntry);
        newPrice = findViewById(R.id.editText_price);
        db = FirebaseFirestore.getInstance();

        // layout features from RiderMapActivity that are removed
        //LinearLayout layout = (LinearLayout) findViewById(R.id.locationSearchBar);
        //layout.setVisibility(View.GONE);
        //Button button = (Button) findViewById(R.id.switchModeButton);
        //button.setVisibility(View.GONE);

        // first we will have to check if payment amount is null or not - need to implement still
        // if not then calculate, otherwise retrieve from database

        calculateCost();

        // on clicking the edit button, allow the user
        // to edit the price
        final Button editPriceButton = findViewById(R.id.edit_price_button);
        editPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField.setVisibility(View.VISIBLE);
            }
        });
        final Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cost = newPrice.getText().toString();
                TextView costText = (TextView) findViewById(R.id.priceEstimate);
                costText.setText("$" + cost);
                newPrice.getText().clear();
                editField.setVisibility(View.INVISIBLE);
            }
        });
        // if the confirm button is clicked then the rider has settled on this price,
        // store this price in teh database as the payment Amount
        final Button confirmButton = findViewById(R.id.confirm_price_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // store to the request in the database
                // lead to next activity
            }
        });

    }

    /**
     * this method will calculate the distance between two locations
     * and find a cost based on the distance
     * the locations are pulled from the Riderequests collection in
     * firestore database
     * @author Payas
     */
    public void calculateCost(){
        // need document path from RiderMapActivity
        // as of now the document path is hardcoded
        // retrieve start and end locations from firestore database
        /// Google Firebase, Get data with Cloud Firestore
        /// https://firebase.google.com/docs/firestore/query-data/get-data
        Location startLocation = new Location("startLocation");
        Location endLocation = new Location("endLocation");
        DocumentReference docRef = db.collection("riderRequests").document("CtnTp0ltVK2xx44MwWFZ");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String startLat = document.getString("startLatitude");
                        String startLong = document.getString("startLongitude");
                        String endLat = document.getString("endLatitude");
                        String endLong = document.getString("endLongitude");
                        startLocation.setLatitude(Double.parseDouble(startLat));
                        startLocation.setLongitude(Double.parseDouble(startLong));
                        endLocation.setLatitude(Double.parseDouble(endLat));
                        endLocation.setLongitude(Double.parseDouble(endLong));
                        // this calculation will be shifted out of this method later
                        // once the listeners for this method are complete
                        float distance;
                        distance = startLocation.distanceTo(endLocation)/600 ;
                        cost = String.format("%.2f", distance);
                        TextView costText = (TextView) findViewById(R.id.priceEstimate);
                        costText.setText("$" + cost);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
