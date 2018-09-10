package com.purplefront.brightly.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import retrofit2.Call;
import retrofit2.Response;

public class CreateSet extends BaseActivity {

    EditText create_setName;
    EditText create_setDescription;
    Button btn_createSet;

    String userId;
    String channel_id;
    String set_name = "";
    String set_description = "";
    String channel_name;
    ChannelListModel chl_list_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Create Set");

        userId = getIntent().getStringExtra("userId");
        chl_list_obj=getIntent().getParcelableExtra("model_obj");
        channel_id=chl_list_obj.getChannel_id();
        channel_name=chl_list_obj.getChannel_name();

        create_setName = (EditText) findViewById(R.id.create_setName);
        create_setDescription = (EditText) findViewById(R.id.create_setDescription);
        btn_createSet = (Button) findViewById(R.id.btn_createSet);

        btn_createSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {

        // Get all edittext texts
        set_name = create_setName.getText().toString();
        set_description = create_setDescription.getText().toString();

        // Check if all strings are null or not
        if (set_name.equals("") || set_name.length() == 0
                || set_description.equals("") || set_description.length() == 0) {

            new CustomToast().Show_Toast(CreateSet.this, create_setName,
                    "Both fields are required.");
        }

        // Else do signup or do your stuff
        else {
            getAddSet();
        }
    }

    private void getAddSet() {

        try {

            if (CheckNetworkConnection.isOnline(CreateSet.this)) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(CreateSet.this).getAddSet(userId, channel_id, set_name, set_description);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(CreateSet.this) {
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

        if(message.equals("success"))
        {
            Intent intent = new Intent(CreateSet.this, MyChannelsSet.class);
            intent.putExtra("model_obj", chl_list_obj);
            startActivity(intent);
            onBackPressed();
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        }
        else {
            showLongToast(CreateSet.this, message);
        }
    }

    @Override
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }


}
