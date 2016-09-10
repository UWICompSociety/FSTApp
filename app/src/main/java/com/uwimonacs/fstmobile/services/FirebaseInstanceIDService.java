package com.uwimonacs.fstmobile.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    public FirebaseInstanceIDService() {
    }

    /**
     * Gives the new token ID for this specific app instance. Used for testing purposes
     * to send test notifications to a specific device
     */
    @Override
    public void onTokenRefresh() {
        System.out.println("NEW TOKEN: " + FirebaseInstanceId.getInstance().getToken());
    }
}
