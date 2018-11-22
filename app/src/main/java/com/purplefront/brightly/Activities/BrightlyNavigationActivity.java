package com.purplefront.brightly.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.BadgeDrawable;
import com.purplefront.brightly.Fragments.CardDetailFragment;
import com.purplefront.brightly.Fragments.CardList;
import com.purplefront.brightly.Fragments.ChannelFragment;
import com.purplefront.brightly.Fragments.EditSetInfo;
import com.purplefront.brightly.Fragments.MyProfile;
import com.purplefront.brightly.Fragments.Notifications;
import com.purplefront.brightly.Fragments.SharePage;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.NotificationsResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.CircleTransform;
import com.purplefront.brightly.Utils.PermissionUtil;
import com.purplefront.brightly.Utils.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class BrightlyNavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,BaseActivity.alert_dlg_interface {

    private static final String SHOWCASE_ID = "1";
    public PermissionResultInterface permissionResultInterfaceObj;
    ArrayList<ContactShare> contactShares = new ArrayList<>();
    private static final int REQUEST_PERMISSION = 1;
    ContactShare contacts;
    private FragmentManager fragmentManager;
    public ActionBarDrawerToggle toggle;
    public DrawerLayout drawer;
    public Toolbar toolbar;
    FrameLayout frag_container;
    Realm realm;
    int PermissionRequestedCode = -1;
    public static final int PERMISSION_REQ_CODE_AUDIO = 101;
    public static final int PERMISSION_REQ_CODE_CONTACT = 102;
    public static final int PERMISSION_REQ_CODE_IMAGE = 103;
    RealmResults<RealmModel> realmModel;

    public String userId;
    String userName;
    String userPhone;
    String userPicture;
    String type = "all";
    String Title = "All Channels";
    String count = "0";
    int PerCount = -1;
    String deviceToken;
    public boolean DontRunOneTime = false;
    boolean isNotification = false;
    View target_menu;

    boolean isCardNotification = false;
    public boolean isUTubePlayerFullScreen = false;
    public NavigationView navigationView;
    public TextView headerText_Name;
    public TextView headerText_Phone;
    public ImageView headerImage_Profile;
    public boolean isCardClicked = false;
    public int card_toPosition = 0;
    public YouTubePlayer uTubePlayer;
    public boolean isHide_frag = false;
    boolean isPerReqCalled = false;

    /**
     * isCardRefresh flag is needed to refresh card from CardList page to CardDetail called
     */
    public boolean isCardRefresh = false;

    private RealmModel user_obj;
    /**
     * Default true
     */
    public boolean DisableBackBtn = true;  //Default should be true
    public boolean DontRun = false;
    public boolean isRevoked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        isNotification = getIntent().getBooleanExtra("isNotification", false);
        isCardNotification = getIntent().getBooleanExtra("isCardNotification", false);
        isRevoked=getIntent().getBooleanExtra("isRevoked",false);

        fragmentManager = getSupportFragmentManager();
        setDlgListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.my_channel);
        setSupportActionBar(toolbar);
        target_menu = (toolbar.findViewById(R.id.action_bell));


        setActionBarTitle(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        contactShares = new ArrayList<>();

        user_obj = getIntent().getParcelableExtra("user_obj");

        realmModel.load();
        for (RealmModel model : realmModel) {
            userId = model.getUser_Id();
            userName = model.getUser_Name();
            userPhone = model.getUser_PhoneNumber();
            userPicture = model.getImage();
            deviceToken = model.getDeviceToken();

            if (user_obj == null) {
                user_obj = model;
            }

        }

        if (PermissionUtil.hasPermission(new String[]{Manifest.permission.READ_CONTACTS}, BrightlyNavigationActivity.this, PERMISSION_REQ_CODE_CONTACT)) {
//            getAllContacts();
            SharedPreferences mPrefs = getSharedPreferences("contactShares", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("contactShares", "");
            Type type = new TypeToken<ArrayList<ContactShare>>() {
            }.getType();
            contactShares = gson.fromJson(json, type);

            if (contactShares == null) {
                LoadContact loadContact = new LoadContact();
                loadContact.execute();
            }

        } else {
            requestLocationPermission();
        }


        frag_container = (FrameLayout) findViewById(R.id.frag_container);


        if (isNotification) {
            NotificationsModel nfy_model = getIntent().getParcelableExtra("notfy_modl_obj");
            if (isCardNotification) {

                Fragment card_frag = new CardDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNotification", true);
                bundle.putParcelable("notfy_modl_obj", nfy_model);
                card_frag.setArguments(bundle);
                onFragmentCall(Util.view_card, card_frag, false);
            }
            else if(isRevoked){
                //Only for back end Notifications
                showAlertDialog_ok(nfy_model.getNotificationsSetDetail().getName()+" set permission has been revoked","Alert","Ok");
            }
            else {
                Fragment nfy_frag = new Notifications();
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new Notifications(),
                            Util.NOTIFICATIONS).commit();
*/
                 Bundle bundle=new Bundle();
                 bundle.putBoolean("isFireBaseNfy",true);
                 nfy_frag.setArguments(bundle);
                onFragmentCall(Util.NOTIFICATIONS, nfy_frag, false);
            }
            // setActionBarTitle("Notification");
            // frag_container.setVisibility(View.VISIBLE);

        } else {

            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            Fragment chl_frag = new ChannelFragment();
            chl_frag.setArguments(bundle);
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();

            //  setActionBarTitle("Notification");
            frag_container.setVisibility(View.VISIBLE);*/
            onFragmentCall(Util.CHANNELS, chl_frag, false);
        }


      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerText_Name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.User_Name);
        headerText_Name.setText(userName);
        headerText_Phone = (TextView) navigationView.getHeaderView(0).findViewById(R.id.User_Number);
        headerText_Phone.setText(userPhone);
        headerImage_Profile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_profile);
        if (userPicture != null) {

            Glide.with(getApplicationContext())
                    .load(userPicture)
                    .centerCrop()
                    .transform(new CircleTransform(BrightlyNavigationActivity.this))
//                        .override(50, 50)
                    .into(headerImage_Profile);
        } else {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_user_image)
                    .centerCrop()
                    .transform(new CircleTransform(BrightlyNavigationActivity.this))
                    /*.override(50, 50)*/
                    .into(headerImage_Profile);
        }


        navigationView.setNavigationItemSelectedListener(this);

        //getChannelsLists();
//        MaterialShowcaseView.resetSingleUse(BrightlyNavigationActivity.this, SHOWCASE_ID);
//        ShowcaseSingle();
//        MultipleShowcase();
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(null);
    }
  /*  private void ShowcaseSingle() {


        // single example
        new MaterialShowcaseView.Builder(this)
                .setTarget(target)
                .setDismissText("GOT IT")
                .setDismissTextColor(R.color.black)
                .setContentText("This is some amazing feature you should know about")
                .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                .show();
    }*/

    private void MultipleShowcase() {
// sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(1000); // half second between each showcase view
        config.setMaskColor(Color.parseColor("#AA000000"));
        config.setContentTextColor(Color.WHITE);
        config.setDismissTextColor(Color.CYAN);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setConfig(config);

        sequence.addSequenceItem(target_menu,
                "Here all types of Set, Cards Notifications shown", "GOT IT");

       /* sequence.addSequenceItem(image_createChannel,
                "Click this to Create a Channel", "GOT IT");
*/
/* sequence.addSequenceItem(mButtonThree,
"This is button three", "GOT IT");*/

        sequence.start();
    }

    /*private void MultipleShowcase() {
        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setConfig(config);

        sequence.addSequenceItem(target_menu,
                "Here all types of Set, Cards Notifications shown", "GOT IT");



       *//* sequence.addSequenceItem(mButtonThree,
                "This is button three", "GOT IT");*//*

        sequence.start();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        // put your code here...
        //getNotificationCount();
        if (isPerReqCalled) {
            if (permissionResultInterfaceObj != null) {
                if (PermissionRequestedCode == PERMISSION_REQ_CODE_AUDIO) {
                    permissionResultInterfaceObj.onPermissionResult_rcd(null);

                }
                if (PermissionRequestedCode == PERMISSION_REQ_CODE_IMAGE) {
                   /* permissionResultInterfaceObj.onPermissionResult_rcd(null);*/
                    Toast.makeText(BrightlyNavigationActivity.this,"Tab again to upload the image",Toast.LENGTH_LONG).show();
                }
                if (PermissionRequestedCode == PERMISSION_REQ_CODE_CONTACT) {
                    //  permissionResultInterfaceObj.onPermissionResult_rcd(null);
                    LoadContact loadContact = new LoadContact();
                    loadContact.execute();
                }
            }
            isPerReqCalled = false;
        }


    }


    private void setNotificationCounts(NotificationsResponse notificationsResponse) {

        String message = notificationsResponse.getMessage();

        if (message.equals("success")) {
            count = notificationsResponse.getCount();
        } else {
            count = "0";
        }
    }


    // Replace Login Fragment with animation
    public void replaceLoginFragment() {
        Fragment prof_frag = new MyProfile();
        onFragmentCall(Util.My_Profile, prof_frag, false);
        /*fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frag_container, new MyProfile(),
                        Util.My_Profile).commit();*/
    }

    @Override
    public void onBackPressed() {
        /*Fragment EditProfile_Fragment = fragmentManager
                .findFragmentByTag(Util.Edit_Profile);
        Fragment Profile_Fragment = fragmentManager
                .findFragmentByTag(Util.My_Profile);
        Fragment Notification_Fragment = fragmentManager
                .findFragmentByTag(Util.NOTIFICATIONS);
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } /*else if (EditProfile_Fragment != null) {
            toggle.setDrawerIndicatorEnabled(true);
            replaceLoginFragment();
        } else if (Profile_Fragment != null) {
//            setActionBarTitle(Title);
            backAnimIntent(this, BrightlyNavigationActivity.class);
         *//*   frag_container.setVisibility(View.GONE);
            channel_layout.setVisibility(View.VISIBLE);*//*
        } else if (Notification_Fragment != null) {
//            setActionBarTitle(Title);
            backAnimIntent(this, BrightlyNavigationActivity.class);
            *//*frag_container.setVisibility(View.GONE);
            channel_layout.setVisibility(View.VISIBLE);*//*

        } */ else if (uTubePlayer != null && isUTubePlayerFullScreen) {

            uTubePlayer.setFullscreen(false);
        } else {
            //    finish();
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
        boolean isChannelScrn = false;
        if (id == R.id.nav_profile) {

            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new MyProfile(),
                            Util.My_Profile).commit();
*/

            onFragmentCall(Util.My_Profile, new MyProfile(), false);
            // Handle the camera action
        } else if (id == R.id.nav_mysubscription) {

            type = "subscribe";
            isChannelScrn = true;
            //setActionBarTitle("Shared With Me");

        } else if (id == R.id.nav_myChannels) {

            type = "my";
            isChannelScrn = true;
            //setActionBarTitle("My Channels");


        } else if (id == R.id.nav_allChannels) {

            type = "all";
            isChannelScrn = true;
//            setActionBarTitle("All Channels");


        } else if (id == R.id.nav_notifications) {

            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new Notifications(),
                            Util.NOTIFICATIONS).commit();*/
            Fragment nfy_fragment = new Notifications();


            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();*/
            onFragmentCall(Util.NOTIFICATIONS, nfy_fragment, false);


        } else if (id == R.id.nav_about) {


            showLongToast(BrightlyNavigationActivity.this, "Comming Soon");

        } else if (id == R.id.nav_invite) {

            shareTextUrl();

        } else if (id == R.id.nav_logout) {
           showLogOutDialog();

        }
        if (isChannelScrn) {
            Fragment fragment = new ChannelFragment();

            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            fragment.setArguments(bundle);
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();*/
            onFragmentCall(Util.CHANNELS, fragment, false);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void requestLocationPermission() {
      /*  if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS}, REQUEST_PERMISSION);

          *//*  ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_PERMISSION);*//*
        }*/
        PermissionUtil.hasPermission(new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS}, BrightlyNavigationActivity.this, PERMISSION_REQ_CODE_CONTACT);

    }

    public void showLogOutDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BrightlyNavigationActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Logging Out....");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to Logout? ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {


                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                // delete all realm objects
                realm.deleteAll();

                //commit realm changes
                realm.commitTransaction();
                if (CheckNetworkConnection.isOnline(BrightlyNavigationActivity.this)) {
                    showProgress();
                    Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(BrightlyNavigationActivity.this).call_logout_user(userId, deviceToken);
                    callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(BrightlyNavigationActivity.this) {
                        @Override
                        public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                            dismissProgress();
                            Toast.makeText(BrightlyNavigationActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onApiFailure(boolean isSuccess, String message) {

                            dismissProgress();
                        }
                    });
                } /*else {

                dismissProgress();
            }*/ else {
                    Toast.makeText(BrightlyNavigationActivity.this, "Check network connection", Toast.LENGTH_LONG).show();
                }
            /*realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realmModel.deleteAllFromRealm();// use to delete all

                    //**OR** use in for loop to delete perticulr record as a location
                    realmModel.deleteFromRealm();

                }
            });*/
                finishIntent(BrightlyNavigationActivity.this, Login.class);

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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (PerCount == -1) {
            PerCount = permissions.length - 1;
        } else {
            PerCount--;
        }
        if (PerCount == 0) {

            PerCount = -1;

  /*      Toast.makeText(BrightlyNavigationActivity.this,"OnPerResult",Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // getAllContacts();
                    if (permissions[0].equalsIgnoreCase(Manifest.permission.RECORD_AUDIO)) {

                    } else {
                        LoadContact loadContact = new LoadContact();
                        loadContact.execute();
                    }

                } else {
                    if (permissions[0].equalsIgnoreCase(Manifest.permission.RECORD_AUDIO)) {
                        showLongToast(BrightlyNavigationActivity.this, "Please provide permission for record audio");
                    }

                    // permission denied,Disable the
                    // functionality that depends on this permission.
                    else
                        showLongToast(BrightlyNavigationActivity.this, "Contacts not Updated. Please provide permission");
                }
                return;
            }

        }*/

            int i = 0;
            Intent intent = null;
            boolean per_flag = true;
            final String Permission_Denied;
            boolean showRationale = false;



            for (final String permission : permissions) {

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //  isPermissionSuccess=true;
                    i++;
                } else {
                    Permission_Denied = permission;
                    per_flag = false;

                    //isPermissionSuccess=false;
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BrightlyNavigationActivity.this, "Need permission " + Permission_Denied, Toast.LENGTH_LONG).show();
                    }
                });*/
                    showRationale = ActivityCompat.shouldShowRequestPermissionRationale(BrightlyNavigationActivity.this, Permission_Denied);
                    if (!showRationale) {
                        isPerReqCalled = true;
                        PermissionRequestedCode = requestCode;
                        intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BrightlyNavigationActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, requestCode);

                    }

                    break;
                    //       return;
                }
                if (per_flag) {
                    switch (requestCode) {
                        case PERMISSION_REQ_CODE_CONTACT:

                            LoadContact loadContact = new LoadContact();
                            loadContact.execute();

                            break;
                        case PERMISSION_REQ_CODE_AUDIO:
                            //   Toast.makeText(BrightlyNavigationActivity.this, "Permission Granted. Tab again to record audio", Toast.LENGTH_LONG).show();
                            break;
                        case PERMISSION_REQ_CODE_IMAGE:
                            // Toast.makeText(BrightlyNavigationActivity.this, "Permission Granted. Tab again to upload image", Toast.LENGTH_LONG).show();
                            permissionResultInterfaceObj.onPermissionResult_rcd(null);
                            break;
                    }
                } else {
                    PermissionUtil.hasPermission(permissions, BrightlyNavigationActivity.this, requestCode);
                }
            }

        }
//        PermissionUtil.checkPermissionResult(requestCode,permissions,grantResults);
    }


    public void setUserModel(RealmModel userMode) {
        user_obj = userMode;
    }


    public RealmModel getUserModel() {
        return user_obj;
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK) {
            Toast.makeText(BrightlyNavigationActivity.this,"Result ok",Toast.LENGTH_LONG).show();
            if (requestCode == PERMISSION_REQ_CODE_AUDIO) {

            }
            if (requestCode == PERMISSION_REQ_CODE_IMAGE) {

            }
            if (requestCode == PERMISSION_REQ_CODE_CONTACT) {

            }
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
              /*
                Toast.makeText(BrightlyNavigationActivity.this,"Welcome",Toast.LENGTH_LONG).show();*/
                //  getSupportFragmentManager().popBackStackImmediate();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onFragmentBackKeyHandler(boolean DisableHomeBtn) {
        if (DisableHomeBtn) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);


        }

        getSupportFragmentManager().popBackStackImmediate();

    }

    /**
     * Removing back stack multiple manner acheive by counts given on PopBackStackCount Parameter
     *
     * @param DisableHomeBtn
     * @param PopBackStackCount
     */
    public void onFragmentBackKeyHandler(boolean DisableHomeBtn, int PopBackStackCount) {
        for (int i = 0; i < PopBackStackCount; i++) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        if (DisableHomeBtn) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);


        }

    }

    public void onFragmentCall(String tag, Fragment call_fragment, boolean enableHomeBtn) {
        if (enableHomeBtn) {
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DisableBackBtn) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                        toggle.setDrawerIndicatorEnabled(true);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }

                    onBackPressed();
                }
            });
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } else {
            // Remove hamburger
//            toggle.setDrawerIndicatorEnabled(true);
//             Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if (toggle != null)
                toggle.setDrawerIndicatorEnabled(true);
            if (drawer != null)
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
        if (isHide_frag) {
            isHide_frag = false;
            if (call_fragment instanceof ChannelFragment || call_fragment instanceof EditSetInfo || call_fragment instanceof CardList) {
                Fragment card_dtl_frag = fragmentManager.findFragmentByTag(Util.view_card);
                transaction.hide(card_dtl_frag);
                transaction.addToBackStack(tag);
                transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
                transaction.add(R.id.frag_container, call_fragment, tag).commit();
            } else if (call_fragment instanceof SharePage) {
                Fragment toHide_frag = fragmentManager.findFragmentByTag(Util.Edit_Set);
                transaction.hide(toHide_frag);
                transaction.addToBackStack(tag);
                transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
                transaction.add(R.id.frag_container, call_fragment, tag).commit();
            } else {
                Fragment toHide_frag = fragmentManager.findFragmentByTag(Util.share_page);
                transaction.hide(toHide_frag);
                transaction.addToBackStack(tag);
                transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
                transaction.add(R.id.frag_container, call_fragment, tag).commit();
            }

        } else {
            transaction

                    .addToBackStack(tag)
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, call_fragment,
                            tag).commit();
        }
    }

    @Override
    public void postive_btn_clicked() {

    }

    @Override
    public void negative_btn_clicked() {

    }


    @SuppressLint("StaticFieldLeak")
    class LoadContact extends AsyncTask<Void, Void, Void> {

        ArrayList<String> list_temp_phone_no;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list_temp_phone_no = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            ContentResolver contentResolver = getContentResolver();
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

                        SharedPreferences mPrefs = getSharedPreferences("contactShares", MODE_PRIVATE);
//                        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyChannel.this);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(contactShares);
                        prefsEditor.putString("contactShares", json);
                        prefsEditor.commit();

                    }
                } while (cursor.moveToNext());
                cursor.close();
                if (permissionResultInterfaceObj != null)
                    permissionResultInterfaceObj.onPermissionResult_rcd(contactShares);

            }
//                cursor.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each : all) if (!set.add(each)) return true;
        return false;
    }

    public interface PermissionResultInterface {
        public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list);
    }
}
