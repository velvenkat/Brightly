package com.digital_easy.info_share.OTPService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.digital_easy.info_share.Activities.Login;


public class OTPReaderCustom extends BroadcastReceiver {


    /**
     * Constant TAG for logging key.
     */
    private static final String TAG = "OtpReader";

    /**
     * The bound OTP Listener that will be trigerred on receiving message.
     */
    private static Login.OTPListener otpListener;

    /**
     * The Sender number string.
     */
    private static String receiverString;

    /**
     * Binds the sender string and listener for callback.
     *
     * @param listener
     * @param sender
     */
    public static void bind(Login.OTPListener listener, String sender) {
        otpListener = listener;
        receiverString = sender;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {

            final Object[] pdusArr = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusArr.length; i++) {

                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusArr[i]);
                String senderNum = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                Log.i(TAG, "senderNum: " + senderNum + " message: " + message);

                if (!TextUtils.isEmpty(receiverString) && senderNum.contains(receiverString)) { //If message received is from required number.
                    //If bound a listener interface, callback the overriden method.
                    if (otpListener != null) {
                        otpListener.otpReceived(message);
                    }
                }
            }
        }
    }
}


