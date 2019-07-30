package com.example.eventracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.amazonaws.mobileconnectors.cognitoauth.util.JWTParser;
import com.example.eventracker.R;
import com.example.eventracker.data.AttendingAsyncResponse;
import com.example.eventracker.data.UsernameData;

import org.json.JSONException;
import org.json.JSONObject;


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
    private TextView username;
    private ImageView icon;

    private OnFragmentInteractionListener mListener;

    public AuthUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            accessToken = getArguments().getString(getString(R.string.app_access_token));
            idToken = getArguments().getString(getString(R.string.app_id_token));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth_user, container, false);
        wireUp(view);
        showProfile(view);
        showUsername(view);
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
            mListener.showPopup(title, token);
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


    private void showProfile(View view) {
        icon = view.findViewById(R.id.user_icon);
        userEmail = view.findViewById(R.id.user_email);
        if (idToken != null && !idToken.isEmpty()) {
            JSONObject payload = JWTParser.getPayload(idToken);

            try {
                icon.setImageResource(R.drawable.woman_icon);
                userEmail.setText(payload.getString("email"));
                Log.i("email", payload.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showUsername(final View view) {
        new UsernameData().getUsername(idToken, new AttendingAsyncResponse() {
            @Override
            public void processFinished(String usernameResponse) {
                Log.d("username", usernameResponse );
                username = view.findViewById(R.id.username);
                username.setText(usernameResponse.replace("\"", ""));
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
