package com.example.eventracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventracker.model.Venue;
import com.example.eventracker.model.mEvent;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private TextView detsName, detsDescription, detsStart, detsEnd, detsLocaiton, detsRSVP;
    private ImageView detsImage;

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

        final mEvent myEvent = getIntent().getExtras().getParcelable("myEvent");
        final String venueAddress = getIntent().getStringExtra("venueAddress");


        Log.d("venue address", venueAddress);
        if (myEvent != null) {
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


//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            String name = bundle.getString("name");
//            String summary = bundle.getString("summary");
//
//            detsName.setText(name);
//            detsSummary.setText(summary);
    }
}
