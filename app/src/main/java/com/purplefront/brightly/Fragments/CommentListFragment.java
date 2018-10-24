package com.purplefront.brightly.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentListFragment extends BaseFragment {

    View rootView;
    Context context;
    String set_name = "";
    String set_id = "";
    String userId;
    String channel_name = "";


    public CommentListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false);

        //        userId = ((BrightlyNavigationActivity) getActivity()).userId;
        setHasOptionsMenu(true);


        Bundle bundle = getArguments();
        if (bundle != null) {
            set_id = bundle.getString("set_id");
            set_name = bundle.getString("set_name");
            userId = bundle.getString("userId");
            channel_name = bundle.getString("channel_name");

        }

        //setTitle(set_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(channel_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

}
