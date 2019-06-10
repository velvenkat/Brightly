package com.digital_easy.info_share.Firebase;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class AlarmBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        /*Intent background = new Intent(context, VoiceCommandService.class);
        context.startService(background);
        int mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
        context.bindService(new Intent(context, VoiceCommandService.class), mServiceConnection, mBindFlag);
*/
        context.startService(new Intent(context, ConnctionIntentService.class));
    }


}
