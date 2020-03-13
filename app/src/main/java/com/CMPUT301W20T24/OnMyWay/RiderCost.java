package com.CMPUT301W20T24.OnMyWay;

import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.Distribution;

import java.util.Locale;

// This screen will be displayed after a driver has accepted a request
// Rider will be shown an estimate of the cost of the ride and
// will be given the option to edit
public class RiderCost extends AppCompatActivity {

    private String cost;
    LinearLayout editField;
    EditText newPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_cost);
        editField = findViewById(R.id.field_priceEntry);
        newPrice = findViewById(R.id.editText_price);

        LinearLayout layout = (LinearLayout) findViewById(R.id.locationSearchBar);
        layout.setVisibility(View.GONE);
        Button button = (Button) findViewById(R.id.switchModeButton);
        button.setVisibility(View.GONE);
        calculateCost();
        // Set the TextView to the calculated cost
        TextView costText = (TextView) findViewById(R.id.priceEstimate);
        costText.setText("$" + cost);

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
                // NOTE: after confirm is pressed, request creation is done (((but
                // also need to know if ride was accepted)))
                // so now lead to next activity for rider -
                // - view details of ride

            }
        });

    }

    // this method will calculate the cost based on the
    // distance between the two locations
    public void calculateCost(){
        // NOTE:  need to incorporate the lat and long later from request
        // right now - Cameron to MacEwan University
        float distance = 0;
        Location startLoc = new Location("startLoc");
        startLoc.setLatitude(53.526620);
        startLoc.setLongitude(-113.523791);

        Location endLoc = new Location("endLoc");
        endLoc.setLatitude(53.547031);
        endLoc.setLongitude(-113.507143);
        distance = startLoc.distanceTo(endLoc) / 195 ;
        cost = String.format("%.2f", distance);
    }
}
