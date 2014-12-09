package za.co.polymorph.polymordoor.util.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Legion on 14/11/18.
 * Copyright (c) 2014 Polymorph Systems. All rights reserved.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "ConnectivityChangeReceiver";

    private static Set<ConnectivityChangeListener> _listeners = new HashSet<ConnectivityChangeListener>();

    public static void invalidate() {
        _listeners = new HashSet<ConnectivityChangeListener>();
    }

    public static void addConnectivityChangeListener(ConnectivityChangeListener listener) {
        _listeners.add(listener);
    }

    public static void removeConnectivityChangeListener(ConnectivityChangeListener listener) {
        _listeners.remove(listener);
    }

    private static void notifyListener(boolean isConnected) {

        for(ConnectivityChangeListener listener : _listeners) {
            listener.connectivityChanged(isConnected);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(LOG_TAG, "onReceive::isConnected = " + ConnectionUtil.isConnected());
        notifyListener(ConnectionUtil.isConnected());
    }
}
