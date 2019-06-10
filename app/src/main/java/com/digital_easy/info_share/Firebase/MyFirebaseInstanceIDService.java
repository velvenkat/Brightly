package com.digital_easy.info_share.Firebase;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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