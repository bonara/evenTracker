package com.example.eventracker.data;

import com.example.eventracker.model.mEvent;

import java.util.ArrayList;

public interface EventListAsyncResponse {
    void processFinished(ArrayList<mEvent> myEventArrayList);
}

