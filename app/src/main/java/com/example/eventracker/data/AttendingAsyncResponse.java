package com.example.eventracker.data;

import com.example.eventracker.model.User;

import java.util.ArrayList;

public interface AttendingAsyncResponse {
    void processFinished(String attendingResponse);
    void onError(String message);
}

