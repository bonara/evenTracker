package com.example.eventracker.data;

import com.example.eventracker.model.User;

import java.util.ArrayList;


public interface UserListAsyncResponse {
    void processFinished(ArrayList<User> myUserArrayList);
}

