package com.example.eventracker.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.fragment.app.Fragment;

import com.amazonaws.mobileconnectors.cognitoauth.util.ClientConstants;
import com.example.eventracker.R;


/**
 * Unauthenticated user.
 */
public class UnauthUserFragment extends Fragment {
    private static final boolean SIGN_IN = true;

    private OnFragmentInteractionListener mListener;

    private Button userButton;
    private Button clearGoogleButton;

    // copy+paste
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsIntent mCustomTabsIntent;
    private CustomTabsServiceConnection mCustomTabsServiceConnection;

    public UnauthUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unauth_user, container, false);
        wireUp(view);
        return view;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onButtonPress(SIGN_IN);
        }
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
    }

    private void wireUp(View view) {
        userButton = (Button) view.findViewById(R.id.buttonSignin);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        clearGoogleButton = (Button) view.findViewById(R.id.buttonClearGoogle);
        clearGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preWarmChrome();
            }
        });
    }


    // copy+paste
    public void unbindServiceConnection() {
        Context context = getActivity().getApplicationContext();
        if(mCustomTabsServiceConnection != null)
            context.unbindService(mCustomTabsServiceConnection);
    }

    private void launchCustomTabs(final Uri uri) {
        Log.i("CusomTabs", "launchCustomTabs: "+uri.toString());
        Context context = getActivity().getApplicationContext();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(mCustomTabsSession);
        mCustomTabsIntent = builder.build();
        mCustomTabsIntent.intent.setPackage(ClientConstants.CHROME_PACKAGE);
        mCustomTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        mCustomTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCustomTabsIntent.launchUrl(context, uri);
    }

    private void preWarmChrome() {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(final ComponentName name, final CustomTabsClient client) {
                Log.i("CusomTabs", "prewarm1");
                mCustomTabsClient = client;
                mCustomTabsClient.warmup(0L);
                mCustomTabsSession = mCustomTabsClient.newSession(customTabsCallback);

                launchCustomTabs(Uri.parse("https://accounts.google.com/Logout"));
            }

            @Override
            public void onServiceDisconnected(final ComponentName name) {
                Log.i("CusomTabs", "prewarm2");
                mCustomTabsClient = null;
            }
        };
        Context context = getActivity().getApplicationContext();
        boolean chromeState = CustomTabsClient.bindCustomTabsService(context,
                ClientConstants.CHROME_PACKAGE, mCustomTabsServiceConnection);
    }

    private final CustomTabsCallback customTabsCallback = new CustomTabsCallback() {
        @Override
        public void onNavigationEvent(final int navigationEvent, final Bundle extras) {
            super.onNavigationEvent(navigationEvent, extras);
            Log.i("CusomTabs", "onNavigationEvent: "+navigationEvent);
            if (navigationEvent == 2) {
                Log.i("CusomTabs", "customTab hidden callback, code has already been received: ");
            }
        }
    };
}