package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class DriverHistoryFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference requests = db.collection("riderRequests");

    private ArrayList<DriverHistoryInfo> foo = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Icon made from Google
        View view = inflater.inflate(R.layout.driver_history_button, container, false);
        view.setOnClickListener(this);

        User currentUser = UserRequestState.getCurrentUser();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + currentUser.getUserId());
        requests.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (documentSnapshot.getString("driverId") != null){
                        if(documentSnapshot.getString("driverId").equals(currentUser.getUserId()))
                        {
                            DriverHistoryInfo temp = new DriverHistoryInfo(documentSnapshot.getString("requestID"), documentSnapshot.getString("startLocationName"), documentSnapshot.getString("endLocationName"));
                            foo.add(temp);
                        }
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), DriverHistoryActivity.class);
        intent.putParcelableArrayListExtra("TheData", foo);
        startActivity(intent);
    }

    public void openActivity(){

    }
}
