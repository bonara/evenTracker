package com.example.eventracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventracker.data.UserData;
import com.example.eventracker.data.UserListAsyncResponse;
import com.example.eventracker.model.User;
import com.example.eventracker.model.Venue;
import com.example.eventracker.model.mEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private TextView detsName, detsDescription, detsStart, detsEnd, detsLocaiton, detsRSVP;
    private ImageView detsImage;
    private Button detsYesButton, detsUsers;
    private List<User> myUserList;
    private String eventId;
    private String token;

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


//        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
////        SharedPreferences.Editor idToken = editor.putString("idToken", "test");
//        editor.clear();
//        editor.commit();

        SharedPreferences getIdToken = getSharedPreferences("IDTOKEN",Context.MODE_PRIVATE);
        token = getIdToken.getString("idToken", "Nothing");
        Log.d("shared pref", token);

        detsYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                myUserList = new UserData().getUser(eventId, token, new UserListAsyncResponse() {
                    @Override
                    public void processFinished(ArrayList<User> myUserArrayList) {
                        Log.d("eventID", eventId);
                        Log.d("Inside", "Process finished" );
                        Toast.makeText(DetailsActivity.this, "nothing", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
