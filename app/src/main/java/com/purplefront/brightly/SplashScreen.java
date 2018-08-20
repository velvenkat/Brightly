package com.purplefront.brightly;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.Activities.MyChannel;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Utils.SharedPrefUtils;
import com.purplefront.brightly.Utils.Util;

import io.realm.Realm;
import io.realm.RealmResults;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    String User_ID;
    String UserName;
    String User_CompanyName;
    String User_Email;
    String phoneNumber;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        realm = Realm.getDefaultInstance();
//        userId = SharedPrefUtils.getString(MyChannel.this, Util.User_Id, "");

        RealmResults<RealmModel> realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for(RealmModel model:realmModel){
            User_ID = model.getUser_Id();
        }

        //User_ID = SharedPrefUtils.getString(SplashScreen.this, Util.User_Id, "");


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (User_ID != null && !User_ID.isEmpty()){
                    Intent intent = new Intent(SplashScreen.this, MyChannel.class);
                    SplashScreen.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }
                else
                {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    SplashScreen.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
