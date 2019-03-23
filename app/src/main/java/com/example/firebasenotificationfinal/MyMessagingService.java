package com.example.firebasenotificationfinal;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyMessagingService extends FirebaseMessagingService {
    private static String TAG = MyMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null) {
            if (remoteMessage.getNotification() != null)
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

            if (remoteMessage.getData() != null) {
                Log.d(TAG, "data received: " + remoteMessage.getData().toString());
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: " + s);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.star_on);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());
    }
}
