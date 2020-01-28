package com.franklyn.alc.cryptocash.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by AGBOMA franklyn on 22/10/17.
 */
public class ConnectionReceiver extends BroadcastReceiver {

    private final String LOG_TAG = ConnectionReceiver.class.getSimpleName();
    private static boolean value;

    @Override
    public void onReceive(Context context, final Intent intent) {

        int connectionType = Connection.getConnectionState(context);

        if(connectionType == 0){
            Log.e(LOG_TAG, "not connected");
            value = false;

        }
        else {
            Log.e(LOG_TAG, "connected");
            value = true;
        }
    }

    public static boolean isConnected() {
        return value;
    }
}
