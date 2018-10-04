package com.purplefront.brightly.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;

public class CreateSet extends BaseFragment {

    EditText create_setName;
    EditText create_setDescription;
    Button btn_createSet;

    String userId;
    String channel_id;
    String set_name = "";
    String set_description = "";
    String channel_name;
    ChannelListModel chl_list_obj;
    View rootView;

    RealmModel user_obj;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_create_set, container, false);

        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Create Set");
*/
        Bundle bundle = getArguments();
        userId = user_obj.getUser_Id();
        chl_list_obj = bundle.getParcelable("model_obj");
        channel_id = chl_list_obj.getChannel_id();
        channel_name = chl_list_obj.getChannel_name();

        create_setName = (EditText) rootView.findViewById(R.id.create_setName);
        create_setDescription = (EditText) rootView.findViewById(R.id.create_setDescription);
        btn_createSet = (Button) rootView.findViewById(R.id.btn_createSet);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Set");
        btn_createSet.setOnClickListener(new View.OnClickListener() {
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

    private void checkValidation() {

        // Get all edittext texts
        set_name = create_setName.getText().toString();
        set_description = create_setDescription.getText().toString();

        // Check if all strings are null or not
        if (set_name.equals("") || set_name.length() == 0
                || set_description.equals("") || set_description.length() == 0) {

            new CustomToast().Show_Toast(getContext(), create_setName,
                    "Both fields are required.");
        }

        // Else do signup or do your stuff
        else {
            getAddSet();
        }
    }

    private void getAddSet() {

        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getAddSet(userId, channel_id, set_name, set_description);
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
           /* Intent intent = new Intent(CreateSet.this, com.purplefront.brightly.Fragments.SetsFragment.class);
            intent.putExtra("model_obj", chl_list_obj);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
         /*   Fragment fragment=new SetsFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("model_obj", chl_list_obj);
            ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.Set_List,fragment,false);*/
            ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
        } else {
            showLongToast(getActivity(), message);
        }
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


}
