package com.CMPUT301W20T24.OnMyWay;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static androidx.core.content.ContextCompat.getSystemService;

public class DriverHistoryFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference requests = db.collection("riderRequests");
    private ArrayList<DriverHistoryInfo> historyArray = new ArrayList<>();
    User currentUser = UserRequestState.getCurrentUser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Icon made from Google
        View view = inflater.inflate(R.layout.driver_history_button, container, false);
        view.setOnClickListener(this);

        requests.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (documentSnapshot.getString("driverId") != null && documentSnapshot.getString("status").equals("COMPLETE")){
                        if(documentSnapshot.getString("driverId").equals(currentUser.getUserId()))
                        {
                            DriverHistoryInfo temp = new DriverHistoryInfo(
                                    documentSnapshot.getString("requestID"),
                                    documentSnapshot.getString("startLocationName"),
                                    documentSnapshot.getString("endLocationName"),
                                    documentSnapshot.getLong("timeAccepted")
                            );
                            historyArray.add(temp);
                        }
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onClick(View view) {

        if(!isNetworkAvailable()){
            Intent intent = new Intent(getContext(), DriverHistoryActivity.class);
            intent.putParcelableArrayListExtra("TheData", historyArray);
            startActivity(intent);
        }
        else{
            updateList();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateList(){
        historyArray.clear();
        requests.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (documentSnapshot.getString("driverId") != null && documentSnapshot.getString("status").equals("COMPLETE")){
                        if(documentSnapshot.getString("driverId").equals(currentUser.getUserId()))
                        {
                            DriverHistoryInfo temp = new DriverHistoryInfo(
                                    documentSnapshot.getString("requestID"),
                                    documentSnapshot.getString("startLocationName"),
                                    documentSnapshot.getString("endLocationName"),
                                    documentSnapshot.getLong("timeAccepted")
                            );
                            historyArray.add(temp);
                        }
                    }
                }
                Intent intent = new Intent(getContext(), DriverHistoryActivity.class);
                intent.putParcelableArrayListExtra("TheData", historyArray);
                startActivity(intent);
            }
        });


    }

}
