package com.example.eventracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.eventracker.MainActivity;
import com.example.eventracker.R;
import com.example.eventracker.UserLocation;
import com.example.eventracker.adapter.RecyclerViewAdapter;
import com.example.eventracker.data.EventData;
import com.example.eventracker.data.EventListAsyncResponse;
import com.example.eventracker.model.mEvent;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AllEventsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RequestQueue requestQueue;
    private List<mEvent> myEventList;
    private ProgressBar progressBar;

    private String userLat = "40.730610";
    private String userLong = "-73.935242";
    View v;
    public AllEventsFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_all_events, container,false);
        return v;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            userLat = data.getStringExtra("lat");
            userLong = data.getStringExtra("long");
            Log.d("lat", data.getStringExtra("lat"));
            Log.d("long", data.getStringExtra("long"));
            listMyEvents();
        }else {
            listMyEvents();
        }

    }

    private void listMyEvents() {
        myEventList = new EventData().getEvents(userLat, userLong, new EventListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<mEvent> myEventArrayList) {
                Log.d("Inside", "Process finished" + myEventArrayList);


                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AllEventsFragment.this));

                //setup adapter
                recyclerViewAdapter = new RecyclerViewAdapter(AllEventsFragment.this, myEventList);

                recyclerView.setAdapter(recyclerViewAdapter);
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }



}
