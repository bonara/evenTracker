package com.example.eventracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.eventracker.R;
import com.amazonaws.mobileconnectors.cognitoauth.util.JWTParser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * Authenticated user.
 */
public class AuthUserFragment extends Fragment {

    private static final boolean SIGN_OUT = false;
    private static final String TAB = "    ";

    // User tokens.
    private String accessToken;
    private String idToken;

    // Wiring up the user interface.
    private Button userButton;
    private CardView accessTokenCard;
    private CardView idTokenCard;
    private TextView userEmail;

    private OnFragmentInteractionListener mListener;

    public AuthUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accessToken = getArguments().getString(getString(R.string.app_access_token));
            Log.d("-- frag access: ", accessToken);
            idToken = getArguments().getString(getString(R.string.app_id_token));
            Log.d("-- frag id: ", idToken);
//
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth_user, container, false);
        wireUp(view);
        showCards(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface with the Activity.
     */
    public interface OnFragmentInteractionListener {
        void onButtonPress(boolean signIn);
        void showPopup(String title, String content);
    }

    /**
     * Communicate button press to the Activity.
     */
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onButtonPress(SIGN_OUT);
        }
    }

    /**
     * Show token.
     * @param title Token name.
     * @param token Token payload.
     */
    public void onCardSelected(String title, String token) {
        if (mListener != null) {
            mListener.showPopup(token, title );
        }
    }

    /**
     * Get references to view components.
     * @param view {@link View}.
     */
    private void wireUp(View view) {
        userButton = (Button) view.findViewById(R.id.buttonSignout);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
    }

    /**
     * Display card if the token is available.
     * @param view
     */
    private void showCards(View view) {
        accessTokenCard = (CardView) view.findViewById(R.id.card_view_access);
        idTokenCard = (CardView) view.findViewById(R.id.card_view_id);

        userEmail = view.findViewById(R.id.user_email);
        if (accessToken != null && !accessToken.isEmpty()) {
            accessTokenCard.setVisibility(View.VISIBLE);
            accessTokenCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardSelected("Access Token",
                            prettyPrintJWT(JWTParser.getPayload(accessToken), TAB));
                }
            });
        } else {
            accessTokenCard.setVisibility(View.INVISIBLE);
        }

        if (idToken != null && !idToken.isEmpty()) {
            JSONObject payload = JWTParser.getPayload(idToken);


            try {
                userEmail.setText(payload.getString("email"));
                Log.i("email", payload.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String prettyidToken = prettyPrintJWT(JWTParser.getPayload(idToken),TAB);
        Log.d("idToken",idToken);


        Log.d("idToken", prettyPrintJWT(JWTParser.getPayload(accessToken),TAB));

        if (idToken != null && !idToken.isEmpty()) {
            idTokenCard.setVisibility(View.VISIBLE);
            idTokenCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardSelected("Id Token",
                            prettyPrintJWT(JWTParser.getPayload(idToken), TAB));
                }
            });
        } else {
            idTokenCard.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Pretty print flat Json.
     */
    private String prettyPrintJWT(JSONObject jwtJson, String tab) {
        String seperator = " : ";
        StringBuilder prettyPrintBuilder = new StringBuilder();
        prettyPrintBuilder.append("{");
        if (jwtJson != null) {
            try {
                Iterator<String> jsonIterator = jwtJson.keys();
                while (jsonIterator.hasNext()) {
                    String key = jsonIterator.next();
                    prettyPrintBuilder.append("\n").append(tab).append(key).append(seperator).append(jwtJson.get(key));
                }
            } catch (Exception e) {

            }
        }
        prettyPrintBuilder.append("\n}");
        return  prettyPrintBuilder.toString();
    }
}
