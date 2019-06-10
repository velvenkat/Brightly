package com.digital_easy.info_share;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.Login;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.AppVarModule;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    String User_ID;
    String UserName;
    String User_CompanyName;
    String User_Email;
    String phoneNumber;
    Realm realm;
    ImageView logo;
    TextView logo_title;
    RealmModel model = null;
    boolean isNotification = false;
    boolean isCardNotification = false;
    NotificationsModel nfy_obj;
    boolean isRevoked;
    boolean isAudioRecorded;
    // boolean isOpenFromGallery;
    AppVarModule appVarModuleObj;
    ArrayList<String> image_uri_list = null;
    TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        realm = Realm.getDefaultInstance();
        logo = (ImageView) findViewById(R.id.logo);
        logo_title = (TextView) findViewById(R.id.logo_title);
        txtVersion = (TextView) findViewById(R.id.txtVersion);

//        userId = SharedPrefUtils.getString(MyChannel.this, Util.User_Id, "");


        if (getIntent() != null) {
            String action = getIntent().getAction();
            String type = getIntent().getType();
            isAudioRecorded = getIntent().getBooleanExtra("isAudioMatch", false);
            isNotification = getIntent().getBooleanExtra("isNotification", false);

            isRevoked = getIntent().getBooleanExtra("isRevoked", false);
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    //   handleSendText(intent); // Handle text being sent
                } else if (type.startsWith("image/")) {
//                    isOpenFromGallery = true;
                    handleSendImage(getIntent()); // Handle single image being sent
                }
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                if (type.startsWith("image/")) {
                    //                  isOpenFromGallery = true;
                    handleSendMultipleImages(getIntent()); // Handle multiple images being sent
                }
            } else {
                // Handle other intents, such as being started from the home screen
            }
        }
        if (isNotification) {
            isCardNotification = getIntent().getBooleanExtra("isCardNotification", false);
            String jsonStr = getIntent().getStringExtra("Card_id");
            //  Toast.makeText(SplashScreen.this, "Json Str:" + jsonStr, Toast.LENGTH_LONG).show();
            nfy_obj = getIntent().getParcelableExtra("notfy_modl_obj");
        }
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txtVersion.setText("Version:" + version);
        } catch (Exception e) {

        }


        RealmResults<RealmModel> realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();

        if (realmModel.size() == 1) {
            model = realmModel.get(0);
            User_ID = model.getUser_Id();
        }


        //User_ID = SharedPrefUtils.getString(SplashScreen.this, Util.User_Id, "");
        getAppVar();

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);
        logo_title.startAnimation(myanim);
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }


    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (imageUri != null) {
            // Update UI to reflect image being shared
            image_uri_list = new ArrayList<>();
            image_uri_list.add(imageUri.toString());
            Toast.makeText(SplashScreen.this, "Image URI:" + imageUri, Toast.LENGTH_LONG).show();
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            image_uri_list = new ArrayList<>();
            if (imageUris.size() <= 10) {
                for (Uri uri : imageUris) {
                    image_uri_list.add(uri.toString());
                }
                Toast.makeText(SplashScreen.this, "Image URI:" + imageUris.get(0), Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(SplashScreen.this, "Image limit is exceeded" + imageUris.get(0), Toast.LENGTH_LONG).show();

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
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                if (User_ID != null && !User_ID.isEmpty()) {


                                    Intent intent = new Intent(SplashScreen.this, BrightlyNavigationActivity.class);
                                    intent.putExtra("user_obj", model);
                                    //    Log.e("Coment_notify_user_id", model.getUser_Id());
                                    // Toast.makeText(SplashScreen.this,ge)
                                    intent.putExtra("app_var_obj", appVarModuleObj);
                                    intent.putExtra("isAudioMatch",isAudioRecorded);
                                    intent.putStringArrayListExtra("uri_list", image_uri_list);
                                    if (isNotification) {
                                        intent.putExtra("isNotification", true);
                                        intent.putExtra("isRevoked", isRevoked);
                                        intent.putExtra("notfy_modl_obj", nfy_obj);

                                        //    Log.e("Coment_notify_card_id", "Card_id" + nfy_obj.comment_card_id);


                                        if (isCardNotification) {
                                            intent.putExtra("isCardNotification", true);
                                        }

                                    }
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    SplashScreen.this.finish();
                                } else {
                                    Intent intent = new Intent(SplashScreen.this, Login.class);
                                    intent.putExtra("app_var_obj", appVarModuleObj);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    SplashScreen.this.finish();
                                }

                            }
                        }, SPLASH_TIME_OUT);
                    } else {
                        Toast.makeText(SplashScreen.this, message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onApiFailure(boolean isSuccess, String message) {
                    Toast.makeText(SplashScreen.this, "Error Params missing v" + message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
