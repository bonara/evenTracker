package com.example.eventracker.data;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eventracker.controller.AppController;
import com.example.eventracker.model.mEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventData {

    ArrayList<mEvent> myEventArrayList = new ArrayList<>();
//    private String latitude = "47.608013";
//    private String longitude = "-122.335167";


    public List<mEvent> getEvents(String latitude, String longitude, final EventListAsyncResponse callBack) {
        String url = "https://q2e4y7sz9h.execute-api.us-west-2.amazonaws.com/staging/events/?longitude="+longitude+"&latitude="+latitude;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            url, (JSONArray) null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("Response", "onResponse " + response);
                    for (int i = 0; i < response.length(); i++){
                        try {
                            mEvent myEvent = new mEvent();
                            myEvent.setName(response.getJSONObject(i).getString("name"));
                            myEvent.setDescription(response.getJSONObject(i).getString("description"));
                            myEvent.setId(response.getJSONObject(i).getString("id"));
                            myEvent.setEventUrl(response.getJSONObject(i).getString("event_url"));
                            myEvent.setStart(response.getJSONObject(i).getString("start"));
                            myEvent.setEnd(response.getJSONObject(i).getString("end"));
                            myEvent.setTimezone(response.getJSONObject(i).getString("timezone"));
                            myEvent.setOrganizer_id(response.getJSONObject(i).getString("organizer_id"));
                            myEvent.setFree(response.getJSONObject(i).getBoolean("is_free"));
                            myEvent.setSummary(response.getJSONObject(i).getString("summary"));
                            myEvent.setImageUrl(response.getJSONObject(i).getString("logo_url"));
                            myEvent.setVenueId(response.getJSONObject(i).getString("venue_id"));

                            myEventArrayList.add(myEvent);
//                            Log.d("Response", "mes" + response.getJSONObject(i));
//                            Log.d("Response", "mes" + myEventArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != callBack) callBack.processFinished(myEventArrayList);
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


        return myEventArrayList;
    }

}
