package com.example.eventracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.eventracker.adapter.RecyclerViewAdapter;
import com.example.eventracker.data.EventData;
import com.example.eventracker.data.EventListAsyncResponse;
import com.example.eventracker.model.mEvent;

import java.util.ArrayList;
import java.util.List;

//import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RequestQueue requestQueue;
    private List<mEvent> myEventList;
    private ProgressBar progressBar;

    private String userLat = "40.730610";
    private String userLong = "-73.935242";

//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//    private ViewPagerAdapter adapter;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Events");
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, UserLocation.class);

        this.startActivityForResult(intent, 1);

//        tabLayout = findViewById(R.id.tabloyout_id);
//        viewPager = findViewById(R.id.viewpager_id);
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        adapter.AddFragment(new AllEventsFragment(), "All");
//        adapter.AddFragment(new AllEventsFragment(), "Test");
//        adapter.AddFragment(new AllEventsFragment(), "test");
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);

    }
    private void listMyEvents() {
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
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

}