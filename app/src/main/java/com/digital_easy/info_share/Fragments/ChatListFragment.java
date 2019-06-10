package com.digital_easy.info_share.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.ChatListAdapter;
import com.digital_easy.info_share.Modules.CommentsListResponse;
import com.digital_easy.info_share.Modules.CommentsModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.Util;

import retrofit2.Call;
import retrofit2.Response;

public class ChatListFragment extends BaseFragment implements ChatListAdapter.Chat_Sel_interface {
    View rootview;
    RecyclerView recycle_chat_list;
    String set_name = "";
    String set_id = "";
    String card_created_by;
    String Card_Id = "";
    String userId;
    String channel_name = "";
    String comments = "";
    TextView txtNoComts_available;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.frag_chat_list, container, false);
        recycle_chat_list = rootview.findViewById(R.id.recycle_chat_list);
        recycle_chat_list.setLayoutManager(new LinearLayoutManager(getContext()));
        txtNoComts_available=rootview.findViewById(R.id.txtNoComts_available);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Card_Id = bundle.getString("card_id");
            set_name = bundle.getString("set_name");
            card_created_by = bundle.getString("card_crtd_by");
            set_id = bundle.getString("set_id");
            userId = bundle.getString("userId");
            channel_name = bundle.getString("channel_name");
            boolean isOwnUser = bundle.getBoolean("isOwnCard");

        }

        getCommentList();
        return rootview;
    }

    private void setAddSetCredentials(CommentsListResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {

            ChatListAdapter adapter = new ChatListAdapter(getContext(), addMessageResponse.getData(), this);
            recycle_chat_list.setAdapter(adapter);
        } else {
            showLongToast(getActivity(), message);
        }
    }

    private void getCommentList() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<CommentsListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_comment_list(Card_Id, userId);
                callRegisterUser.enqueue(new ApiCallback<CommentsListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<CommentsListResponse> response, boolean isSuccess, String message) {
                        CommentsListResponse addMessageResponse = response.body();
                        dismissProgress();
                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                if(addMessageResponse.getData()!=null && addMessageResponse.getData().size()!=0) {
                                    setAddSetCredentials(addMessageResponse);
                                    txtNoComts_available.setVisibility(View.GONE);
                                    recycle_chat_list.setVisibility(View.VISIBLE);
                                }
                                else {
                                    txtNoComts_available.setVisibility(View.VISIBLE);
                                    recycle_chat_list.setVisibility(View.GONE);
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


    @Override
    public void onSelect(int position, CommentsModel modelObj) {
        Bundle bundle_comnt = new Bundle();


        bundle_comnt.putString("card_id", Card_Id);
        bundle_comnt.putString("set_name", set_name);
        bundle_comnt.putString("set_id", set_id);
        bundle_comnt.putString("userId", userId);
        bundle_comnt.putString("coment_crtd_by", modelObj.created_by);
        bundle_comnt.putString("card_crtd_by", card_created_by);
        bundle_comnt.putString("channel_name", channel_name);


        Fragment cmt_frag = new CommentsFragment();
        cmt_frag.setArguments(bundle_comnt);
        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Comments, cmt_frag, true);

    }
}
