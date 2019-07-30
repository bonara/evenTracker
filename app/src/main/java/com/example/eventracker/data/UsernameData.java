package com.example.eventracker.data;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventracker.controller.AppController;

import java.util.HashMap;
import java.util.Map;

public class UsernameData {

    String usernameResponse;

    public String getUsername(final String token, final AttendingAsyncResponse callBack){
        String url = "https://q2e4y7sz9h.execute-api.us-west-2.amazonaws.com/staging/user";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        usernameResponse = response;
                        if (null != callBack) callBack.processFinished(usernameResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != callBack) callBack.onError(error.toString());
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(request);

        return usernameResponse;
    }

}
