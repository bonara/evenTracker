package com.example.eventracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.DialogInterface;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.cognitoauth.util.ClientConstants;
import com.example.eventracker.fragments.UnauthUserFragment;
import com.example.eventracker.fragments.AuthUserFragment;
import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amazonaws.mobileconnectors.cognitoauth.AuthUserSession;
import com.amazonaws.mobileconnectors.cognitoauth.handlers.AuthHandler;



public class UserAccount extends AppCompatActivity
        implements AuthUserFragment.OnFragmentInteractionListener,
        UnauthUserFragment.OnFragmentInteractionListener {
    private static final String TAG = "CognitoAuth";
    private Auth auth;
    private AlertDialog userDialog;
    private Uri appRedirectSignin;
    private Uri appRedirectSignout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initCognito();

        if (this.auth.isAuthenticated()) {
            this.auth.getSession();
//            setAuthUserFragment(this.auth.);
        }
        else {
            setNewUserFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent activityIntent = getIntent();
        if (activityIntent.getData() != null) {

        }
        //  -- Call Auth.getTokens() to get Cognito JWT --
        if (activityIntent.getData() != null) {
            if (appRedirectSignin.getHost().equals(activityIntent.getData().getHost()) && appRedirectSignin.getPath().equals(activityIntent.getData().getPath())) {
                auth.getTokens(activityIntent.getData());
            }
        }
    }

    /**
     * Sets new user fragment on the screen.
     */
    private void setNewUserFragment() {
        UnauthUserFragment newUserFragment = new UnauthUserFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutContainer, newUserFragment);
        transaction.commit();




//        setScreenImages();
    }

    /**
     * Sets auth user fragment.
     *
     * @param session {@link AuthUserSession} containing tokens for a user.
     */
    private void setAuthUserFragment(AuthUserSession session) {
        AuthUserFragment userFragment = new AuthUserFragment();

        Bundle fragArgs = new Bundle();
        fragArgs.putString(getString(R.string.app_access_token), session.getAccessToken().getJWTToken());
        fragArgs.putString(getString(R.string.app_id_token), session.getIdToken().getJWTToken());
        userFragment.setArguments(fragArgs);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutContainer, userFragment);
        transaction.commit();

    }

    /**
     * Handles button press.
     *
     * @param signIn When {@code True} this performs sign-in.
     */
    public void onButtonPress(boolean signIn) {
        Log.d(" -- ", "Button press: " + signIn);
        if (signIn) {
            this.auth.getSession();
        } else {
            this.auth.signOut();
        }
    }

    @Override
    public void showPopup(String title, String content) {
        showDialogMessage(title, content);
    }

    /**
     * Setup authentication with Cognito.
     */
    void initCognito() {
        //  -- Create an instance of Auth --
        Auth.Builder builder = new Auth.Builder().setAppClientId(getString(R.string.cognito_client_id))
                .setAppClientSecret(getString(R.string.cognito_client_secret))
                .setAppCognitoWebDomain(getString(R.string.cognito_web_domain))
                .setApplicationContext(getApplicationContext())
                .setAuthHandler(new callback())
                .setSignInRedirect(getString(R.string.app_redirect))
                .setSignOutRedirect(getString(R.string.app_sign_out_redirect)); //.setPersistenceEnabled(false);
        this.auth = builder.build();
        appRedirectSignin = Uri.parse(getString(R.string.app_redirect));
        appRedirectSignout = Uri.parse(getString(R.string.app_sign_out_redirect));
    }

    /**
     * Callback handler for Amazon Cognito.
     */
    class callback implements AuthHandler {

        @Override
        public void onSuccess(AuthUserSession authUserSession) {
            // Show tokens for the authenticated user
            setAuthUserFragment(authUserSession);
            auth.setUsername(authUserSession.getUsername());

//            Intent intent = new Intent(UserAccount.this, MainActivity.class);
//
//            UserAccount.this.startActivity(intent);
        }

        @Override
        public void onSignout() {
            // Back to new user screen.
            setNewUserFragment();
        }


        @Override
        public void onFailure(Exception e) {
            Log.e(TAG, "Failed to auth", e);
        }
    }

    /**
     * Show an popup dialog.
     *
     * @param title
     * @param body
     */
    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();

                } catch (Exception e) {
                    // Log failure
                    Log.e(TAG, "Dialog failure", e);
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }
}