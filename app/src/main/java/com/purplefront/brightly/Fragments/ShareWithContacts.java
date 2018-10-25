package com.purplefront.brightly.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.ContactsAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ShareWithContacts extends BaseFragment {

    private static final int REQUEST_PERMISSION = 1;

    ListView contacts_listview;
    EditText conatcts_searchView;
    ImageView btn_share, btn_sync;

    ArrayList<ContactShare> contactShares = new ArrayList<>();
    ArrayList<ContactShare> getContactShares;
    ContactShare contacts;
    ContactsAdapter contactAdapter;

    String split_num = "";
    String split_name = "";

    String set_description = "";
    String set_name = "";
    String set_id = "";
    String userId;
    String share_link;
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;
    // ActionBarUtil actionBarUtilObj;
    NotificationsModel notificationsModel;
    boolean isNotification;
    RealmModel user_obj;
    View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_share_with_contacts, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = false;
//        setContentView(R.layout.activity_share_with_contacts);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        contacts_listview = (ListView) rootView.findViewById(R.id.contacts_listview);
        contacts_listview.setTextFilterEnabled(true);
        conatcts_searchView = (EditText) rootView.findViewById(R.id.conatcts_searchView);

        btn_share = (ImageView) rootView.findViewById(R.id.btn_share);
        btn_sync = (ImageView) rootView.findViewById(R.id.btn_sync);

        Bundle bundle = getArguments();

        if (bundle != null) {
            isNotification = bundle.getBoolean("isNotification", false);
        }

        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            share_link = notificationsModel.getNotificationsSetDetail().getShare_link();
        } else {
            chl_list_obj = bundle.getParcelable("model_obj");
            setsListModel = bundle.getParcelable("setsListModel");
            set_description = setsListModel.getDescription();
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();
            share_link = setsListModel.getShare_link();
        }
        userId = user_obj.getUser_Id();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share with Contacts");

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences mPrefs = getActivity().getSharedPreferences("contactShares", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("contactShares", "");
            Type type = new TypeToken<ArrayList<ContactShare>>() {
            }.getType();
            contactShares = gson.fromJson(json, type);
            setConatcts(contactShares);
        } else {

            requestLocationPermission();
        }
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
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    showLongToast(getActivity(), "Loading New Contacts");
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
                String srch_str = conatcts_searchView.getText().toString();
                if (srch_str.trim().equals("")) {

                } else {
                    String text = srch_str.trim().toLowerCase();

                    if (contactAdapter != null) {
                        contactAdapter.filter(text);
                    } else {
                        Toast.makeText(getContext(), "Could't able to find the Data ,Please try after some time.", Toast.LENGTH_LONG).show();
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
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false);
                    return true;
                }
                return false;
            }
        });

        return rootView;

    }


    // Check Validation before login
    private void checkValidation() {
        // Get phonenumber
        ArrayList<String> sel_num = new ArrayList<>(contactAdapter.getContact_Share());
        ArrayList<String> sel_name = new ArrayList<>(contactAdapter.getContactName_Share());
        split_num = android.text.TextUtils.join(",", sel_num);
        split_name = android.text.TextUtils.join(",", sel_name);

        if (split_num.equals("") || split_num.length() <= 9) {
            new CustomToast().Show_Toast(getActivity(), btn_share,
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

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getShareSet(userId, set_id, split_num, split_name);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();
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


    private void setAddSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMsg();
        showLongToast(getActivity(), addMessageResponse.getMessage());

        if (message.equals("success")) {
         /*   Intent intent = new Intent(ShareWithContacts.this, EditSetInfo.class);
            intent.putExtra("userId", userId);
            intent.putExtra("model_obj", chl_list_obj);
            intent.putExtra("setsListModel", setsListModel);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
            EditSetInfo parent_frag = (EditSetInfo) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.Edit_Set);
            parent_frag.isShareSetChgd = true;
            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false, 2);
        } else {
            showLongToast(getActivity(), message);
        }
    }


    protected void requestLocationPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS}, REQUEST_PERMISSION);
        // show UI part if you want here to show some rationale !!!
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getAllContacts();

                } else {

                    showLongToast(getActivity(), "Contacts not Updated. Please provide permission");
                }
                return;
            }

        }
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/

    private void getAllContacts() {
//        showProgress();
        contactShares.clear();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    phoneCursor.moveToFirst();

                    if (phoneCursor.getCount() > 0) {
                        do {

                            contacts = new ContactShare();
                            contacts.setContactName(name);

                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contacts.setContactNumber(phoneNumber);
                            contactShares.add(contacts);

                        } while (phoneCursor.moveToNext());
                    }
                   /* if (phoneCursor.moveToLast()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.setContactNumber(phoneNumber);
                    }*/

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }

                    /*if (!contactShares.contains(contacts)) {
                        contactShares.add(contacts);
                    }*/


                }
            } while (cursor.moveToNext());

            setConatcts(contactShares);
            showLongToast(getActivity(), "Contacts Updated");
            cursor.close();
        }
    }

    private void setConatcts(ArrayList<ContactShare> contactShares) {


        this.getContactShares = contactShares;

        contactAdapter = new ContactsAdapter(getContext(), getContactShares);
        contacts_listview.setAdapter(contactAdapter);
        SharedPreferences mPrefs = getActivity().getSharedPreferences("contactShares", MODE_PRIVATE);
//      SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyChannel.this);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactShares);
        prefsEditor.putString("contactShares", json);
        prefsEditor.commit();

    }
}


