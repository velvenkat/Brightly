package com.purplefront.brightly.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Adapters.ContactsAdapter;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.Modules.SharedDataModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class ShareWithContacts extends BaseActivity {

    private static final int REQUEST_PERMISSION = 1;

    ListView contacts_listview;
    EditText conatcts_searchView;
    ImageView btn_share, btn_sync;

    ArrayList<ContactShare> contactShares = new ArrayList<>();
    ArrayList<ContactShare> getContactShares;
    ContactShare contacts;
    ContactsAdapter contactAdapter;

    String split_num = "";

    String set_description = "";
    String set_name = "";
    String set_id = "";
    String userId;
    String share_link;
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_with_contacts);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        contacts_listview = (ListView) findViewById(R.id.contacts_listview);
        contacts_listview.setTextFilterEnabled(true);
        conatcts_searchView = (EditText) findViewById(R.id.conatcts_searchView);

        btn_share = (ImageView) findViewById(R.id.btn_share);
        btn_sync = (ImageView) findViewById(R.id.btn_sync);

        chl_list_obj=getIntent().getParcelableExtra("model_obj");
        setsListModel = getIntent().getParcelableExtra("setsListModel");
        set_description = setsListModel.getDescription();
        set_name = setsListModel.getSet_name();
        set_id = setsListModel.getSet_id();
        share_link = setsListModel.getShare_link();
        userId = getIntent().getStringExtra("userId");


        SharedPreferences mPrefs = getSharedPreferences("contactShares", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("contactShares", "");
        Type type = new TypeToken<ArrayList<ContactShare>>(){}.getType();
        contactShares = gson.fromJson(json, type);
        setConatcts(contactShares);

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               checkValidation();
//               Toast.makeText(ShareWithContacts.this,"Sel_num"+split_num,Toast.LENGTH_LONG).show();

            }
        });


       btn_sync.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {
               showLongToast(ShareWithContacts.this, "Loading New Contacts");
               if (ActivityCompat.checkSelfPermission(ShareWithContacts.this, android.Manifest.permission.READ_CONTACTS)
                       == PackageManager.PERMISSION_GRANTED) {
                   getAllContacts();
               } else {
                   requestLocationPermission();
               }
           }
       });

        // Capture Text in EditText
        conatcts_searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String srch_str=conatcts_searchView.getText().toString();
                if(srch_str.trim().equals("")){

                }
                else {
                    String text = srch_str.trim().toLowerCase();

                        if (contactAdapter != null) {
                            contactAdapter.filter(text);
                        } else {
                            Toast.makeText(ShareWithContacts.this, "Could't able to find the Data ,Please try after some time.", Toast.LENGTH_LONG).show();
                        }
                    }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }


    // Check Validation before login
    private void checkValidation() {
        // Get phonenumber
        ArrayList<String>sel_num= new ArrayList<>(contactAdapter.getContact_Share());
        split_num = android.text.TextUtils.join(",",sel_num);

        if (split_num.equals("") || split_num.length() <= 9 )
        {
            new CustomToast().Show_Toast(ShareWithContacts.this, btn_share,
                    "Please select the Phone Number.");

        }
        // Else do login and do your stuff
        else {
           getShareSet();
            //            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT).show();
        }

    }

    private void getShareSet() {

        try {

            if (CheckNetworkConnection.isOnline(ShareWithContacts.this)) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(ShareWithContacts.this).getShareSet(userId, set_id, split_num);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(ShareWithContacts.this) {
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


        String message = addMessageResponse.getMsg();
        showLongToast(ShareWithContacts.this, addMessageResponse.getMessage());

        if(message.equals("success"))
        {
            Intent intent = new Intent(ShareWithContacts.this, EditSetInfo.class);
            intent.putExtra("userId", userId);
            intent.putExtra("model_obj", chl_list_obj);
            intent.putExtra("setsListModel", setsListModel);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        }
        else {
            showLongToast(ShareWithContacts.this, message);
        }
    }


    protected void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS}, REQUEST_PERMISSION);

          /*  ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_PERMISSION);*/
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   // getAllContacts();

                } else {

                    showLongToast(ShareWithContacts.this, "Contacts not Updated. Please provide permission");
                }
                return;
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    private void getAllContacts() {
//        showProgress();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contacts = new ContactShare();
                    contacts.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactShares.add(contacts);

                }
            }

            setConatcts(contactShares);
            showLongToast(ShareWithContacts.this, "Contacts Updated");
        }
    }

    private void setConatcts(ArrayList<ContactShare> contactShares) {
        this.getContactShares = contactShares;
        contactAdapter = new ContactsAdapter(ShareWithContacts.this, getContactShares);
        contacts_listview.setAdapter(contactAdapter);
        SharedPreferences mPrefs = getSharedPreferences("contactShares", MODE_PRIVATE);
//                        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyChannel.this);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(contactShares);
                    prefsEditor.putString("contactShares", json);
                    prefsEditor.commit();

    }
}


