package com.example.eventracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.eventracker.R;


/**
 * Unauthenticated user.
 */
public class UnauthUserFragment extends Fragment {
    private static final boolean SIGN_IN = true;

    private OnFragmentInteractionListener mListener;

    private Button userButton;

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
    }
}