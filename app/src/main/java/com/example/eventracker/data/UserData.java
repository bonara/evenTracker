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

//    String token = "eyJraWQiOiJsdm5vOWR0MjloUjVvZVZ3TVphTVlFSjVcL29aSE9XeU83WFVvNTJCenlBOD0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiWW96LU44aHlUTE0wb3NNN2x5Ui1WUSIsInN1YiI6IjkwYjk2MGM3LWZkYTQtNGNjNC05M2I0LTg3NGVkZTBiZmY5ZCIsImF1ZCI6IjM4NGswMjdjNmJrMDcwZGdnN3M4MzBsZG5rIiwiY29nbml0bzpncm91cHMiOlsidXMtd2VzdC0yX09FOThNeUpwTV9Hb29nbGUiXSwiaWRlbnRpdGllcyI6W3sidXNlcklkIjoiMTA2NDA3MTIzMTU0MDQ0Mjk5MDM1IiwicHJvdmlkZXJOYW1lIjoiR29vZ2xlIiwicHJvdmlkZXJUeXBlIjoiR29vZ2xlIiwiaXNzdWVyIjpudWxsLCJwcmltYXJ5IjoidHJ1ZSIsImRhdGVDcmVhdGVkIjoiMTU2MzE2NjQzODk4NSJ9XSwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE1NjM5MTE2NzUsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC51cy13ZXN0LTIuYW1hem9uYXdzLmNvbVwvdXMtd2VzdC0yX09FOThNeUpwTSIsImNvZ25pdG86dXNlcm5hbWUiOiJHb29nbGVfMTA2NDA3MTIzMTU0MDQ0Mjk5MDM1IiwiZXhwIjoxNTYzOTE4OTAzLCJpYXQiOjE1NjM5MTUzMDMsImVtYWlsIjoibmFyYXNoa2E1MjBAZ21haWwuY29tIn0.ZwI75tcgZhf9xUumF9RN8NbeoIDCLWDBaYAI32cYOoE9wSYuLFB1wCCCKy9uBDruwv--nubYKFFu7zuuegBlI7xhxEsJ_zPrpAC8dfI1Q9OXGk1bt2QITomRWpY5P-qyhlk4b50quO_1k_qvT8LV7l9ANRi0b9IAhgL6CoeDsgV00awjJhwHDWsgemqrYybxos-29bESC9AiDtrl6BUksBWamGw9Jx7t_wiYX_sFibf1E5VlskDbMgtmtQmlq0n9WKKx2AbhHH1UiaNpFI3btmQfca_XFWP2Ahwy85xcyQRVW3V0dj3wmH_qjGW-mHD03ZctqzvFxNZFJBkXjATbtA";


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

}
