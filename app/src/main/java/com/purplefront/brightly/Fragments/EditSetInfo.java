package com.purplefront.brightly.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.SharedListAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
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

public class EditSetInfo extends BaseFragment implements SharedListAdapter.SharedListInterface {

    RecyclerView shared_listview;
    SharedListAdapter sharedListAdapter;
    EditText edit_setName;
    EditText edit_setDescription;
    Button btn_editSet;
    TextView text_share_title;

   // ImageView share, delete;

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
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;
    NotificationsModel notificationsModel;
    View rootView;


    RealmModel user_obj;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if (Created_By.equalsIgnoreCase(user_obj.getUser_Id())) {
          inflater.inflate(R.menu.edit_set_menu,menu);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootView=inflater.inflate(R.layout.activity_edit_set_info,container,false);
         user_obj=((BrightlyNavigationActivity)getActivity()).getUserModel();
         setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Edit Set Info");
*/
        Bundle bundle =getArguments();
        isNotification = bundle.getBoolean("isNotification", false);

        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            channel_id = notificationsModel.getChannel_id();
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            Created_By = notificationsModel.getNotificationsSetDetail().getCreated_by();
            shared_by = notificationsModel.getNotificationsSetDetail().getShared_by();

        } else {

            chl_list_obj = bundle.getParcelable("model_obj");
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

        }

        edit_setName = (EditText) rootView.findViewById(R.id.edit_setName);
        edit_setDescription = (EditText) rootView.findViewById(R.id.edit_setDescription);
        text_share_title = (TextView) rootView.findViewById(R.id.text_share_title);
        btn_editSet = (Button)rootView.findViewById(R.id.btn_editSet);


        shared_listview = (RecyclerView) rootView.findViewById(R.id.shared_listview);

        edit_setName.setText(set_name);
        edit_setDescription.setText(set_description);
        if (!Created_By.equalsIgnoreCase(user_obj.getUser_Id())) {

            btn_editSet.setVisibility(View.GONE);
            edit_setName.setEnabled(false);
            edit_setDescription.setEnabled(false);
            if(shared_by != null) {
                text_share_title.setVisibility(View.VISIBLE);
                text_share_title.setText("Shared by : " + shared_by);
            }
         //   setTitle("Set Info");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Set Info");

          /*  shared_listview.setLayoutManager(new LinearLayoutManager(this));
            sharedListAdapter = new SharedListAdapter(EditSetInfo.this, sharedDataModels, set_id);
            shared_listview.setAdapter(sharedListAdapter);*/

        } else {


            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Set Info");
            getSetSharedInfo();


        }
        btn_editSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();
                    ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
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
        if (set_name.equals("") || set_name.length() == 0
                || set_description.equals("") || set_description.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), edit_setName,
                    "Both fields are required.");
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
        alertDialog.setMessage("You are about to delete the Set. All the information contained in the Sets will be lost. ");

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
                Fragment fragment =new SharePage();
                Bundle bundle1=new Bundle();
                bundle1.putParcelable("model_obj", chl_list_obj);
                bundle1.putParcelable("setsListModel", setsListModel);
                fragment.setArguments(bundle1);
                ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.share_page,fragment,true);
                return true;
            case R.id.action_delete:
                showAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getRevokeSet(String set_id, String assigned_to) {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
//                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getRevokeSet(set_id, assigned_to);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setRevokeSetCredentials(addMessageResponse);
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


    private void setRevokeSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();


        if (message.equals("success")) {
            getSetSharedInfo();
            showLongToast(getActivity(), addMessageResponse.getMessage());
            dismissProgress();
        } else {
            showLongToast(getActivity(), message);
            dismissProgress();
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
                        AddMessageResponse deleteSetResponse = response.body();
                        if (isSuccess) {

                            if (deleteSetResponse != null) {

                                setDeleteCredentials(deleteSetResponse);
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

            Fragment fragment=new SetsFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("model_obj", chl_list_obj);
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.Set_List,fragment,false);



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
            Fragment fragment=new CardDetailFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("model_obj", chl_list_obj);
            bundle.putParcelable("setsListModel", setsListModel);
            fragment.setArguments(bundle);
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.view_card,fragment,false);
//            ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
        } else {
            dismissProgress();
            showLongToast(getActivity(), message);

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
                        SetInfoSharedResponse infoSharedResponse = response.body();
                        if (isSuccess) {

                            if (infoSharedResponse != null) {

                                setSharedCredentials(infoSharedResponse);
                                dismissProgress();

                            } else {
                                dismissProgress();
                                text_share_title.setVisibility(View.GONE);

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

    private void setSharedCredentials(SetInfoSharedResponse infoSharedResponse) {

        if (!setsListModel.getShared_data().isEmpty() && setsListModel.getShared_data() != null) {
            text_share_title.setVisibility(View.VISIBLE);
            shared_listview.setLayoutManager(new LinearLayoutManager(getContext()));
            sharedListAdapter = new SharedListAdapter(getContext(), infoSharedResponse.getShared_data(), set_id,this);
            shared_listview.setAdapter(sharedListAdapter);
            dismissProgress();
        } else {
            dismissProgress();
        }
    }

    @Override
    public void call_revoke(String set_id, String assigned_to) {
        getRevokeSet(set_id,assigned_to);
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/
}
