package com.example.eventracker.data;

import java.util.ArrayList;
import com.example.eventracker.model.Venue;


public interface VenueListAsyncResponse {
    void processFinished(ArrayList<Venue> myVenueArrayList);
}

