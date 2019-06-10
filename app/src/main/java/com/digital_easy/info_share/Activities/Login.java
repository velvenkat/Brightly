package com.digital_easy.info_share.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.digital_easy.info_share.Fragments.LoginFragment;
import com.digital_easy.info_share.Modules.AppVarModule;
import com.digital_easy.info_share.Modules.ContactShare;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.PermissionUtil;
import com.digital_easy.info_share.Utils.Util;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentManager = getSupportFragmentManager();
        AppVarModule appVarModuleObj = getIntent().getParcelableExtra("app_var_obj");
        // If savedinstnacestate is null then replace login fragment
        bundle = new Bundle();
        bundle.putParcelable("app_var_obj", appVarModuleObj);
        LoginFragment loginFrag_args = new LoginFragment();
        loginFrag_args.setArguments(bundle);
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()

                    .replace(R.id.frameContainer, loginFrag_args,
                            Util.Login_Fragment).commit();
        }
    }

    // Replace Login Fragment with animation
    public interface PermissionResultInterface {
        public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list);
    }

    public interface OTPListener {
        public void otpReceived(String message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(Login.this, Permission_Denied);
                if (!showRationale) {

                  /*  intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", SplashScreen.this.getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, requestCode);*/

                }

                break;
                //       return;
            }
            if (per_flag) {
                switch (requestCode) {
                    case 100:


                        break;

                }
            } else {
                PermissionUtil.hasPermission(permissions, Login.this, requestCode);
            }
        }


    }

}
