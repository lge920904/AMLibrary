package com.giveangel.amlibrary.adpublisher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Kyungman on 2015-06-13.
 */
public class NetworkManager {
    public static boolean checkNetwork(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork == null) return false;
        switch (activeNetwork.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_MOBILE:
                return true;
            default:
                return false;
        }

    }
}
