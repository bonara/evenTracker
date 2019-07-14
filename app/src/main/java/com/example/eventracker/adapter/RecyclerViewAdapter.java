package com.example.eventracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.eventracker.DetailsActivity;
import com.example.eventracker.MainActivity;
import com.example.eventracker.R;
import com.example.eventracker.data.VenueData;
import com.example.eventracker.data.EventListAsyncResponse;
import com.example.eventracker.model.mEvent;


import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    private Context context;
    private List<mEvent> myEventList;

    private RequestQueue requestQueue;
    private List<mEvent> myVenue;

    public RecyclerViewAdapter(Context context, List<mEvent> myEventList) {
        this.context = context;
        this.myEventList = myEventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mevent_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        mEvent myEvent = myEventList.get(position); //each mEvent object inside of our list
        viewHolder.name.setText(myEvent.getName());
        viewHolder.summary.setText(myEvent.getSummary());

    }


    @Override
    public int getItemCount() {
        return myEventList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView summary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            summary = itemView.findViewById(R.id.summary);


        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            mEvent myEvent = myEventList.get(position);

//            intent.putExtra("name", myEvent.getName());
//            intent.putExtra("summary", myEvent.getSummary());
//
//            context.startActivity(intent);
//            Log.d("Clicked", "OnClick "+ view.getId());

//            Log.d("Clicked", "OnClick "+ myEvent.getVenueId());
            myVenue = new VenueData().getVenue(myEvent.getVenueId(), new EventListAsyncResponse() {
                @Override
                public void processFinished(ArrayList<mEvent> myEventArrayList) {
                    Log.d("Inside", "Process finished" + myEventArrayList);

                    int position = getAdapterPosition();
                    mEvent myEvent = myEventList.get(position);

                    Intent intent = new Intent(context, DetailsActivity.class);


                    intent.putExtra("myEvent", myEvent);

                    context.startActivity(intent);

                }

            });
        }
    }
}
