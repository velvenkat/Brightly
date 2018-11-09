package com.purplefront.brightly.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 *
 * =================================================================================================
 *
 * <t>CheckNetworkConnection</t> purpose is to check availability of internet connection
 *
 * =================================================================================================
 *
 * Class consists of single class level method <t>isOnline</t> checks the connection
 * and gives back the result
 *      isOnline required parameters
 *      <tt>Context</tt>
 *      returns result boolean values
 *
 */
public class CheckNetworkConnection {

    /**
     * @param context
     * @return boolean
     *
     */
    public static boolean isOnline(Context context) {

        /*Check for class instance availability*/
        if (context == null)
            return false;

        try {
        /*Factory method of settting to get Connection services*/
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        /*Collects the network infromation*/
            NetworkInfo netInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            if (netInfo == null || !netInfo.isConnected()){
                return false;
            }
            else if (netInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return true;
            }
            else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                int networkType = netInfo.getSubtype();
                switch (networkType){
                    case TelephonyManager.NETWORK_TYPE_LTE:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:

                        return true;
                    default:
                        return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;



    }
}
