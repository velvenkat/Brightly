package com.purplefront.brightly.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BaseActivity;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends BaseFragment {

    View rootView;
    Context context;

    EditText edit_comments;
    Button submitBtn;

    String set_name = "";
    String set_id = "";
    String userId;
    String channel_name = "";
    String comments = "";


    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_comments, container, false);
//        userId = ((BrightlyNavigationActivity) getActivity()).userId;
        setHasOptionsMenu(true);

        edit_comments = (EditText) rootView.findViewById(R.id.edit_comments);
        clear_edit_text_focus(edit_comments);

        submitBtn = (Button) rootView.findViewById(R.id.submitBtn);

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

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comments = edit_comments.getText().toString();
                if(!comments.equals("")) {
                    getSetComment();
                }
                else
                {
                    showLongToast(getActivity(), "Empty comments cannot be sent");
                }

            }
        });

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



    private void getSetComment() {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getSetComments(userId, set_id, comments);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setAddSetCredentials(addMessageResponse);
                                dismissProgress();

                            } else {
                                dismissProgress();

                            }

                        } else {

                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                    }
                });
            } else {

                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }
    }


    private void setAddSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {

            ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
        } else {
            showLongToast(getActivity(), message);
        }
    }

}
