package com.purplefront.brightly;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.AppVarModule;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

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
    AppVarModule appVarModuleObj;
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
            isNotification = getIntent().getBooleanExtra("isNotification", false);
            isRevoked = getIntent().getBooleanExtra("isRevoked", false);

        }
        if (isNotification) {
            isCardNotification = getIntent().getBooleanExtra("isCardNotification", false);
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
                                    intent.putExtra("app_var_obj", appVarModuleObj);
                                    if (isNotification) {
                                        intent.putExtra("isNotification", true);
                                        intent.putExtra("isRevoked", isRevoked);
                                        intent.putExtra("notfy_modl_obj", nfy_obj);

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
                    Toast.makeText(SplashScreen.this, message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
