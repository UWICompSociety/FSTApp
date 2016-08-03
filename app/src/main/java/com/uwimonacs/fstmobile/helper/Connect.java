package com.uwimonacs.fstmobile.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by Matthew on 11/22/2015.
 */
public class Connect {
    private final Context ctxt;
    private final ConnectivityManager cm;

    public Connect(Context ctxt) {
        this.ctxt = ctxt;
        cm = (ConnectivityManager)ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * checks whether phone is connected to a network
     * @return isConnected
     */
    public boolean isConnected() {
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * checks whether phone has internet connectivity
     * @return
     * @throws Exception
     */
    public static boolean haveInternetConnectivity() throws Exception {
        final URL url = new URL("http://www.google.com");

        final HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) // checks if server responds with 200 OK
            return true;   // internet is present
        else{
            return false;  // no internet present
        }
    }
}
