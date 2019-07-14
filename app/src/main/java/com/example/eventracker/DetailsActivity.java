package com.example.eventracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.eventracker.model.mEvent;

public class DetailsActivity extends AppCompatActivity {
    private TextView detsName, detsDescription, detsStart, detsEnd,detsRSVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detsName = findViewById(R.id.dets_name);
        detsDescription = findViewById(R.id.dets_description);
        detsStart = findViewById(R.id.dets_start);
        detsEnd = findViewById(R.id.dets_end);
        detsRSVP = findViewById(R.id.dets_rsvp);


        mEvent myEvent = getIntent().getExtras().getParcelable("myEvent");

        if (myEvent != null) {
            detsName.setText(myEvent.getName());
            detsDescription.setText(myEvent.getDescription());
            detsStart.setText(myEvent.getStart());
            detsEnd.setText(myEvent.getEnd());
        }


//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            String name = bundle.getString("name");
//            String summary = bundle.getString("summary");
//
//            detsName.setText(name);
//            detsSummary.setText(summary);
    }
}
