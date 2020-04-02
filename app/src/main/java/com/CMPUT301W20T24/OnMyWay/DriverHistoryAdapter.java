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

public class DriverHistoryAdapter extends ArrayAdapter<DriverHistoryInfo> {
    private Context my_context;
    int my_resource;

    public DriverHistoryAdapter(Context context, int resource, ArrayList<DriverHistoryInfo> list){
        super(context, resource, list);
        my_context = context;
        my_resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String history_id = getItem(position).getHistory_id();
        String history_start = getItem(position).getHistory_start();
        String history_end = getItem(position).getHistory_dest();
        Long history_time = getItem(position).getHistory_time();

        LayoutInflater inflater = LayoutInflater.from(my_context);
        convertView = inflater.inflate(my_resource, parent, false);

        TextView textview_id = (TextView) convertView.findViewById(R.id.tvID);
        TextView textview_start = (TextView) convertView.findViewById(R.id.tvStart);
        TextView textview_end = (TextView) convertView.findViewById(R.id.tvDest);
        TextView textview_time = (TextView) convertView.findViewById(R.id.tvTime);

        String date;
        RequestTime temp_date = new RequestTime(history_time);
        date = temp_date.getDate();

        textview_id.setText(history_id);
        textview_start.setText(history_start);
        textview_end.setText(history_end);
        textview_time.setText(date);

        return convertView;
    }
}
