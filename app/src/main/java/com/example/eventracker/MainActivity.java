package com.example.eventracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.eventracker.adapter.RecyclerViewAdapter;
import com.example.eventracker.controller.AppController;
import com.example.eventracker.data.EventData;
import com.example.eventracker.data.EventListAsyncResponse;
import com.example.eventracker.model.mEvent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RequestQueue requestQueue;
    private List<mEvent> myEventList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEventList= new EventData().getEvents(new EventListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<mEvent> myEventArrayList) {
                Log.d("Inside", "Process finished" +myEventArrayList);


                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                //setup adapter
                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, myEventList);

                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });

    }
}