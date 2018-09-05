package com.purplefront.brightly.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.purplefront.brightly.Adapters.ContactsAdapter;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShareWithContacts extends BaseActivity {

    private static final int REQUEST_PERMISSION = 1;

    ListView contacts_listview;
    EditText conatcts_searchView;

    ArrayList<ContactShare> contactShares = new ArrayList<>();
    ArrayList<ContactShare> getContactShares;
    ContactShare contacts;
    ContactsAdapter contactAdapter;

    ProgressDialog progress;

    String set_name = "";
    String set_id = "";
    String userId;
    String share_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_with_contacts);

        contacts_listview = (ListView) findViewById(R.id.contacts_listview);
        conatcts_searchView = (EditText) findViewById(R.id.conatcts_searchView);


        set_name = getIntent().getStringExtra("set_name");
        set_id = getIntent().getStringExtra("set_id");
        share_link = getIntent().getStringExtra("share_link");
        userId = getIntent().getStringExtra("userId");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
//            getAllContacts();
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        } else {
            requestLocationPermission();
        }

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

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   // getAllContacts();
                    LoadContact loadContact = new LoadContact();
                    loadContact.execute();

                } else {

                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    /*private void getAllContacts() {
        progress=new ProgressDialog(ShareWithContacts.this);
        progress.setMessage("Extracting Contacts...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

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
            progress.dismiss();
        }
    }*/

    private void setConatcts(ArrayList<ContactShare> contactShares) {
        this.getContactShares = contactShares;
        contactAdapter = new ContactsAdapter(ShareWithContacts.this, getContactShares);
        contacts_listview.setAdapter(contactAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    class LoadContact extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress=new ProgressDialog(ShareWithContacts.this);
            progress.setMessage("Extracting Contacts...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            showProgress();

        }

        @Override
        protected Void doInBackground(Void... voids) {
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

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /*contactAdapter = new ContactsAdapter(ShareWithContacts.this, contactShares);
            contacts_listview.setAdapter(contactAdapter);*/
            setConatcts(contactShares);
            dismissProgress();
            progress.dismiss();
        }
    }

}


