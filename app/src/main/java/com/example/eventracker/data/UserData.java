package com.example.eventracker.data;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eventracker.controller.AppController;
import com.example.eventracker.model.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserData {
    Context mContext;


    ArrayList <User> myUserArrayList = new ArrayList<>();
    ArrayList <User> myUserIdList = new ArrayList<>();

    public List<User> getUser(String event, final String token, final UserListAsyncResponse callBack) {
        String url = "https://q2e4y7sz9h.execute-api.us-west-2.amazonaws.com/staging/attendee/?eventId="+ event;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url, (JSONObject) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", "onResponse " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                User myUser = new User();
                                myUser.setUsername(response.getJSONObject(i).getString("username"));
                                myUserArrayList.add(myUser);
                            Log.d("list", "myUserArrayList" + myUserArrayList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != callBack) callBack.processFinished(myUserArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


        return myUserArrayList;
    }

    public List<User> getUserId(String event, final String token, final UserListAsyncResponse callBack) {
        String url = "https://q2e4y7sz9h.execute-api.us-west-2.amazonaws.com/staging/attendee/?eventId="+ event;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url, (JSONObject) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", "onResponse " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                User myUserId = new User();
                                myUserId.setUserId(response.getJSONObject(i).getString("userId"));
                                myUserIdList.add(myUserId);
                                Log.d("list", "myUserArrayList" + myUserIdList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != callBack) callBack.processFinished(myUserIdList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


        return myUserIdList;
    }

}
