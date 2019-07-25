package com.example.eventracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventracker.adapter.UsersAdapter;
import com.example.eventracker.data.UserData;
import com.example.eventracker.data.UserListAsyncResponse;
import com.example.eventracker.model.User;
import com.example.eventracker.model.Venue;
import com.example.eventracker.model.mEvent;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



public class DetailsActivity extends AppCompatActivity {
    private TextView detsName, detsDescription, detsStart, detsEnd, detsLocaiton, detsRSVP;
    private ImageView detsImage;
    private Button detsYesButton, detsUsers;
    private ArrayList<User> myUserList;
    private ArrayList<User> myUserIdList;
    private String eventId;
    private String token;
    private String userId;
    private AlertDialog userDialog;
    private UsersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detsName = findViewById(R.id.dets_name);
        detsDescription = findViewById(R.id.dets_description);
        detsStart = findViewById(R.id.dets_start);
        detsEnd = findViewById(R.id.dets_end);
        detsLocaiton = findViewById(R.id.dets_location);
        detsRSVP = findViewById(R.id.dets_rsvp);
        detsImage = findViewById(R.id.dets_image);
        detsYesButton = findViewById(R.id.button_yes);
        detsUsers = findViewById(R.id.dets_users);

        final mEvent myEvent = getIntent().getExtras().getParcelable("myEvent");
        final String venueAddress = getIntent().getStringExtra("venueAddress");


        Log.d("venue address", venueAddress);
        if (myEvent != null) {
            eventId = myEvent.getId();
            detsName.setText(myEvent.getName());
            detsDescription.setText(myEvent.getDescription());
            detsStart.setText(myEvent.getStart());
            detsEnd.setText(myEvent.getEnd());
            detsLocaiton.setText(venueAddress);
            Picasso.get().load(myEvent.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(detsImage);

            detsRSVP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(myEvent.getEventUrl()));
                    startActivity(intent);
                }
            });
        }

        SharedPreferences getIdToken = getSharedPreferences("IDTOKEN",Context.MODE_PRIVATE);
        token = getIdToken.getString("idToken", "Nothing");
        userId = getIdToken.getString("username", "none");
        Log.d("shared pref", token);
        Log.d("shared pref", userId);



        myUserIdList = (ArrayList<User>) new UserData().getUserId(eventId, token, new UserListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<User> myUserIdList) {
                Log.d("eventID", eventId);
                Log.d("Inside", "Process finished" + myUserIdList );
                for(User user : myUserIdList){
                    if(user.getUserId() != null && user.getUserId().equals(userId)) {
                        detsYesButton.setText("No");
                        detsYesButton.setBackgroundColor(Color.LTGRAY);
                        Log.d("finished", "username " + user.getUserId());
                        Log.d("userId from shared pref", userId);
                    }
                }

            }
        });

        myUserList = (ArrayList<User>) new UserData().getUser(eventId, token, new UserListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<User> myUserArrayList) {
                Log.d("eventID", eventId);
                Log.d("Inside", "Process finished" );
                Toast.makeText(DetailsActivity.this, "finished", Toast.LENGTH_SHORT).show();
            }
        });


        detsYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        detsUsers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialogMessage(myUserList);

            }
        });

    }


    private void showDialogMessage(ArrayList<User> myUserList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.all_attendees, null);
        builder.setTitle("Attendees");

        ListView listView = (ListView)row.findViewById(R.id.listView);
        mAdapter = new UsersAdapter(this, myUserList);
        listView.setAdapter(mAdapter);

        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();

                } catch (Exception e) {
                    // Log failure
                    Log.d("e", "Dialog failure", e);
                }
            }
        });
        builder.setView(row);


        userDialog = builder.create();
        userDialog.show();
    }
}
