package com.digital_easy.info_share.Fragments;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.SharedListAdapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.CustomToast;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.Util;

import retrofit2.Call;
import retrofit2.Response;

public class EditSetInfo extends BaseFragment {


    SharedListAdapter sharedListAdapter;
    EditText edit_setName;
    EditText edit_setDescription;
    Button btn_editSet;
    TextView text_share_title;
    LinearLayout layout_act_inact;
    RadioGroup share_link_options;


    // ImageView share, delete;

    public boolean isShareSetChgd = false;

    boolean isNotification;
    //  String userId;
    String channel_id = "";
    String set_description = "";
    String set_name = "";
    String set_id = "";
    String share_link;
    String channel_name;
    String Created_By;
    String shared_by;
    String share_access;
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;
    NotificationsModel notificationsModel;
    View rootView;
    String access_val;
    RelativeLayout toggle_contr;

    RealmModel user_obj;
    String level2_title_singular;
    boolean isCardDtlPage;


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        if (Created_By.equalsIgnoreCase(user_obj.getUser_Id())) {
            inflater.inflate(R.menu.edit_set_menu, menu);

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_edit_set_info, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        share_link_options = (RadioGroup) rootView.findViewById(R.id.share_link_options);
        /*toggle_contr = (RelativeLayout) rootView.findViewById(R.id.toggle_contr);
        Switch toggle_share_access = rootView.findViewById(R.id.switch_toggle_access);*/
        setHasOptionsMenu(true);
        ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = true;
//        toolbar=(Toolbar)rootView.findViewById(R.id.toolbar);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        level2_title_singular = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel2title().getSingular();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Edit Set Info");
*/
        Bundle bundle = getArguments();
        if (bundle != null) {
            isNotification = bundle.getBoolean("isNotification", false);
            isCardDtlPage = bundle.getBoolean("isCardDtlPage", false);
        }

        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            channel_id = notificationsModel.getChannel_id();
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            Created_By = notificationsModel.getNotificationsSetDetail().getCreated_by();
            shared_by = notificationsModel.getNotificationsSetDetail().getShared_by();
            share_access = notificationsModel.getNotificationsSetDetail().getShare_access();
            share_link = notificationsModel.getNotificationsSetDetail().getShare_link();

        } else {

            //chl_list_obj = bundle.getParcelable("model_obj");
            chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
            channel_id = chl_list_obj.getChannel_id();
            channel_name = chl_list_obj.getChannel_name();
            Created_By = chl_list_obj.getCreated_by();
            //userId = bundle.getString("userId");
            setsListModel = bundle.getParcelable("setsListModel");
            set_description = setsListModel.getDescription();
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();
            share_link = setsListModel.getShare_link();
            shared_by = setsListModel.getShared_by();
            share_access = setsListModel.getShare_access();

        }


      /*  //  switchAB.setChecked(false);
        if (Created_By.equalsIgnoreCase(user_obj.getUser_Id())) {
            toggle_contr.setVisibility(View.VISIBLE);
            toggle_share_access.setShowText(true);
        } else {
            toggle_contr.setVisibility(View.GONE);
        }
        if (share_access.equals("0")) {
            toggle_share_access.setChecked(false);

        } else {
            toggle_share_access.setChecked(true);
        }
        toggle_share_access.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                   *//* Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT)
                            .show();*//*
                    //menu.getItem()
                    //share_item.setVisible(true);

                    api_call_share_access_update("1");
                } else {
                    //share_item.setVisible(false);
                    api_call_share_access_update("0");
                }


            }
        });*/


        edit_setName = (EditText) rootView.findViewById(R.id.edit_setName);
        edit_setDescription = (EditText) rootView.findViewById(R.id.edit_setDescription);
        text_share_title = (TextView) rootView.findViewById(R.id.text_share_title);
        btn_editSet = (Button) rootView.findViewById(R.id.btn_editSet);

        layout_act_inact = (LinearLayout) rootView.findViewById(R.id.layout_act_inact);


        edit_setName.setText(set_name);
        edit_setDescription.setText(set_description);
        edit_setDescription.setHint(level2_title_singular + " Description");
        edit_setName.setHint(level2_title_singular + " Name");
        btn_editSet.setText("Edit " + level2_title_singular);
        clear_edit_text_focus(edit_setName);
        clear_edit_text_focus(edit_setDescription);

        if (!Created_By.equalsIgnoreCase(user_obj.getUser_Id())) {

            btn_editSet.setVisibility(View.GONE);
            share_link_options.setVisibility(View.GONE);
            edit_setName.setEnabled(false);
            edit_setDescription.setEnabled(false);
            //   setTitle("Set Info");
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(level2_title_singular + " Info");


          /*  shared_listview.setLayoutManager(new LinearLayoutManager(this));
            sharedListAdapter = new SharedListAdapter(EditSetInfo.this, sharedDataModels, set_id);
            shared_listview.setAdapter(sharedListAdapter);*/

        } else {

            if (setsListModel.getWeb_sharing().equalsIgnoreCase("1")) {


                share_link_options.check(R.id.radio_yes);

            } else {


                share_link_options.check(R.id.radio_no);
            }
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Edit " + level2_title_singular + " Info");

        }


        if (shared_by != null && !shared_by.equals("me")) {
            text_share_title.setVisibility(View.VISIBLE);
            text_share_title.setText("Shared by : " + shared_by);
        }
        btn_editSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        share_link_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                   /* if (isChecked) {
                        access_val = "1";
                    } else
                        access_val = "0";*/
                switch (checkedId) {
                    case R.id.radio_no:
                        access_val = "0";
                        break;
                    case R.id.radio_yes:
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
                                        setsListModel.setWeb_sharing(access_val);
                                        CardDetailFragment parent_frag = (CardDetailFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Util.view_card);
                                        parent_frag.setsListModel = setsListModel;
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


    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        set_name = edit_setName.getText().toString();
        set_description = edit_setDescription.getText().toString();


        // Check if all strings are null or not
        if (set_name.trim().equals("") || set_name.trim().length() == 0
        ) {

            new CustomToast().Show_Toast(getActivity(), edit_setName,
                    level2_title_singular + " name is required");
        }

        // Else do signup or do your stuff
        else {
            getUpdateSet();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete....");

        // Setting Dialog Message
        alertDialog.setMessage("You are about to delete the " + level2_title_singular + ". All the information contained in the " + level2_title_singular + " will be lost. ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                getDeleteSet();
                // Write your code here to invoke YES event
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

/*

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = true;
            if (isShareSetChgd) {
                //  getSetSharedInfo();
            }
            if (!Created_By.equalsIgnoreCase(user_obj.getUser_Id())) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Set Info");
            } else
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Set Info");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(set_name);
        }
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          /*  case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.

                finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;
*/
            case R.id.action_share:

               /* Fragment fragment = new SharePage();
                Bundle bundle1 = new Bundle();
                if (isNotification) {
                    bundle1.putBoolean("isNotification", true);
                    bundle1.putParcelable("notfy_modl_obj", notificationsModel);
                } else {
                    bundle1.putBoolean("isNotification", false);
                    bundle1.putParcelable("model_obj", chl_list_obj);
                    bundle1.putParcelable("setsListModel", setsListModel);
                }
                fragment.setArguments(bundle1);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.share_page, fragment, true);*/
                return true;
            case R.id.action_delete:
                showAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void getDeleteSet() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getDeleteSet(set_id);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        dismissProgress();
                        AddMessageResponse deleteSetResponse = response.body();
                        if (isSuccess) {

                            if (deleteSetResponse != null) {
                                setDeleteCredentials(deleteSetResponse);


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

    private void setDeleteCredentials(AddMessageResponse deleteSetResponse) {

        String message = deleteSetResponse.getMessage();

        if (message.equals("success")) {
          /*  Intent intent = new Intent(EditSetInfo.this, MyChannelsSet.class);
            intent.putExtra("model_obj", chl_list_obj);
            intent.putExtra("userId", userId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            onBackPressed();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/

          /*  Fragment fragment=new SetsFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("model_obj", chl_list_obj);
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.Set_List,fragment,false);*/
            showShortToast(getActivity(), level2_title_singular + " " + set_name + " is deleted");
            //   ((BrightlyNavigationActivity)getActivity()).Don
            if (isCardDtlPage)
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true, 2);
            else
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);


        } else {
            showLongToast(getActivity(), message);
        }
    }

    public void getUpdateSet() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getUpdateSet(user_obj.getUser_Id(), channel_id, set_name, set_description, set_id);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse updateSetResponse = response.body();
                        if (isSuccess) {

                            if (updateSetResponse != null) {

                                setUpdateCredentials(updateSetResponse);
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

    private void setUpdateCredentials(AddMessageResponse updateSetResponse) {

        String message = updateSetResponse.getMessage();

        if (message.equals("success")) {
            dismissProgress();
            /*   Intent intent = new Intent(EditSetInfo.this, MyChannelsSet.class);
            intent.putExtra("model_obj", chl_list_obj);
            intent.putExtra("userId", userId);
//            intent.putExtra("setsListModel", setsListModel);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
            /*Fragment fragment=new CardDetailFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("model_obj", chl_list_obj);
            bundle.putParcelable("setsListModel", setsListModel);
            fragment.setArguments(bundle);
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.view_card,fragment,false);*/
            if (isNotification) {
                notificationsModel.getNotificationsSetDetail().setName(set_name);
                notificationsModel.getNotificationsSetDetail().setDescription(set_description);
            } else {
                setsListModel.setSet_name(set_name);
                setsListModel.setDescription(set_description);
            }
            CardDetailFragment Card_dtl_frag = (CardDetailFragment) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.view_card);
            if (Card_dtl_frag != null) {
                Card_dtl_frag.chl_list_obj = chl_list_obj;
                Card_dtl_frag.setsListModel = setsListModel;
                Card_dtl_frag.notificationsModel = notificationsModel;
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
            } else
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);

        } else {
            dismissProgress();
//            showLongToast(getActivity(), message);
            if (message.equals("Unable to Update Set.")) {
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
            }

        }
    }

  /*  public void getSetSharedInfo() {
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
                                text_share_title.setVisibility(View.VISIBLE);
                                layout_act_inact.setVisibility(View.VISIBLE);
                                shared_listview.setVisibility(View.VISIBLE);


                            } else {

//                                text_share_title.setVisibility(View.GONE);
                                layout_act_inact.setVisibility(View.GONE);
                                shared_listview.setVisibility(View.GONE);

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

    }*/

   /* private void setSharedCredentials(SetInfoSharedResponse infoSharedResponse) {

        if (!infoSharedResponse.getShared_data().isEmpty() && infoSharedResponse.getShared_data() != null) {
            text_share_title.setVisibility(View.VISIBLE);
            shared_listview.setLayoutManager(new LinearLayoutManager(getContext()));
            sharedListAdapter = new SharedListAdapter(getContext(), infoSharedResponse.getShared_data(), set_id, share_link, this, EditSetInfo.this);
            shared_listview.setAdapter(sharedListAdapter);
        }
    }*/


   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/
}
