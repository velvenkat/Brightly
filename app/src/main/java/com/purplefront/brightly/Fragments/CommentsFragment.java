package com.purplefront.brightly.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.CommentsAdapter;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CommentsListResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends BaseFragment implements CommentsAdapter.Comments_Sel_interface {

    View rootView;
    Context context;

    EditText edit_comments;
    //ImageView send_img;

    String set_name = "";
    String set_id="";
    String Card_Id = "";
    String userId;
    String channel_name = "";
    String comments = "";
    RecyclerView recycler_comnt_list;
    RelativeLayout comnt_ip_contr;

    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false);
//        userId = ((BrightlyNavigationActivity) getActivity()).userId;
        setHasOptionsMenu(true);

        comnt_ip_contr = (RelativeLayout) rootView.findViewById(R.id.rl_comnt_contr);
        edit_comments = (EditText) rootView.findViewById(R.id.edit_comments);
        recycler_comnt_list = (RecyclerView) rootView.findViewById(R.id.recycle_comnt_list);
        clear_edit_text_focus(edit_comments);

        recycler_comnt_list.setLayoutManager(new LinearLayoutManager(getContext()));


        //send_img = (ImageView) rootView.findViewById(R.id.img_send);
        edit_comments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edit_comments.getRight() - edit_comments.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        comments = edit_comments.getText().toString();
                        if (!comments.equals("")) {
                            edit_comments.setText("");
                            getCardComment();
                        } else {

                            new CustomToast().Show_Toast(getActivity(), rootView, "Comments should not be empty");
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            Card_Id = bundle.getString("card_id");
            set_name = bundle.getString("set_name");
            set_id=bundle.getString("set_id");
            userId = bundle.getString("userId");
            channel_name = bundle.getString("channel_name");
            boolean isOwnUser = bundle.getBoolean("isOwnCard");
            if (isOwnUser) {
                comnt_ip_contr.setVisibility(View.GONE);
            }
        }

        getCommentList();

        //setTitle(set_name);
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(channel_name);
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);


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

    private void getCommentList() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<CommentsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_comment_list(Card_Id,userId,set_id);
                callRegisterUser.enqueue(new ApiCallback<CommentsListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<CommentsListResponse> response, boolean isSuccess, String message) {
                        CommentsListResponse addMessageResponse = response.body();
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

    private void getCardComment() {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getCardComments(userId, Card_Id, comments);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {


                                dismissProgress();
                                if (addMessageResponse.getMessage().equalsIgnoreCase("success")) {
                                    getCommentList();
                                }

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


    private void setAddSetCredentials(CommentsListResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {

            CommentsAdapter adapter = new CommentsAdapter(getContext(), addMessageResponse.getData(), this);
            recycler_comnt_list.setAdapter(adapter);
        } else {
            showLongToast(getActivity(), message);
        }
    }

    @Override
    public void onSelect(int position, CardsListModel modelObj) {

    }
}
