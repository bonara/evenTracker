package com.example.eventracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

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

    private String userLat = "40.730610";
    private String userLong = "-73.935242";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_user:
//                Toast.makeText(this, "msg" + "Login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserAccount.class);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences getSharedDate = getSharedPreferences("LOCATION",MODE_PRIVATE);
//        String userLat = getSharedDate.getString("latitide","47.608013");
//        String userLong = getSharedDate.getString("longitude","-122.335167");
//        Log.d("shared", userLat);
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            userLat = data.getStringExtra("lat");
            userLong = data.getStringExtra("long");
            Log.d("lat", data.getStringExtra("lat"));
            Log.d("long", data.getStringExtra("long"));
            myEventList = new EventData().getEvents(userLat, userLong, new EventListAsyncResponse() {
                @Override
                public void processFinished(ArrayList<mEvent> myEventArrayList) {
                    Log.d("Inside", "Process finished" + myEventArrayList);


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, UserLocation.class);

        this.startActivityForResult(intent, 1);


    }

}