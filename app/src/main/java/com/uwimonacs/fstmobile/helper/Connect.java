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

    private Context ctxt;
    ConnectivityManager cm;

    public Connect(Context ctxt){
        this.ctxt = ctxt;
        cm = (ConnectivityManager)ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * checks whether phone is connected to a network
     * @return isConnected
     */
    public boolean isConnected(){
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    /**
     * checks whether phone has internet connectivity
     * @return
     * @throws Exception
     */
    public boolean haveInternetConnectivity() throws Exception{
        URL url = new URL("http://www.google.com");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //opens a connection to google.com

        conn.setRequestMethod("GET");

        if(conn.getResponseCode() == 200) //checks if server responds with 200 OK
            return true;   //internet is present
        else{
            return false;  //no internet present
        }
    }
}
