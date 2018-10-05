package com.purplefront.brightly;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Utils.SharedPrefUtils;
import com.purplefront.brightly.Utils.Util;

import io.realm.Realm;
import io.realm.RealmResults;

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
    boolean isNotification=false;
    boolean isCardNotification=false;
    NotificationsModel nfy_obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        realm = Realm.getDefaultInstance();
        logo=(ImageView)findViewById(R.id.logo);
        logo_title=(TextView) findViewById(R.id.logo_title);
//        userId = SharedPrefUtils.getString(MyChannel.this, Util.User_Id, "");

        if(getIntent()!=null){
            isNotification=getIntent().getBooleanExtra("isNotification",false);

        }
        if(isNotification){
            isCardNotification=getIntent().getBooleanExtra("isCardNotification",false);
            nfy_obj=getIntent().getParcelableExtra("notfy_modl_obj");
        }

        RealmResults<RealmModel> realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();

        if(realmModel.size()==1){
            model=realmModel.get(0);
          User_ID= model.getUser_Id();
        }


        //User_ID = SharedPrefUtils.getString(SplashScreen.this, Util.User_Id, "");


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (User_ID != null && !User_ID.isEmpty()){


                    Intent intent = new Intent(SplashScreen.this, BrightlyNavigationActivity.class);
                    intent.putExtra("user_obj",model);
                    if(isNotification){
                        intent.putExtra("isNotification",true);
                        intent.putExtra("notfy_modl_obj",nfy_obj);
                        if(isCardNotification){
                         intent.putExtra("isCardNotification",true);
                        }

                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    SplashScreen.this.finish();
                }
                else
                {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    SplashScreen.this.finish();
                }

            }
        }, SPLASH_TIME_OUT);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo_title.startAnimation(myanim);
    }


}
