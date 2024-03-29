package com.digital_easy.info_share.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.ContactsAdapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.CustomToast;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.ContactShare;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.PermissionUtil;
import com.digital_easy.info_share.Utils.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ShareWithContacts extends BaseFragment implements BrightlyNavigationActivity.PermissionResultInterface, ContactsAdapter.ContactAdapterInterface {

    private static final int REQUEST_PERMISSION = 1;

    ListView contacts_listview;
    EditText conatcts_searchView;
    ImageView btn_sync;
    TextView view_nodata;

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
    boolean isScrnSetList;
    boolean isCardShare;
    //String card_shre_Org_set_id;
 /*   String card_shr_create_set_name;
    String card_shr_create_set_desc;*/
    //String card_shr_card_id;
    // String card_shr_prev_catg_id = "";

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuInflater inflater = getActivity().getMenuInflater();
        menu.clear();
        if (chk_sel_items()) {
            inflater.inflate(R.menu.share_icon_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mnu_share:
                checkValidation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_share_with_contacts, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = false;
        ((BrightlyNavigationActivity) getActivity()).permissionResultInterfaceObj = this;
//        setContentView(R.layout.activity_share_with_contacts);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setHasOptionsMenu(true);
        contacts_listview = (ListView) rootView.findViewById(R.id.contacts_listview);
        contacts_listview.setTextFilterEnabled(true);
        conatcts_searchView = (EditText) rootView.findViewById(R.id.conatcts_searchView);


        btn_sync = (ImageView) rootView.findViewById(R.id.btn_sync);
        view_nodata = (TextView) rootView.findViewById(R.id.view_nodata);

        Bundle bundle = getArguments();

        if (bundle != null) {
            isNotification = bundle.getBoolean("isNotification", false);
            isScrnSetList = bundle.getBoolean("isScrnSetList", false);
            isCardShare = bundle.getBoolean("isCardShare", false);
        }

        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            share_link = notificationsModel.getNotificationsSetDetail().getShare_link();
            if (isCardShare) {
                //  card_shr_card_id = bundle.getString("card_shr_card_id");
                //  card_shr_prev_catg_id = bundle.getString("card_shr_chl_id");
              /*  card_shr_create_set_name = bundle.getString("card_shr_set_name");
                card_shr_create_set_desc = bundle.getString("card_shr_set_desc");*/

            }
        } else {
            //chl_list_obj = bundle.getParcelable("model_obj");
            chl_list_obj = ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj;
            setsListModel = bundle.getParcelable("setsListModel");
            set_description = setsListModel.getDescription();
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();
            share_link = setsListModel.getShare_link();
            if (isCardShare) {
                //card_shr_card_id = bundle.getString("card_shr_card_id");
                //  card_shr_prev_catg_id = bundle.getString("card_shr_chl_id");
             /*   card_shr_create_set_name = bundle.getString("card_shr_set_name");
                card_shr_create_set_desc = bundle.getString("card_shr_set_desc");*/

            }
        }

        // card_shre_Org_set_id = bundle.getString("org_set_id");
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().show();

        userId = user_obj.getUser_Id();

        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Share with contacts");

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences mPrefs = getActivity().getSharedPreferences("contactShares", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("contactShares", "");
            Type type = new TypeToken<ArrayList<ContactShare>>() {
            }.getType();
            contactShares = gson.fromJson(json, type);


            if (contactShares != null && contactShares.size() > 0)
                setConatcts(contactShares);
        } else {
            view_nodata.setVisibility(View.VISIBLE);
            requestLocationPermission();
        }
     /*   btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//               Toast.makeText(ShareWithContacts.this,"Sel_num"+split_num,Toast.LENGTH_LONG).show();

            }
        });*/


        btn_sync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
//                     getAllContacts();
                    view_nodata.setVisibility(View.GONE);
                    LoadContact loadContact = new LoadContact();
                    loadContact.execute();
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
                    if (!isCardShare)
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false);
                    else
                        ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    return true;
                }
                return false;
            }
        });

        return rootView;

    }

    private boolean chk_sel_items() {
        if (contactAdapter != null) {
            int sizeVal = contactAdapter.getContact_Share().size();
            if (sizeVal > 0) {
                return true;
            }
        }
        return false;
    }

    // Check Validation before login
    private void checkValidation() {
        // Get phonenumber
        ArrayList<String> sel_num = new ArrayList<>(contactAdapter.getContact_Share());
        ArrayList<String> sel_name = new ArrayList<>(contactAdapter.getContactName_Share());
        String num_digit10 = "";
        split_num = android.text.TextUtils.join(",", sel_num);
        split_name = android.text.TextUtils.join(",", sel_name);
        if (split_num.equals("") && split_name.equals("")) {
            new CustomToast().Show_Toast(getActivity(), btn_sync,
                    "Please select a contact to share.");
        } else {
            num_digit10 = split_num.substring(split_num.length() - 10, split_num.length());

            // Toast.makeText(getContext(),"num"+num_digit10,Toast.LENGTH_LONG).show();
            if (split_num.equals("") || split_num.length() <= 9) {
                new CustomToast().Show_Toast(getActivity(), btn_sync,
                        "Please select the Phone Number.");

            } else if (num_digit10.equalsIgnoreCase(user_obj.getUser_PhoneNumber())) {
                new CustomToast().Show_Toast(getActivity(), btn_sync,
                        "You can't give your own number");
            }
            // Else do login and do your stuff
            else {
                getShareSet();
                //            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each : all) if (!set.add(each)) return true;
        return false;
    }

    private void getShareSet() {

        try {
            Call<AddMessageResponse> callRegisterUser;
            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();
                if (!isCardShare)
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getShareSet(userId, set_id, split_num, split_name);
                else {
                    //CARD SHARE
                    if (!isNotification)
                        callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getShareSet(userId, set_id, split_num, split_name);
                    else
                        callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getShareSet(userId, set_id, split_num, split_name);
                }
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
                        if (message.equals("timeout")) {
                            showLongToast(getActivity(), "Internet is too slow.");
                            ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false, 2);
                        }
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


        String msg = addMessageResponse.getMsg();
        String message = addMessageResponse.getMessage();

        if (msg.equals("success")) {
         /*   Intent intent = new Intent(ShareWithContacts.this, EditSetInfo.class);
            intent.putExtra("userId", userId);
            intent.putExtra("model_obj", chl_list_obj);
            intent.putExtra("setsListModel", setsListModel);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
            showLongToast(getActivity(), addMessageResponse.getMessage());
            if (!isCardShare) {
                if (!isScrnSetList) {
                    ShareSettings parent_frag = (ShareSettings) ((BrightlyNavigationActivity) getActivity()).getSupportFragmentManager().findFragmentByTag(Util.Set_Share_settings);
                    parent_frag.isShareSetChgd = true;
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(false, 2);
                } else {
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true, 2);
                }
            } else {
                ((BrightlyNavigationActivity) getActivity()).DontRun = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true, 3);
            }
        } else {
            showLongToast(getActivity(), message);
        }
    }


    protected void requestLocationPermission() {

      /*  ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS}, REQUEST_PERMISSION);
        // show UI part if you want here to show some rationale !!!*/
        if (PermissionUtil.hasPermission(new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS}, getContext(), BrightlyNavigationActivity.PERMISSION_REQ_CODE_CONTACT)) {
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {


    }*/

    /*private void getAllContacts() {
//        showProgress();
        contactShares.clear();
        ArrayList<String> list_temp_phone_no = new ArrayList<>();
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
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                            String temp_ph_no = phoneNumber.replaceAll("[^0-9]", "").trim();

                            if (temp_ph_no.length() >= 10) {
                                if (temp_ph_no.length() == 10) {

                                } else {
                                    temp_ph_no=temp_ph_no.substring(temp_ph_no.length() - 10);
                                }
                                list_temp_phone_no.add(temp_ph_no);

                                if (list_temp_phone_no.size() > 0 && hasDuplicate(list_temp_phone_no)) {
                                    list_temp_phone_no.remove(list_temp_phone_no.size() - 1);
                                } else {
                                    contacts = new ContactShare();
                                    contacts.setContactName(name);


                                    contacts.setContactNumber(phoneNumber);
                                    contactShares.add(contacts);
                                }
                            }
                        } while (phoneCursor.moveToNext());
                    }
                    phoneCursor.close();

                }



*//*
                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }*//*

     *//*if (!contactShares.contains(contacts)) {
                        contactShares.add(contacts);
                    }*//*


            } while (cursor.moveToNext());

            if (contactShares.size() > 0)
                setConatcts(contactShares);
            showLongToast(getActivity(), "Contacts Updated");
            cursor.close();
        }
    }*/

    private void setConatcts(ArrayList<ContactShare> contactShares) {


        this.getContactShares = contactShares;

        contactAdapter = new ContactsAdapter(getContext(), getContactShares, this);
        contacts_listview.setAdapter(contactAdapter);
        SharedPreferences mPrefs = getActivity().getSharedPreferences("contactShares", MODE_PRIVATE);
//      SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyChannel.this);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactShares);
        prefsEditor.putString("contactShares", json);
        prefsEditor.commit();

    }

    @Override
    public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list) {
        //Toast.makeText(getContext(), "I am here", Toast.LENGTH_LONG).show();
        contact_list = contactShares;
        if (contactShares != null) {
            if (contactShares.size() > 0)
                setConatcts(contactShares);
            showLongToast(getActivity(), "Contacts Updated");
        }

//                    getAllContacts();
       /* LoadContact loadContact = new LoadContact();
        loadContact.execute();*/


    }

    @Override
    public void contactsClicked() {
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LoadContact extends AsyncTask<Void, Void, Void> {

        ArrayList<String> list_temp_phone_no;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
            contactShares.clear();
            showLongToast(getActivity(), "Loading New Contacts");
            list_temp_phone_no = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            contactShares = new ArrayList<>();
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
                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                String temp_ph_no = phoneNumber.replaceAll("[^0-9]", "").trim();

                                if (temp_ph_no.length() >= 10) {
                                    if (temp_ph_no.length() == 10) {

                                    } else {
                                        temp_ph_no = temp_ph_no.substring(temp_ph_no.length() - 10);
                                    }
                                    list_temp_phone_no.add(temp_ph_no);

                                    if (list_temp_phone_no.size() > 0 && hasDuplicate(list_temp_phone_no)) {
                                        list_temp_phone_no.remove(list_temp_phone_no.size() - 1);
                                    } else {
                                        contacts = new ContactShare();
                                        contacts.setContactName(name);


                                        contacts.setContactNumber(phoneNumber);

                                        contactShares.add(contacts);
                                    }
                                }


                            } while (phoneCursor.moveToNext());
                        }
                        phoneCursor.close();

                      /*  Cursor emailCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (emailCursor.moveToNext()) {
                            String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        }
*/

                    }
                } while (cursor.moveToNext());
                cursor.close();

            }
//                cursor.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgress();
            if (contactShares.size() > 0)
                setConatcts(contactShares);
            showLongToast(getActivity(), "Contacts Updated");

        }
    }
}


