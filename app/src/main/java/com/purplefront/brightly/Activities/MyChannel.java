package com.purplefront.brightly.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Adapters.MyChannelsAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.BadgeDrawable;
import com.purplefront.brightly.Fragments.LoginFragment;
import com.purplefront.brightly.Fragments.MyProfile;
import com.purplefront.brightly.Fragments.Notifications;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.ChannelListResponse;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.Modules.NotificationsResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.CircleTransform;
import com.purplefront.brightly.Utils.SharedPrefUtils;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class MyChannel extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<ContactShare> contactShares = new ArrayList<>();
    private static final int REQUEST_PERMISSION = 1;
    ContactShare contacts;
    private static FragmentManager fragmentManager;
    public ActionBarDrawerToggle toggle;
    public Toolbar toolbar;
    List<ChannelListModel> channelListModels = new ArrayList<>();
    MyChannelsAdapter myChannelsAdapter;
    RelativeLayout channel_layout;
    TextView view_nodata;
    FrameLayout profileContainer;
    ImageView image_createChannel;
    RecyclerView channels_listview;
    Realm realm;
    RealmResults<RealmModel> realmModel;
    String userId;
    String userName;
    String userPhone;
    String userPicture;
    String type = "all";
    String Title = "All Channels";
    String count = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_channel);
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(Title);
        setActionBarTitle(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for (RealmModel model : realmModel) {
            userId = model.getUser_Id();
            userName = model.getUser_Name();
            userPhone = model.getUser_PhoneNumber();
            userPicture = model.getImage();
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
//            getAllContacts();
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        } else {
            requestLocationPermission();
        }

        getNotification();

        channel_layout = (RelativeLayout) findViewById(R.id.channel_layout);
        profileContainer = (FrameLayout) findViewById(R.id.profileContainer);

        profileContainer.setVisibility(View.GONE);
        channel_layout.setVisibility(View.VISIBLE);

        image_createChannel = (ImageView) findViewById(R.id.image_createChannel);
        image_createChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyChannel.this, CreateChannel.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

        view_nodata = (TextView) findViewById(R.id.view_nodata);
        channels_listview = (RecyclerView) findViewById(R.id.channels_listview);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView headerText_Name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.User_Name);
        headerText_Name.setText(userName);
        TextView headerText_Phone = (TextView) navigationView.getHeaderView(0).findViewById(R.id.User_Number);
        headerText_Phone.setText(userPhone);
        ImageView headerImage_Profile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_profile);
        if (userPicture != null) {

            Glide.with(MyChannel.this)
                    .load(userPicture)
                    .centerCrop()
                    .transform(new CircleTransform(MyChannel.this))
//                        .override(50, 50)
                    .into(headerImage_Profile);
        } else {
            Glide.with(MyChannel.this)
                    .load(R.drawable.default_user_image)
                    .centerCrop()
                    .transform(new CircleTransform(MyChannel.this))
                    /*.override(50, 50)*/
                    .into(headerImage_Profile);
        }


        navigationView.setNavigationItemSelectedListener(this);

        getChannelsLists();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void getNotification() {
        try {

            if (CheckNetworkConnection.isOnline(MyChannel.this)) {
                showProgress();
                Call<NotificationsResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(MyChannel.this).getNotifications(userId);
                callRegisterUser.enqueue(new ApiCallback<NotificationsResponse>(MyChannel.this) {
                    @Override
                    public void onApiResponse(Response<NotificationsResponse> response, boolean isSuccess, String message) {
                        NotificationsResponse notificationsResponse = response.body();

                        if (isSuccess) {

                            if (notificationsResponse != null) {

                                setNotificationCounts(notificationsResponse);
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
                        showLongToast(MyChannel.this, message);
                        dismissProgress();
                    }
                });
            } else {

                showLongToast(MyChannel.this, "Network Error");
                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showLongToast(MyChannel.this, "Something went Wrong, Please try Later");


        }
    }

    private void setNotificationCounts(NotificationsResponse notificationsResponse) {

        String message = notificationsResponse.getMessage();

        if(message.equals("success"))
        {
            count = String.valueOf(notificationsResponse.getCount());
        }
        else
        {
            count = "0";
        }
    }

    public void getChannelsLists() {
        try {

            if (CheckNetworkConnection.isOnline(MyChannel.this)) {
//                showProgress();
                Call<ChannelListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(MyChannel.this).getMyChannelsList(userId, type);
                callRegisterUser.enqueue(new ApiCallback<ChannelListResponse>(MyChannel.this) {
                    @Override
                    public void onApiResponse(Response<ChannelListResponse> response, boolean isSuccess, String message) {
                        ChannelListResponse channelListResponse = response.body();
                        if (isSuccess) {

                            if (channelListResponse != null && channelListResponse.getChannels() != null && channelListResponse.getChannels().size() != 0) {

                                channelListModels.clear();
                                channelListModels = channelListResponse.getChannels();
                                setAdapter(channelListModels);
                                channels_listview.setVisibility(View.VISIBLE);
                                view_nodata.setVisibility(View.GONE);
                                dismissProgress();

                            } else {
                                channels_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
                                dismissProgress();
                            }

                        } else {
                            showLongToast(MyChannel.this, message);
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

    private void setAdapter(List<ChannelListModel> channelListModels) {
        channels_listview.setLayoutManager(new GridLayoutManager(MyChannel.this, 3));
        myChannelsAdapter = new MyChannelsAdapter(MyChannel.this, channelListModels);
        channels_listview.setAdapter(myChannelsAdapter);
        myChannelsAdapter.notifyDataSetChanged();

    }

    // Replace Login Fragment with animation
    public void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.profileContainer, new MyProfile(),
                        Util.My_Profile).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment EditProfile_Fragment = fragmentManager
                .findFragmentByTag(Util.Edit_Profile);
        Fragment Profile_Fragment = fragmentManager
                .findFragmentByTag(Util.My_Profile);
        Fragment Notification_Fragment = fragmentManager
                .findFragmentByTag(Util.NOTIFICATIONS);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (channel_layout.getVisibility() == View.VISIBLE) {
            finish();
            super.onBackPressed();
        } else if (EditProfile_Fragment != null) {
            toggle.setDrawerIndicatorEnabled(true);
            replaceLoginFragment();
        } else if (Profile_Fragment != null) {
//            setActionBarTitle(Title);
            backAnimIntent(this, MyChannel.class);
         /*   profileContainer.setVisibility(View.GONE);
            channel_layout.setVisibility(View.VISIBLE);*/
        } else if (Notification_Fragment != null) {
//            setActionBarTitle(Title);
            backAnimIntent(this, MyChannel.class);
            /*profileContainer.setVisibility(View.GONE);
            channel_layout.setVisibility(View.VISIBLE);*/
        } else {
            finish();
            super.onBackPressed();
        }
    }

    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_msg_subject));
        String url = "https://play.google.com/store/apps/details?id=purplefront.com.kriddrpetparent";
        share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg_text) + "\n " + url);
        startActivity(Intent.createChooser(share, "Share link!"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_channel, menu);
        MenuItem itemCart = menu.findItem(R.id.action_bell);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(this, icon, count);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bell) {



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.profileContainer, new MyProfile(),
                            Util.My_Profile).commit();

            setActionBarTitle("My Profile");
            profileContainer.setVisibility(View.VISIBLE);
            channel_layout.setVisibility(View.GONE);


            // Handle the camera action
        } else if (id == R.id.nav_mysubscription) {

            type = "subscribe";
            setActionBarTitle("My Subscriptions");
            profileContainer.setVisibility(View.GONE);
            channel_layout.setVisibility(View.VISIBLE);
            image_createChannel.setVisibility(View.GONE);
            getChannelsLists();

        } else if (id == R.id.nav_myChannels) {

            type = "my";
            setActionBarTitle("My Channels");
            profileContainer.setVisibility(View.GONE);
            channel_layout.setVisibility(View.VISIBLE);
            image_createChannel.setVisibility(View.VISIBLE);
            getChannelsLists();

        } else if (id == R.id.nav_allChannels) {

            type = "all";
            setActionBarTitle("All Channels");
            profileContainer.setVisibility(View.GONE);
            channel_layout.setVisibility(View.VISIBLE);
            image_createChannel.setVisibility(View.VISIBLE);
            getChannelsLists();

        } else if (id == R.id.nav_notifications) {

            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.profileContainer, new Notifications(),
                            Util.NOTIFICATIONS).commit();

            setActionBarTitle("Notification");
            profileContainer.setVisibility(View.VISIBLE);
            channel_layout.setVisibility(View.GONE);

        } else if (id == R.id.nav_about) {


            showLongToast(MyChannel.this, "Comming Soon");

        } else if (id == R.id.nav_invite) {

            shareTextUrl();

        } else if (id == R.id.nav_logout) {

            realm.beginTransaction();

            // delete all realm objects
            realm.deleteAll();

            //commit realm changes
            realm.commitTransaction();
            /*realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realmModel.deleteAllFromRealm();// use to delete all

                    //**OR** use in for loop to delete perticulr record as a location
                    realmModel.deleteFromRealm();

                }
            });*/
            finishIntent(MyChannel.this, Login.class);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    @SuppressLint("StaticFieldLeak")
    class LoadContact extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                        SharedPreferences mPrefs = getSharedPreferences("contactShares", MODE_PRIVATE);
//                        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyChannel.this);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(contactShares);
                        prefsEditor.putString("contactShares", json);
                        prefsEditor.commit();

                    }
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
