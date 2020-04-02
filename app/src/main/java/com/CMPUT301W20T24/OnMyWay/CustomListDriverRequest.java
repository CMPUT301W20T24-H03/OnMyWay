package com.CMPUT301W20T24.OnMyWay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListDriverRequest extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context context;

    public CustomListDriverRequest(Context context, ArrayList<Request> requests){
        super(context,0,requests);
        this.requests = requests;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // check
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.custom_list_content,parent,false);
        }

        Request request = requests.get(position);

        // setting TextView's that will be added to listview
        TextView usernameText = view.findViewById(R.id.riderUser);
        TextView latText = view.findViewById(R.id.riderlat);
        TextView longText = view.findViewById(R.id.riderlong);
        TextView paymentText = view.findViewById(R.id.paymentAmount);

        // setting TextView's
        usernameText.setText(request.getRiderId());
        latText.setText(String.valueOf(request.getStartLatitude()));
        longText.setText(String.valueOf(request.getEndLatitude()));

        return view;
    }
}
