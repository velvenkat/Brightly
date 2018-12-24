package com.purplefront.brightly.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.SharedListAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.SetInfoSharedResponse;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import retrofit2.Call;
import retrofit2.Response;


public class ShareSettings extends BaseFragment implements SharedListAdapter.SharedListInterface {

    View rootView;
    TextView txtCapShrdConts;
    RecyclerView recycle_shrd_conts_list;
    LinearLayout layout_act_inact;
    RealmModel user_obj;
    String set_id, channel_id, share_link;
    ChannelListModel chl_list_obj;
    SetsListModel set_list_obj;
    NotificationsModel notificationsModel;
    boolean isNotification;
    // SwitchCompat toggle_shareLink;
    boolean isShareSetChgd;
    String access_val = "";
    RadioGroup share_link_options;
    TextView txtShareLinkAccess;
    RadioButton radio_private, radio_public;
    String shared_by, share_access, Created_By, set_name;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share_setting_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_share) {
            Fragment fragment = new SharePage();
            Bundle bundle1 = new Bundle();
            if (isNotification) {
                bundle1.putBoolean("isNotification", true);
                bundle1.putParcelable("notfy_modl_obj", notificationsModel);
            } else {
                bundle1.putBoolean("isNotification", false);
                bundle1.putParcelable("model_obj", chl_list_obj);
                bundle1.putParcelable("setsListModel", set_list_obj);
            }
            fragment.setArguments(bundle1);
            ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
            ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.share_page, fragment, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.lo_shr_settings, container, false);
        txtCapShrdConts = (TextView) rootView.findViewById(R.id.txtCapShrdConts);
        share_link_options = (RadioGroup) rootView.findViewById(R.id.share_link_options);
        recycle_shrd_conts_list = (RecyclerView) rootView.findViewById(R.id.recycle_shrd_conts_list);
        layout_act_inact = (LinearLayout) rootView.findViewById(R.id.layout_act_inact);
        radio_private = (RadioButton) rootView.findViewById(R.id.radio_private);
        radio_public = (RadioButton) rootView.findViewById(R.id.radio_public);
        txtShareLinkAccess = (TextView) rootView.findViewById(R.id.txtShareLink);
        //toggle_shareLink = (SwitchCompat) rootView.findViewById(R.id.switch_share_link);
        Bundle bundle = getArguments();
        isNotification = bundle.getBoolean("isNotification", false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        setHasOptionsMenu(true);
        ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = true;
        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            channel_id = notificationsModel.getChannel_id();
        /*    set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();*/
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            Created_By = notificationsModel.getNotificationsSetDetail().getCreated_by();
            shared_by = notificationsModel.getNotificationsSetDetail().getShared_by();
            share_access = notificationsModel.getNotificationsSetDetail().getShare_access();
            share_link = notificationsModel.getNotificationsSetDetail().getShare_link();

            share_link_options.setVisibility(View.GONE);
            //txtShareLinkAccess.setVisibility(View.GONE);
            if (notificationsModel.getNotificationsSetDetail().getWeb_sharing() == null) {
                //txtShareLinkAccess.setText("Your share settings are set to private ");
              //  Toast.makeText(getContext(), "getting null", Toast.LENGTH_LONG).show();
                notificationsModel.getNotificationsSetDetail().setWeb_sharing("0");
                CardDetailFragment parent_frag = (CardDetailFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Util.view_card);
                parent_frag.notificationsModel = notificationsModel;
            }
            if (notificationsModel.getNotificationsSetDetail().getWeb_sharing().equalsIgnoreCase("0")) {
                //radio_private.setChecked(true);
                txtShareLinkAccess.setText("Your share settings are set to private ");
            } else
                //radio_public.setChecked(true);
                txtShareLinkAccess.setText("Your share settings are set to public ");
            // txtShareLinkAccess.setText("");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);

        } else {

            chl_list_obj = bundle.getParcelable("model_obj");
            channel_id = chl_list_obj.getChannel_id();
            //  channel_name = chl_list_obj.getChannel_name();
            Created_By = chl_list_obj.getCreated_by();
            //userId = bundle.getString("userId");
            set_list_obj = bundle.getParcelable("setsListModel");
            set_name = set_list_obj.getSet_name();
            set_id = set_list_obj.getSet_id();
            share_link = set_list_obj.getShare_link();
            shared_by = set_list_obj.getShared_by();
            share_access = set_list_obj.getShare_access();
            if (set_list_obj.getWeb_sharing().equalsIgnoreCase("0")) {

                if (!Created_By.equals(user_obj.getUser_Id())) {
                    share_link_options.setVisibility(View.GONE);
                    //txtShareLinkAccess.setVisibility(View.GONE);
                    txtShareLinkAccess.setText("Your share settings are set to private ");
                }
                radio_private.setChecked(true);

            } else {

                if (!Created_By.equals(user_obj.getUser_Id())) {
                    share_link_options.setVisibility(View.GONE);
                    //txtShareLinkAccess.setVisibility(View.GONE);
                    txtShareLinkAccess.setText("Your share settings are set to public ");
                }
                radio_public.setChecked(true);
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);

        }


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share Settings");

        getSetSharedInfo();

        share_link_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                   /* if (isChecked) {
                        access_val = "1";
                    } else
                        access_val = "0";*/
                switch (checkedId) {
                    case R.id.radio_private:
                        access_val = "0";
                        break;
                    case R.id.radio_public:
                        access_val = "1";
                        break;
                }
                try {

                    if (CheckNetworkConnection.isOnline(getContext())) {
                        showProgress();
                        Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).setToggleShareLink(set_id, access_val);
                        callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                            @Override
                            public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                                dismissProgress();
                                AddMessageResponse addMessageResponse = response.body();
                                if (isSuccess) {

                                    dismissProgress();
                                    Toast.makeText(getContext(), addMessageResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    if (!isNotification) {
                                        set_list_obj.setWeb_sharing(access_val);
                                        CardDetailFragment parent_frag = (CardDetailFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Util.view_card);
                                        parent_frag.setsListModel = set_list_obj;
                                    } else {
                                        notificationsModel.getNotificationsSetDetail().setWeb_sharing(access_val);
                                        CardDetailFragment parent_frag = (CardDetailFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Util.view_card);
                                        parent_frag.notificationsModel = notificationsModel;
                                    }


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


    public void getRevokeSet(String set_id, String assigned_to, String name) {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getRevokeSet(set_id, assigned_to, user_obj.getUser_Id());
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            dismissProgress();
                            if (addMessageResponse != null) {

                                setRevokeSetCredentials(addMessageResponse, name);


                            }

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
    public void call_revoke(String set_id, String assigned_to, String name) {
        getRevokeSet(set_id, assigned_to, name);
    }


    private void setRevokeSetCredentials(AddMessageResponse addMessageResponse, String name) {


        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {
            showLongToast(getActivity(), "Set Permission has been Revoked for " + name);
            getSetSharedInfo();
            dismissProgress();
        } else {
            showLongToast(getActivity(), message);
            dismissProgress();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = true;
            if (isShareSetChgd) {
                isShareSetChgd = false;
                getSetSharedInfo();
            }
            if (!Created_By.equalsIgnoreCase(user_obj.getUser_Id())) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share Settings");
            } else
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share Settings");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);
        }
    }

    public void getSetSharedInfo() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<SetInfoSharedResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getSetSharedInfo(user_obj.getUser_Id(), channel_id, set_id);
                callRegisterUser.enqueue(new ApiCallback<SetInfoSharedResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SetInfoSharedResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        SetInfoSharedResponse infoSharedResponse = response.body();
                        if (isSuccess) {

                            if (infoSharedResponse != null && infoSharedResponse.getShared_data().size() != 0) {

                                setSharedCredentials(infoSharedResponse);
                                //   txtCapShrdConts.setVisibility(View.VISIBLE);

                                //  shared_listview.setVisibility(View.VISIBLE);


                            } else {

//                                text_share_title.setVisibility(View.GONE);
                                txtCapShrdConts.setVisibility(View.GONE);
                                layout_act_inact.setVisibility(View.GONE);
                                recycle_shrd_conts_list.setVisibility(View.GONE);

                            }

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
            dismissProgress();
            e.printStackTrace();


        }

    }

    public void api_call_share_access_update(String value, String assigned_to, String name) {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).call_share_access_update(user_obj.getUser_Id(), set_id, value, assigned_to);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse infoSharedResponse = response.body();
                        if (isSuccess) {

                            dismissProgress();

                            if (infoSharedResponse != null) {

                                if (value.equals("0")) {
                                    showShortToast(getActivity(), name + " can only View the Set ");
                                }

                                if (value.equals("1")) {
                                    showShortToast(getActivity(), name + " can Share the Set");
                                }

                            }
                            if (isNotification) {
                                notificationsModel.getNotificationsSetDetail().setShare_access(value);

                            } else {
                                set_list_obj.setShare_access(value);
                            }

                            CardDetailFragment Card_dtl_frag = (CardDetailFragment) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.view_card);

                            Card_dtl_frag.setsListModel = set_list_obj;
                            Card_dtl_frag.notificationsModel = notificationsModel;
                            getSetSharedInfo();
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

    private void setSharedCredentials(SetInfoSharedResponse infoSharedResponse) {

        if (!infoSharedResponse.getShared_data().isEmpty() && infoSharedResponse.getShared_data() != null) {
            layout_act_inact.setVisibility(View.VISIBLE);
            txtCapShrdConts.setVisibility(View.VISIBLE);
            recycle_shrd_conts_list.setVisibility(View.VISIBLE);
            recycle_shrd_conts_list.setLayoutManager(new LinearLayoutManager(getContext()));
            SharedListAdapter sharedListAdapter = new SharedListAdapter(getContext(), infoSharedResponse.getShared_data(), set_id, share_link, this, ShareSettings.this);
            recycle_shrd_conts_list.setAdapter(sharedListAdapter);
        }
    }


}
