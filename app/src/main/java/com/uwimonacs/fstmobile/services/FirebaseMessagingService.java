package com.uwimonacs.fstmobile.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.Splash;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public FirebaseMessagingService() {
    }

    /**
     * Called when the message is received from Firebase
     *
     * @param remoteMessage an Object representing the message received
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map data = remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String body = notification.getBody();
        String title = notification.getTitle();
        sendNotification(title, body, data);
    }

    /**
     * Sends a notification to the device when a message is received from
     * FCM
     */
    private void sendNotification(String title, String body, Map data){
        Intent intent = new Intent(this, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("activity", (String) data.get("activity"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.fstlogofinal)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
