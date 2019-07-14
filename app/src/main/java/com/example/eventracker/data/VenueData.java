package com.example.eventracker.data;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eventracker.controller.AppController;
import com.example.eventracker.model.mEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VenueData {

    ArrayList<mEvent> myEventArrayList = new ArrayList<>();



    public List<mEvent> getVenue(String venue, final EventListAsyncResponse callBack) {
        String url = "https://q2e4y7sz9h.execute-api.us-west-2.amazonaws.com/staging/venues/?venue="+ venue;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "onResponse " + response);

                                mEvent myEvent = new mEvent();
//                                myEvent.setName(response.getJSONObject(1).getString("name"));

                                myEventArrayList.add(myEvent);
//                            Log.d("Response", "mes" + response.getJSONObject(i));
//                            Log.d("Response", "mes" + myEventArrayList);

                        if (null != callBack) callBack.processFinished(myEventArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


        return myEventArrayList;
    }

}