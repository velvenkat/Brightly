package com.digital_easy.info_share.Firebase;

<<<<<<< HEAD
=======
import android.app.ActivityManager;
>>>>>>> 58950c34046f03db5897e96b69e20658a7a83aa0
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
<<<<<<< HEAD
=======
import android.util.Log;
>>>>>>> 58950c34046f03db5897e96b69e20658a7a83aa0

public class ConnctionIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    Context mContext;
    private Messenger mServiceMessenger;

    public ConnctionIntentService() {
        super("ConnctionIntentService");
    }

    public ConnctionIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mContext = getApplicationContext();
        int mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
        //   mContext.bindService(new Intent(mContext, VoiceCommandService.class), mServiceConnection, mBindFlag);
        Intent service = new Intent(mContext, VoiceCommandService.class);
<<<<<<< HEAD
        startService(service);
        mContext.bindService(new Intent(mContext, VoiceCommandService.class), mServiceConnection, mBindFlag);
    }

=======
//        if (isMyServiceRunning(VoiceCommandService.class))
            startService(service);
        mContext.bindService(new Intent(mContext, VoiceCommandService.class), mServiceConnection, mBindFlag);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

>>>>>>> 58950c34046f03db5897e96b69e20658a7a83aa0
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // if (DEBUG) {Log.d(TAG, "onServiceConnected");} //$NON-NLS-1$

            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
            msg.what = VoiceCommandService.MSG_RECOGNIZER_START_LISTENING;

            try {
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // if (DEBUG) {Log.d(TAG, "onServiceDisconnected");} //$NON-NLS-1$
            mServiceMessenger = null;
        }

    };
}
