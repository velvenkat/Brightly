package com.purplefront.brightly.Firebase;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.purplefront.brightly.Activities.MyChannel;
import com.purplefront.brightly.Activities.MySetCards;
import com.purplefront.brightly.Fragments.Notifications;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.NotificationsSetDetail;
import com.purplefront.brightly.R;
import com.purplefront.brightly.SplashScreen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.List;



import static android.app.Notification.DEFAULT_VIBRATE;


/**
 * Created by pf-05 on 10/16/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    private static final String TAG = "MyFirebaseMsgService";

  //  String message_body = "", message_title = "", control_unit_sim_number = "" , base_unit_id = "";

    public static MediaPlayer media_player_object;
    Intent intent;





    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);


/*
        message_body = remoteMessage.getData().get("body");
        message_title = remoteMessage.getData().get("title");
        control_unit_sim_number = remoteMessage.getData().get("tag");
        base_unit_id = remoteMessage.getData().get("unit_id");
*/

       /* Intent remoteIntent=remoteMessage.toIntent();
        Bundle bundle=remoteIntent.getExtras();
        String extraStr=bundle.getString("gcm.notification.data");

        Gson gson = new Gson();

        NotificationsModel gsonObj = gson.fromJson(extraStr, NotificationsModel.class);*/

        String content=remoteMessage.getData().get("data");
        Gson gson = new Gson();

        NotificationsModel gsonObj = gson.fromJson(content, NotificationsModel.class);



        if (!onAppRunningonBackground(this)) {
            Log.d("JDKDNKK", "JDKDNKK");

            Log.d("RIDACKDFD", "RIDACKDFD");

            if((gsonObj.getType().equals("set")) && gsonObj.getAction().equals("deleted")) {

                intent = new Intent(this, MyChannel.class);
//                Toast.makeText(this, "This Set is Deleted...", Toast.LENGTH_SHORT).show();

            }
            else if((gsonObj.getType().equals("card")) && gsonObj.getAction().equals("deleted")) {

                intent = new Intent(this, MyChannel.class);
//                Toast.makeText(this, "This Card is Deleted...", Toast.LENGTH_SHORT).show();

            }
            else if(gsonObj.getAction().equals("revoked"))
            {
                intent = new Intent(this, MyChannel.class);
//                Toast.makeText(this, "The Set permission has been Revoked.", Toast.LENGTH_SHORT).show();
            }

            else
            {
                 intent = new Intent(this, MySetCards.class);

            }

           /* intent.putExtra("message_body", remoteMessage.getData().get("body"));
            intent.putExtra("control_unit_sim_number", remoteMessage.getData().get("tag"));
            intent.putExtra("base_unit_id", remoteMessage.getData().get("unit_id"));
            intent.putExtra("title_name", remoteMessage.getData().get("title"));*/
           intent.putExtra("isNotification",true);
          intent.putExtra("notfy_modl_obj",gsonObj);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setContentText(remoteMessage.getData().get("body"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());


//            Intent lIntent=new Intent(MedConstants.CONFIG_PUSHNOTIFICATION);
//            lIntent.putExtra("title",remoteMessage.getNotification().getTitle());
//            lIntent.putExtra("body",remoteMessage.getNotification().getBody());
//            LocalBroadcastManager.getInstance(this).sendBroadcast(lIntent);
//            return;


            return;

        } else {

            Log.d("DAFARE", "DAFARE");
            if((gsonObj.getType().equals("set")) && gsonObj.getAction().equals("deleted")) {

                intent = new Intent(this, MyChannel.class);
//                Toast.makeText(this, "This Set is Deleted...", Toast.LENGTH_SHORT).show();

            }
            else if((gsonObj.getType().equals("card")) && gsonObj.getAction().equals("deleted")) {

                intent = new Intent(this, MyChannel.class);
//                Toast.makeText(this, "This Card is Deleted...", Toast.LENGTH_SHORT).show();

            }
            else if(gsonObj.getAction().equals("revoked"))
            {
                intent = new Intent(this, MyChannel.class);
//                Toast.makeText(this, "The Set permission has been Revoked.", Toast.LENGTH_SHORT).show();
            }

            else
            {
                intent = new Intent(this, MySetCards.class);

            }

         /*   intent.putExtra("message_body", remoteMessage.getData().get("body"));
            intent.putExtra("control_unit_sim_number", remoteMessage.getData().get("tag"));
            intent.putExtra("base_unit_id", remoteMessage.getData().get("unit_id"));
            intent.putExtra("title_name", remoteMessage.getData().get("title"));*/
            intent.putExtra("isNotification",true);
           intent.putExtra("notfy_modl_obj",gsonObj);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setContentText(remoteMessage.getData().get("body"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());


            Log.d("ONBCKGRUND", "ONBCKGRUND");
        }


    }






   /* private void sendNotification(String messageBody) {

        Log.d("NOTIBODY","NOTIBODY"+messageBody);

        if(messageBody.startsWith("Hi"))
        {

            Intent intent = new Intent(this, NotificationHandler.class);
            Log.d("MNSDKL","MNSDKL"+messageBody);
            Log.d("PASSMSG","PASSMSG"+messageBody);
            intent.putExtra("message_body",messageBody);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 *//* Request code *//*, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(message_title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 *//* ID of notification *//*, notificationBuilder.build());
        }
        else if(messageBody.startsWith("Warning"))
        {

            Log.d("DIREDAR","DIREDAR");

            // AlaramNotification.isgasNotificationAttendended = false;
            Intent intent = new Intent(this, NotificationHandler.class);
            Log.d("MNSDKL","MNSDKL"+messageBody);
            Log.d("PASSMSG","PASSMSG"+messageBody);

            intent.putExtra("message_body",messageBody);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 *//* Request code *//*, intent,
                    PendingIntent.FLAG_ONE_SHOT);



*//*

            MediaPlayer defaultSoundUri = MediaPlayer.create(this, R.raw.siren);
            defaultSoundUri.setLooping(true);
            defaultSoundUri.start();


            media_player_object = defaultSoundUri;

*//*



            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(message_title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setDefaults(DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 *//* ID of notification *//*, notificationBuilder.build());

        }



    }*/



    // Check the application running in Background


    public static boolean onAppRunningonBackground(Context pContext)
    {
        boolean lIsAppIsBackground=true;
        ActivityManager lActivityManager=(ActivityManager)pContext.getSystemService(Context.ACTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH)
        {
            List<ActivityManager.RunningAppProcessInfo> lRunningAppProcess=lActivityManager.getRunningAppProcesses();
            for(ActivityManager.RunningAppProcessInfo runnAppProcess : lRunningAppProcess)
            {
                if(runnAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                {
                    for(String lPackgeFromAppsList : runnAppProcess.pkgList)
                    {
                        if(lPackgeFromAppsList.equals(pContext.getPackageName()))
                        {
                            lIsAppIsBackground=false;
                        }
                    }
                }
            }
        }
        else
        {
            List<ActivityManager.RunningTaskInfo> lRunningTaskInfo=lActivityManager.getRunningTasks(1);
            ComponentName lComponentName=lRunningTaskInfo.get(0).topActivity;
            if(lComponentName.getPackageName().equals(pContext.getPackageName()))
            {
                lIsAppIsBackground=false;
            }
        }
        return lIsAppIsBackground;
    }

}