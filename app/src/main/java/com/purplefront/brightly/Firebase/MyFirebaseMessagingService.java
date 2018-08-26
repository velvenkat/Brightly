package com.purplefront.brightly.Firebase;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;
import java.util.List;



import static android.app.Notification.DEFAULT_VIBRATE;


/**
 * Created by pf-05 on 10/16/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    private static final String TAG = "MyFirebaseMsgService";

    String message_body = "", message_title = "", control_unit_sim_number = "" , base_unit_id = "";

    public static MediaPlayer media_player_object;






    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);


        message_body = remoteMessage.getData().get("body");
        message_title = remoteMessage.getData().get("title");
        control_unit_sim_number = remoteMessage.getData().get("tag");
        base_unit_id = remoteMessage.getData().get("unit_id");

        if(!onAppRunningonBackground(this))
        {
            Log.d("JDKDNKK","JDKDNKK");

            if(remoteMessage.getData().get("body").startsWith("Warning"))
            {
                Log.d("RYEMNDR","RYEMNDR");
                Intent intent = new Intent(this, NotificationHandler.class);
                intent.putExtra("message_body",remoteMessage.getData().get("body"));
                intent.putExtra("control_unit_sim_number",remoteMessage.getData().get("tag"));
                intent.putExtra("base_unit_id",remoteMessage.getData().get("unit_id"));
                intent.putExtra("title_name",remoteMessage.getData().get("title"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);


                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


           /*     MediaPlayer defaultSoundUri = MediaPlayer.create(this, R.raw.siren);
                defaultSoundUri.setLooping(true);
                defaultSoundUri.start();


                media_player_object = defaultSoundUri;
*/



                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                        .setContentTitle(message_title)
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setDefaults(DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent);


                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());


            }
            else
            {
                Log.d("RIDACKDFD","RIDACKDFD");
                Intent intent = new Intent(this, NotificationHandler.class);
                intent.putExtra("message_body",remoteMessage.getData().get("body"));
                intent.putExtra("control_unit_sim_number",remoteMessage.getData().get("tag"));
                intent.putExtra("base_unit_id",remoteMessage.getData().get("unit_id"));
                intent.putExtra("title_name",remoteMessage.getData().get("title"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(message_title)
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

            }

//            Intent lIntent=new Intent(MedConstants.CONFIG_PUSHNOTIFICATION);
//            lIntent.putExtra("title",remoteMessage.getNotification().getTitle());
//            lIntent.putExtra("body",remoteMessage.getNotification().getBody());
//            LocalBroadcastManager.getInstance(this).sendBroadcast(lIntent);
//            return;






            return;

        }
        else
        {
            if(remoteMessage.getData().get("body").startsWith("Warning"))
            {
                Log.d("SERESER","SERESER");
                Intent intent = new Intent(this, NotificationHandler.class);
                intent.putExtra("message_body",remoteMessage.getData().get("body"));
                intent.putExtra("control_unit_sim_number",remoteMessage.getData().get("tag"));
                intent.putExtra("base_unit_id",remoteMessage.getData().get("unit_id"));
                intent.putExtra("title_name",remoteMessage.getData().get("title"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);


                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

/*

                MediaPlayer defaultSoundUri = MediaPlayer.create(this, R.raw.siren);
                defaultSoundUri.setLooping(true);
                defaultSoundUri.start();


                media_player_object = defaultSoundUri;


*/


                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(message_title)
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setDefaults(DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent);


                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());


            }
            else
            {
                Log.d("DAFARE","DAFARE");
                Intent intent = new Intent(this, NotificationHandler.class);

                intent.putExtra("message_body",remoteMessage.getData().get("body"));
                intent.putExtra("control_unit_sim_number",remoteMessage.getData().get("tag"));
                intent.putExtra("base_unit_id",remoteMessage.getData().get("unit_id"));
                intent.putExtra("title_name",remoteMessage.getData().get("title"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(message_title)
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

            }

            Log.d("ONBCKGRUND","ONBCKGRUND");
        }





        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
            Log.d("SJQERE","SJQERE"+remoteMessage.getData());

            Log.d("REMSIZE","REMSIZE"+remoteMessage.getData().toString());
            String storeDAta = remoteMessage.getData().toString();
            Log.d("STODATA","STODATA"+storeDAta);

            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.d("JSOMREAS","JSOMREAS"+json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            //  Log.d("MSGTITLE","MSGTITLE"+remoteMessage.getNotification().getTitle());
            //  Log.d("MDJKS","MDJKS"+remoteMessage.getNotification().getTag());
            sendNotification(remoteMessage.getData().get("body"));

            // Log.d("OMDNEARD","OMDNEARD"+remoteMessage.getNotification().getBody());

            message_body = remoteMessage.getData().get("body");

            message_title = remoteMessage.getData().get("title");


            // Create object of SharedPreferences.
            SharedPreferences sharedPref = getSharedPreferences("mypref", 0);
            //now get Editor
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("control_unit_no", remoteMessage.getData().get("tag"));
            editor.commit();

            Log.d("JSKDN","JSKDN"+message_body);


        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */





    private void sendNotification(String messageBody) {

        Log.d("NOTIBODY","NOTIBODY"+messageBody);

        if(messageBody.startsWith("Hi"))
        {

            Intent intent = new Intent(this, NotificationHandler.class);
            Log.d("MNSDKL","MNSDKL"+messageBody);
            Log.d("PASSMSG","PASSMSG"+messageBody);
            intent.putExtra("message_body",messageBody);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
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

            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
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
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);



/*

            MediaPlayer defaultSoundUri = MediaPlayer.create(this, R.raw.siren);
            defaultSoundUri.setLooping(true);
            defaultSoundUri.start();


            media_player_object = defaultSoundUri;

*/



            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(message_title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setDefaults(DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

        }



    }



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