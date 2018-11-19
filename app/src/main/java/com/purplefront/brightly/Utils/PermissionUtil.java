package com.purplefront.brightly.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niranjan Reddy on 28-02-2018.
 */

public class PermissionUtil {

    static Context mContext;
    public static int PERMISSION_REQ_CODE;

   /* public static  enum ReqPermission{
        IMAGE,AUDIO,CONTACT
    }*/


    public static boolean hasPermission(String [] Per_List,Context contextObj,int PermissionReqCode) {
        PERMISSION_REQ_CODE=PermissionReqCode;
        boolean per_flag = true;
        mContext = contextObj;
        List<String> permission_list=new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M  && Per_List != null) {
            //  per_flag = true;
            for (String permission : Per_List) {

                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {

                    per_flag = false;
                    //isPermissionSuccess=false;
                    permission_list.add(permission);
                }
            }
        }
        /*if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                permission)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{permission},
                    PERMISSION_REQ_CODE);

        }*/
                 if(!per_flag)
                 {
                        ActivityCompat.requestPermissions((Activity) mContext,
                                permission_list.toArray(new String[permission_list.size()]),
                                PERMISSION_REQ_CODE);
                    }


                //i++;



            /*if(isPermissionSuccess && (i==Per_List.length)){

                screen_Handler();
            }*/



        if (per_flag) {

            return true;
        }

    return false;
    }
    public static Intent checkPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        boolean per_flag=true;
        int i=0;
        Intent intent = null;
        final String Permission_Denied;
        boolean showRationale=false;
        for (final String permission:permissions){

            if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                //  isPermissionSuccess=true;
                i++;
            }
            else{
                Permission_Denied=permission;
                per_flag=false;
                //isPermissionSuccess=false;
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"Need permission "+Permission_Denied, Toast.LENGTH_LONG).show();
                    }
                });
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext, Permission_Denied );
                if (! showRationale) {
                     intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                    intent.setData(uri);
                    ((Activity)mContext).startActivityForResult(intent, PERMISSION_REQ_CODE);
                }

                break;
                //       return;
            }

        }
        if(per_flag){
            return  null;
        }
        else{
            return intent;
        }

    }
}
