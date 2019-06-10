package com.digital_easy.info_share.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.CommentsAdapter;
import com.digital_easy.info_share.CustomToast;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.CardsListModel;
import com.digital_easy.info_share.Modules.CommentsListResponse;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends BaseFragment implements CommentsAdapter.Comments_Sel_interface, TextWatcher {

    View rootView;
    Context context;

    EditText edit_comments;
    //ImageView send_img;
    boolean isNotification;
    String set_name = "";
    String set_id = "";
    String Card_Id = "";
    String userId;
    String channel_name = "";
    String comments_str = "";
    RecyclerView recycler_comnt_list;
    RelativeLayout comnt_ip_contr;
    String coment_crtd_by = null;
    String card_crtd_by;
    TextView txtComnnt_left, no_comts_avail;
    ImageView img_send_comment;
    NotificationsModel nfy_mdl_obj;

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
        txtComnnt_left = (TextView) rootView.findViewById(R.id.comnts_left);
        img_send_comment = rootView.findViewById(R.id.send_comment);
        no_comts_avail = rootView.findViewById(R.id.no_comts_avail);
        edit_comments.addTextChangedListener(this);
        clear_edit_text_focus(edit_comments);

        recycler_comnt_list.setLayoutManager(new LinearLayoutManager(getContext()));

        img_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comments_str = edit_comments.getText().toString();
                if (!comments_str.equals("")) {
                    edit_comments.setText("");
                    getCardComment();
                } else {

                    new CustomToast().Show_Toast(getActivity(), rootView, "Comments should not be empty");
                }

            }
        });

        //send_img = (ImageView) rootView.findViewById(R.id.img_send);
    /*    edit_comments.setOnTouchListener(new View.OnTouchListener() {
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
        });*/

        Bundle bundle = getArguments();
        if (bundle != null) {
            isNotification = bundle.getBoolean("isNotification", false);
            if (isNotification) {
                nfy_mdl_obj = bundle.getParcelable("notfy_modl_obj");
                Card_Id = nfy_mdl_obj.getNotificationsSetDetail().getComment_card_id();//bundle.getString("card_id");
                // set_name = bundle.getString("set_name");
                set_id = nfy_mdl_obj.getNotificationsSetDetail().getSet_id();//bundle.getString("set_id");
                userId = bundle.getString("userId");
                coment_crtd_by = nfy_mdl_obj.getNotificationsSetDetail().getComment_created_by();//bundle.getString("coment_crtd_by");
                //channel_name = bundle.getString("channel_name");
                card_crtd_by = nfy_mdl_obj.getCard_created_by();
                boolean isOwnUser = bundle.getBoolean("isOwnCard");
                if (isOwnUser) {
                    comnt_ip_contr.setVisibility(View.GONE);
                }
            } else {
                Card_Id = bundle.getString("card_id");
                set_name = bundle.getString("set_name");
                set_id = bundle.getString("set_id");
                userId = bundle.getString("userId");
                coment_crtd_by = bundle.getString("coment_crtd_by");
                channel_name = bundle.getString("channel_name");
                card_crtd_by = bundle.getString("card_crtd_by");
                boolean isOwnUser = bundle.getBoolean("isOwnCard");
                if (isOwnUser) {
                    comnt_ip_contr.setVisibility(View.GONE);
                }
            }
        }

        getCommentList();

        //setTitle(set_name);
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Comment List");
        //  ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();
                    if (!isNotification)
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    else {
                        getActivity().finish();
                        simpleIntent(getActivity(), BrightlyNavigationActivity.class);
                    }
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    private void getCommentList() {
        // Toast.makeText(getContext(), "card_crtd_by " + card_crtd_by + " userId :" + userId + ",SetID:" + set_id + "CardId" + Card_Id + "Created" + coment_crtd_by, Toast.LENGTH_LONG).show();
        try {

            Call<CommentsListResponse> callRegisterUser;
            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                //Log.e(getContext(), , Toast.LENGTH_LONG).show();
                //  Crashlytics.log("Params:" + userId + "," + Card_Id + "," + coment_crtd_by);
                if (!card_crtd_by.equals(userId))
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_comment_list(Card_Id, userId);
                else
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_comment_list(Card_Id, coment_crtd_by);
                callRegisterUser.enqueue(new ApiCallback<CommentsListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<CommentsListResponse> response, boolean isSuccess, String message) {
                        CommentsListResponse addMessageResponse = response.body();
                        dismissProgress();
                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setAddSetCredentials(addMessageResponse);


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
                Call<AddMessageResponse> callRegisterUser;
                if (card_crtd_by.equals(userId)) {
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getCardComments(userId, Card_Id, set_id, comments_str, coment_crtd_by);
                } else
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getCardComments(userId, Card_Id, set_id, comments_str, card_crtd_by);
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

            if (addMessageResponse.getData() != null && addMessageResponse.getData().size() > 0) {
                if (addMessageResponse.getData().get(0).registered_status.equals("0")) {
                    comnt_ip_contr.setVisibility(View.GONE);
                }
                CommentsAdapter adapter = new CommentsAdapter(getContext(), addMessageResponse.getData(), this, userId);
                recycler_comnt_list.setAdapter(adapter);
                no_comts_avail.setVisibility(View.GONE);
                recycler_comnt_list.setVisibility(View.VISIBLE);

                recycler_comnt_list.scrollToPosition(addMessageResponse.getData().size() - 1);
            } else {
                no_comts_avail.setVisibility(View.VISIBLE);
                recycler_comnt_list.setVisibility(View.GONE);
                // comnt_ip_contr.setVisibility(View.GONE);


            }
        } else {
            showLongToast(getActivity(), message);
        }
    }

    @Override
    public void onSelect(int position, CardsListModel modelObj) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String comments = edit_comments.getText().toString();
        int comtd_lgth = comments.length();
        int remaining = 250 - comtd_lgth;
        if (comtd_lgth > 0) {
            txtComnnt_left.setText(remaining + " left");
        } else
            txtComnnt_left.setText("");
    }
}
