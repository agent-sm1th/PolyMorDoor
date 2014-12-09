package za.co.polymorph.polymordoor.util.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import za.co.polymorph.polymordoor.app.App;


/**
 * Created by Legion on 14/11/14.
 * Copyright (c) 2014 Polymorph Systems. All rights reserved.
 *
 * A utility to assist with determining the device's connectivity state.
 */
public class ConnectionUtil {

    /**
     * Whether the device is connected.
     * To be used as the default connectivity state method.
     * Place custom connection requirements here.
     * @return Whether the device is connected using the default connection criteria.
     */
    public static boolean isConnected() {
        return isConnectedToWifi(App.getContext());
    }

    /**
     * Whether the device is connected to wifi.
     * @param context A context.
     * @return Whether the device is connected to wifi.
     */
    public static boolean isConnectedToWifi(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Whether the device is connected to any network (wifi, cellular, etc.).
     * @param context A context.
     * @return Whether the device is connected to any network.
     */
    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    /**
     * Whether the device is connected to the specified connection type.
     * @param context A context.
     * @param connectionType A connection type to check.
     * @return Whether the device is connected to the specified connection type.
     */
    public static boolean isConnected(Context context, int connectionType) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(connectionType);
        return ni != null && ni.isConnected();
    }
}
