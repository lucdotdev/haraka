package com.lucdotdev.haraka.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lucdotdev.haraka.R;
import com.lucdotdev.haraka.ui.home_livreur.LivreurHomeScreen;
import com.lucdotdev.haraka.ui.home_store.StoreHomeScreen;
import com.lucdotdev.haraka.utils.MyWorker;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleNow(remoteMessage.getData().get("msg"));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification( remoteMessage.getNotification().getBody());
        }
        
    }
    // [END receive_message]


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleNow(String msg) {
        Log.d(TAG, "Short lived task is done.");
        sendNotification(msg);
    }

    private void sendRegistrationToServer(String token) {

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendNotification(String messageBody) {


        SharedPreferences prefs = getSharedPreferences("AUTH", MODE_PRIVATE);
        int type = prefs.getInt("account_type", 2);



        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, getItent(type),
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Haraka";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.haraka_flat)
                        .setContentTitle("Notification")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Haraka",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private Intent getItent(int type) {
       if(type == 1){
           Intent intent = new Intent(this, StoreHomeScreen.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           return intent;
       } else {
           Intent intent = new Intent(this, LivreurHomeScreen.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           return intent;
       }
    }
}