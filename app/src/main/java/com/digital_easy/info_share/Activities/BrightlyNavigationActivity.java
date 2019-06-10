package com.digital_easy.info_share.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digital_easy.info_share.Firebase.AlarmBroadcastReceiver;
import com.digital_easy.info_share.Firebase.VoiceCommandService;
import com.digital_easy.info_share.Fragments.CreateChannelFragment;
import com.digital_easy.info_share.Fragments.Dashboard;
import com.digital_easy.info_share.Fragments.ListModules;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.BadgeDrawable;
import com.digital_easy.info_share.Fragments.CardDetailFragment;
import com.digital_easy.info_share.Fragments.CardList;
import com.digital_easy.info_share.Fragments.CommentsFragment;
import com.digital_easy.info_share.Fragments.EditSetInfo;
import com.digital_easy.info_share.Fragments.ImageType;
import com.digital_easy.info_share.Fragments.MyProfile;
import com.digital_easy.info_share.Fragments.Notifications;
import com.digital_easy.info_share.Fragments.SetPagerFragment;
import com.digital_easy.info_share.Fragments.SharePage;
import com.digital_easy.info_share.Fragments.ShareSettings;
import com.digital_easy.info_share.Fragments.ViewPager_Category;
import com.digital_easy.info_share.Modules.AddMessageResponse;
import com.digital_easy.info_share.Modules.AppVarModule;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.ContactShare;
import com.digital_easy.info_share.Modules.GeneralVarModel;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.NotificationsResponse;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import com.digital_easy.info_share.Utils.PermissionUtil;
import com.digital_easy.info_share.Utils.Util;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class BrightlyNavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseActivity.alert_dlg_interface {

    private int mBindFlag;
    private Messenger mServiceMessenger;
    private static final String SHOWCASE_ID = "1";
    public PermissionResultInterface permissionResultInterfaceObj;
    ArrayList<ContactShare> contactShares = new ArrayList<>();
    private static final int REQUEST_PERMISSION = 1;
    ContactShare contacts;
    public boolean isAppOpenByGallery;
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
    public String SET_SINGULAR = "";
    public String SET_PLURAL = "";
    public String CATEGORY_SINGULAR = "";
    public String SET_DEF_IMAGE = "";
    public String CARD_DEF_IMAGE = "";
    public String CARD_AUDIO_IMAGE = "";
    public String CARD_FILE_IMAGE = "";
    public String CARD_UTUBE_IMAGE = "";
    public String CATG_DEF_IMAGE = "";
    public String CATEGORY_PLURAL = "";
    public String CARD_SINGULAR = "";
    public String CARD_PLURAL = "";
    public String HAVING_COMMENT_IMG = "";
    public String NO_COMMENT_IMG = "";
    public String SHARE_DEF_IMAGE = "";
    public String SHARE_SETTING_IMG = "";
    RealmResults<RealmModel> realmModel;

    public String userId;
    String userName;
    String userPhone;
    String userPicture;
    String type = "all";
    String Title = "All Categories";
    String count = "0";
    int PerCount = -1;
    String deviceToken;
    boolean isAudioMatch;
    public boolean DontRunOneTime = false;
    boolean isNotification = false;
    View target_menu;

    TextView txtVersion;
    boolean isCardNotification = false;
    public boolean isUTubePlayerFullScreen = false;
    public NavigationView navigationView;
    public TextView headerText_Name;
    public TextView headerText_Phone;
    public SimpleDraweeView headerImage_Profile;
    public boolean isCardClicked = false;
    public int card_toPosition = 0;
    public YouTubePlayer uTubePlayer;
    public boolean isHide_frag = false;
    boolean isPerReqCalled = false;
    public AppVarModule appVarModuleObj;
    public ChannelListModel glob_chl_list_obj;
    public SetsListModel glob_set_list_obj;
    public String CARD_CREATE_IMAGE;
    String Card_ID;
    ArrayList<String> image_uri_list;

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
    NotificationsModel nfy_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        isNotification = getIntent().getBooleanExtra("isNotification", false);
        isCardNotification = getIntent().getBooleanExtra("isCardNotification", false);
        isRevoked = getIntent().getBooleanExtra("isRevoked", false);
        isAudioMatch = getIntent().getBooleanExtra("isAudioMatch", false);
        Card_ID = getIntent().getStringExtra("Card_id");
        image_uri_list = getIntent().getStringArrayListExtra("uri_list");

        if (PermissionUtil.hasPermission(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, this, 800)) {

        }

        Intent service = new Intent(BrightlyNavigationActivity.this, VoiceCommandService.class);
        startService(service);
        mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;


        Intent alarm = new Intent(this, AlarmBroadcastReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        }
        //Toast.makeText(BrightlyNavigationActivity.this, "Card_id " + Card_ID, Toast.LENGTH_LONG).show();
        fragmentManager = getSupportFragmentManager();
        setDlgListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.my_channel);
        setSupportActionBar(toolbar);
        target_menu = (toolbar.findViewById(R.id.action_bell));
        txtVersion = (TextView) findViewById(R.id.txtVersion);
        PackageInfo pInfo = null;
        String version = "";
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        txtVersion.setText("Version:" + pInfo.versionName);
        setActionBarTitle(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        contactShares = new ArrayList<>();

        user_obj = getIntent().getParcelableExtra("user_obj");
        appVarModuleObj = getIntent().getParcelableExtra("app_var_obj");

        if (user_obj == null) {
            realm = Realm.getDefaultInstance();
            realmModel = realm.where(RealmModel.class).findAllAsync();
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
        } else {
            userId = user_obj.getUser_Id();
            userName = user_obj.getUser_Name();
            userPhone = user_obj.getUser_PhoneNumber();
            userPicture = user_obj.getImage();
            deviceToken = user_obj.getDeviceToken();

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

        nfy_model = getIntent().getParcelableExtra("notfy_modl_obj");


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
        headerImage_Profile = (SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.image_profile);

        set_prof_hdr_image(userPicture);


        //getChannelsLists();
//        MaterialShowcaseView.resetSingleUse(BrightlyNavigationActivity.this, SHOWCASE_ID);
//        ShowcaseSingle();
        //MultipleShowcase();
        View drawerIcon = toolbar.getChildAt(2);
        Map<String, Object> d = new HashMap<>();
        d.put("view", target_menu);
        d.put("cont_txt", "Here all types of Shared Set,Cards Notifications shown");
        d.put("dis_txt", "GOT IT");
        ArrayList<Map<String, Object>> multipleShowCaseList = new ArrayList<>();
        multipleShowCaseList.add(d);

        d = new HashMap<>();
        d.put("view", drawerIcon);
        d.put("cont_txt", "Click here and you will get options to navigate to other sections.");
        d.put("dis_txt", "GOT IT");
        multipleShowCaseList.add(d);


        MultipleShowcase(multipleShowCaseList);
        if (appVarModuleObj != null) {
            setDynamicValues();
        } else
            getAppVar();


    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, VoiceCommandService.class), mServiceConnection, mBindFlag);
    }

    @Override
    protected void onStop() {
        super.onStop();

       /* if (mServiceMessenger != null) {
            unbindService(mServiceConnection);
            mServiceMessenger = null;
        }*/
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // if (DEBUG) {Log.d(TAG, "onServiceConnected");} //$NON-NLS-1$

            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
            msg.what = VoiceCommandService.MSG_RECOGNIZER_START_LISTENING;

            try {
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // if (DEBUG) {Log.d(TAG, "onServiceDisconnected");} //$NON-NLS-1$
            mServiceMessenger = null;
        }

    };

    public void setDynamicValues() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        SET_PLURAL = appVarModuleObj.getLevel2title().getPlural();
        SET_SINGULAR = appVarModuleObj.getLevel2title().getSingular();
        CARD_CREATE_IMAGE = appVarModuleObj.getLevel3_default_create_image().getFetch_key();
        CATEGORY_PLURAL = appVarModuleObj.getLevel1title().getPlural();
        CATEGORY_SINGULAR = appVarModuleObj.getLevel1title().getSingular();
        CARD_PLURAL = appVarModuleObj.getLevel3title().getPlural();
        SHARE_DEF_IMAGE = appVarModuleObj.getSharepic().getFetch_key();
        SHARE_SETTING_IMG = appVarModuleObj.getShare_setting_img().getFetch_key();
        CARD_SINGULAR = appVarModuleObj.getLevel3title().getSingular();
        SET_DEF_IMAGE = appVarModuleObj.getLevel2_default_img().getFetch_key();
        CARD_DEF_IMAGE = appVarModuleObj.getLevel3_default_img().getFetch_key();
        CARD_AUDIO_IMAGE = appVarModuleObj.getLevel3_audio_img().getFetch_key();
        CARD_UTUBE_IMAGE = appVarModuleObj.getLevel3_youtube_img().getFetch_key();
        CARD_FILE_IMAGE = appVarModuleObj.getLevel3_file_img().getFetch_key();
        CATG_DEF_IMAGE = appVarModuleObj.getLevel1_default_img().getFetch_key();
        HAVING_COMMENT_IMG = appVarModuleObj.getComment_img().getFetch_key();
        NO_COMMENT_IMG = appVarModuleObj.getUncomment_img().getFetch_key();

        Menu Navigation_Menu = navigationView.getMenu();

        List<GeneralVarModel> menu_var_obj = appVarModuleObj.getMenu();
        int i = 0;
        int group_id = 111;

        for (GeneralVarModel menu_Items : menu_var_obj) {
            String Title = !menu_Items.getPlural().equals("") ? menu_Items.getPlural() : menu_Items.getSingular();
            Navigation_Menu.add(group_id, 1001 + i, 100 + i, Title);
            i++;
            if (i == 2) {
                group_id++;
            }
        }
        group_id++;
       /* Navigation_Menu.add(group_id, 1110, 100, "Admin");
        Navigation_Menu.addSub(group_id, 1111, 150, "Custom model");*/
/*
        Navigation_Menu.addSubMenu(Menu.NONE, 1110, Menu.NONE, "Admin");

        SubMenu themeMenu = Navigation_Menu.findItem(1110).getSubMenu();

        themeMenu.clear();
        themeMenu.add(0, 1450, Menu.NONE, "Custom Model");
*/


        if (isNotification) {

            if (isCardNotification) {

                Fragment card_frag = new CardDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNotification", true);
                bundle.putParcelable("notfy_modl_obj", nfy_model);
                card_frag.setArguments(bundle);
                onFragmentCall(Util.view_card, card_frag, false);
            } else if (nfy_model.getAction().equals("comment")) {
                Fragment fragment = new CommentsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", user_obj.getUser_Id());
                bundle.putParcelable("notfy_modl_obj", nfy_model);
                bundle.putBoolean("isNotification", true);
                fragment.setArguments(bundle);
                onFragmentCall(Util.Comments, fragment, false);
            } else {
                Fragment nfy_frag = new Notifications();
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new Notifications(),
                            Util.NOTIFICATIONS).commit();
*/
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFireBaseNfy", true);
                nfy_frag.setArguments(bundle);
                onFragmentCall(Util.NOTIFICATIONS, nfy_frag, false);
            }
            // setActionBarTitle("Notification");
            // frag_container.setVisibility(View.VISIBLE);

        } else {
            if (image_uri_list == null) {
                if (isAudioMatch) {
                    Bundle bundle = new Bundle();
                    //  bundle.putString("type", type);
                    Fragment chl_frag = new CreateChannelFragment();
                    chl_frag.setArguments(bundle);
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();

            //  setActionBarTitle("Notification");
            frag_container.setVisibility(View.VISIBLE);*/
                    onFragmentCall(Util.Create_Channel, chl_frag, false);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    Fragment chl_frag = new Dashboard();
                    chl_frag.setArguments(bundle);
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();

            //  setActionBarTitle("Notification");
            frag_container.setVisibility(View.VISIBLE);*/
                    onFragmentCall(Util.DASHBOARD, chl_frag, false);
                }
            } else {
                Bundle bundle = new Bundle();

                Fragment frag = new ImageType();
                bundle.putBoolean("isOpenFromGallery", true);
                bundle.putStringArrayList("uri_list", image_uri_list);
                frag.setArguments(bundle);
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();

            //  setActionBarTitle("Notification");
            frag_container.setVisibility(View.VISIBLE);*/
                onFragmentCall(Util.CHANNELS, frag, false);
            }
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void set_prof_hdr_image(String imgUrl) {
        ResizeOptions mResizeOptions = new ResizeOptions(75, 75);
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        String prof_img_url = null;
        if (imgUrl != null) {
            prof_img_url = imgUrl;
        } else {
            if (appVarModuleObj.getProf_default_img() != null)
                prof_img_url = appVarModuleObj.getProf_default_img().getFetch_key();
        }

        if (prof_img_url != null && !prof_img_url.trim().equals("")) {
            /*Glide.with(getApplicationContext())
                    .load(userPicture)
                    .centerCrop()
                    .transform(new CircleTransform(BrightlyNavigationActivity.this))
//                        .override(50, 50)
                    .into(headerImage_Profile);*/

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(this.getResources());
            builder.setProgressBarImage(R.drawable.loader);
            builder.setRoundingParams(roundingParams);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            headerImage_Profile.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(prof_img_url))
                            .setResizeOptions(mResizeOptions)

                            .build();
            headerImage_Profile.setImageRequest(imageRequest);


        } else {
/*
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_user_image)
                    .centerCrop()
                    .transform(new CircleTransform(BrightlyNavigationActivity.this))
                    *//*.override(50, 50)*//*
                    .into(headerImage_Profile);*/
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.default_user_image))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(mResizeOptions)
                            .build();
            headerImage_Profile.setImageRequest(imageRequest2);

        }

    }

    private void getAppVar() {

        if (CheckNetworkConnection.isOnline(this)) {

            Call<AppVarModule> callRegisterUser = RetrofitInterface.getRestApiMethods(this).getAppVariable();
            callRegisterUser.enqueue(new ApiCallback<AppVarModule>(this) {
                @Override
                public void onApiResponse(Response<AppVarModule> response, boolean isSuccess, String message) {
                    if (isSuccess) {
                        appVarModuleObj = response.body();
                        setDynamicValues();
                    } else {
                        Toast.makeText(BrightlyNavigationActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onApiFailure(boolean isSuccess, String message) {
                    Toast.makeText(BrightlyNavigationActivity.this, "Error Params missing v" + message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(null);
    }

    public void ShowcaseSingle(View target, String content_text, String dismiss_text) {


        // single example
        new MaterialShowcaseView.Builder(this)
                .setTarget(target)
                .setDismissText(dismiss_text)
                .setDismissTextColor(R.color.black)
                .setContentText(content_text)
                .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                .show();
    }

    private void MultipleShowcase(ArrayList<Map<String, Object>> target) {
// sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(1000); // half second between each showcase view
        config.setMaskColor(Color.parseColor("#AA000000"));
        config.setContentTextColor(Color.WHITE);
        config.setDismissTextColor(Color.CYAN);

       /* Map<String, Object> c = new HashMap<>();
        c.put("view", target_menu);
        c.put("cont_txt", content_text);
        c.put("dis_txt", dismiss_text);
        target_menu = (View) c.get("view");
        dismiss_text = (String) c.get("cont_txt");*/

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setConfig(config);
        for (int i = 0; i < target.size(); i++) {
            Map<String, Object> showCaseMap = target.get(i);
            sequence.addSequenceItem((View) showCaseMap.get("view"),
                    (String) showCaseMap.get("cont_txt"), (String) showCaseMap.get("dis_txt"));
        }
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
                    Toast.makeText(BrightlyNavigationActivity.this, "Tab again to upload the image", Toast.LENGTH_LONG).show();
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
        String url = "http://brightlyapp.com/download.php";
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

    /*@Override
    public boolean onNavigationItemSelected(MenuItem item){

      return true;
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean isChannelScrn = false;
        /*else if (id == R.id.nav_list_all_catg) {

            onFragmentCall(Util.List_All_Catg, new ListAllCategory(), false);

        }*//* else if (id == R.id.nav_mysubscription) {

            type = "subscribe";
            isChannelScrn = true;
            //setActionBarTitle("Shared With Me");

        }*/
        if (id == 1001) {   //View By Category

            type = "all";
            isChannelScrn = true;
            //setActionBarTitle("My Channels");


        } else if (id == 1002) { //View by Set

            type = "all";
            isChannelScrn = false;
            Fragment set_frag = new SetPagerFragment();


            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();*/
            onFragmentCall(Util.Set_List, set_frag, false);

//            setActionBarTitle("All Channels");


        } else if (id == 1450) {  //Custom Model Screen

        } else if (id == 1003) {  //Profile Screen

            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new MyProfile(),
                            Util.My_Profile).commit();
*/
            MyProfile myProfile = new MyProfile();
            onFragmentCall(Util.My_Profile, myProfile, false);
            // Handle the camera action
        } else if (id == 1004) {   //Notification Screen

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


        } else if (id == 1005) { //Invite

            shareTextUrl();

        } else if (id == 1006) {  //About APP


            showLongToast(BrightlyNavigationActivity.this, "Comming Soon");

        } else if (id == 1007) {  //Logout
            showLogOutDialog();

        } else if (id == 1008) {  //Admin Settings
            Fragment fragment = new ListModules();


            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();*/
            onFragmentCall(Util.Admin_settings, fragment, false);

        }
        if (isChannelScrn) {
            Fragment fragment = new ViewPager_Category();

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

    public void showLogOutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BrightlyNavigationActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Logging Out....");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to Logout? ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


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
                            // finishIntent(BrightlyNavigationActivity.this, Login.class);
                            Intent intent = new Intent(BrightlyNavigationActivity.this, Login.class);
                            intent.putExtra("app_var_obj", appVarModuleObj);
                            startActivity(intent);
                            finish();
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
            if (call_fragment instanceof ViewPager_Category || call_fragment instanceof EditSetInfo || call_fragment instanceof ShareSettings || call_fragment instanceof CardList) {
                Fragment card_dtl_frag = fragmentManager.findFragmentByTag(Util.view_card);
                transaction.hide(card_dtl_frag);
                transaction.addToBackStack(tag);
                transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
                transaction.add(R.id.frag_container, call_fragment, tag).commit();
            } else if (call_fragment instanceof SharePage) {
                /*try {
                    Fragment toHide_frag = fragmentManager.findFragmentByTag(Util.Edit_Set);
                    transaction.hide(toHide_frag);
                    transaction.addToBackStack(tag);
                    transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
                    transaction.add(R.id.frag_container, call_fragment, tag).commit();
                } catch (Exception w) {*/
                Fragment toHide_frag = fragmentManager.findFragmentByTag(Util.Set_Share_settings);
                transaction.hide(toHide_frag);
                transaction.addToBackStack(tag);
                transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out);
                transaction.add(R.id.frag_container, call_fragment, tag).commit();
                // }
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
