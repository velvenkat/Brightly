package com.purplefront.brightly.Firebase;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pf-05 on 10/16/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    String refreshedToken;
    String androidDeviceId;
    private String tag_string_req_recieve2 = "string_req_recieve2";
    private static final String TAG = "MyFirebaseIIDService";
    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        androidDeviceId = Settings.Secure.getString(getApplicationContext().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    }

}