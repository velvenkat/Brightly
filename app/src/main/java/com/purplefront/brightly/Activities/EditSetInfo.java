package com.purplefront.brightly.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import retrofit2.Call;
import retrofit2.Response;

public class EditSetInfo extends BaseActivity {

    EditText edit_setName;
    EditText edit_setDescription;
    Button btn_editSet;

    ImageView share, delete;

    String userId;
    String channel_id = "";
    String set_description = "";
    String set_name = "";
    String set_id = "";
    String share_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Edit Set Info");

        channel_id = getIntent().getStringExtra("channel_id");
        set_description = getIntent().getStringExtra("set_description");
        set_name = getIntent().getStringExtra("set_name");
        set_id = getIntent().getStringExtra("set_id");
        userId = getIntent().getStringExtra("userId");
        share_link = getIntent().getStringExtra("share_link");


        edit_setName = (EditText) findViewById(R.id.edit_setName);
        edit_setDescription = (EditText) findViewById(R.id.edit_setDescription);
        btn_editSet = (Button) findViewById(R.id.btn_editSet);

        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);

        edit_setName.setText(set_name);
        edit_setDescription.setText(set_description);

        btn_editSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditSetInfo.this, ShareWithContacts.class);
                intent.putExtra("set_id", set_id);
                intent.putExtra("userId", userId);
                intent.putExtra("share_link", share_link);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
            }
        });
    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        set_name = edit_setName.getText().toString();
        set_description = edit_setDescription.getText().toString();

        // Check if all strings are null or not
        if (set_name.equals("") || set_name.length() == 0
                || set_description.equals("") || set_description.length() == 0) {

            new CustomToast().Show_Toast(EditSetInfo.this, edit_setName,
                    "Both fields are required.");
        }

        // Else do signup or do your stuff
        else {
            getUpdateSet();
        }
    }

    public void showAlertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditSetInfo.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete....");

        // Setting Dialog Message
        alertDialog.setMessage("You are about to delete the Set. All the information contained in the Sets will be lost. ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

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
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDeleteSet() {
        try {

            if (CheckNetworkConnection.isOnline(EditSetInfo.this)) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(EditSetInfo.this).getDeleteSet(set_id);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(EditSetInfo.this) {
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

        if(message.equals("success"))
        {
            Intent intent = new Intent(EditSetInfo.this, MyChannelsSet.class);
            intent.putExtra("channel_id", channel_id);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);


        }
        else {
            showLongToast(EditSetInfo.this, message);
        }
    }

    public void getUpdateSet() {
        try {

            if (CheckNetworkConnection.isOnline(EditSetInfo.this)) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(EditSetInfo.this).getUpdateSet(userId, channel_id, set_name, set_description, set_id);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(EditSetInfo.this) {
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

        if(message.equals("success"))
        {
            Intent intent = new Intent(EditSetInfo.this, MyChannelsSet.class);
            intent.putExtra("channel_id", channel_id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);


        }
        else {
            showLongToast(EditSetInfo.this, message);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
