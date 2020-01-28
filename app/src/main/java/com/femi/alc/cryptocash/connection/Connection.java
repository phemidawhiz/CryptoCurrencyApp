package com.franklyn.alc.cryptocash.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

/**
 * Created by AGBOMA franklyn on 22/10/17.
 */
public class Connection {


    private static final String LOG_TAG = Connection.class.getSimpleName();

    private static final int TYPE_MOBILE = 1;
    private static final int TYPE_WIFI = 2;
    private static final int TYPE_NONE = 0;

    public static int getConnectionState(Context context) {

        ConnectivityManager connect = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connect.getActiveNetworkInfo();

        if(null != info) {
            if(info.getState() == NetworkInfo.State.CONNECTED
                    || info.getState() == NetworkInfo.State.CONNECTING){
                if(info.getType() == connect.TYPE_MOBILE) {
                    return TYPE_MOBILE;
                }
                else if (info.getType() == connect.TYPE_WIFI){
                    return TYPE_WIFI;
                }
            }
        }
        return TYPE_NONE;
    }

}
