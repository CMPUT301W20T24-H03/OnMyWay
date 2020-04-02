package com.CMPUT301W20T24.OnMyWay;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;


public class DriverHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_history);
        ListView my_Listview = (ListView) findViewById(R.id.driver_history_listview);

        ArrayList list = (ArrayList) getIntent().getParcelableArrayListExtra("TheData");

        DriverHistoryAdapter adapter = new DriverHistoryAdapter(this, R.layout.driver_history_list_adapter, list);
        my_Listview.setAdapter(adapter);

    }
}
