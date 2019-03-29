package com.example.firebasenotificationfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyMessagingService extends FirebaseMessagingService {
    private static String TAG = MyMessagingService.class.getSimpleName();
    Handler mHandler;
    public static String MY_ACTION = "MESSAGE_RECEIVED";

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null) {
            if (remoteMessage.getNotification() != null)
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

            if (remoteMessage.getData() != null) {
                final String data = remoteMessage.getData().get("data");
                Intent intent = new Intent();
                intent.setAction(MY_ACTION);
                intent.putExtra("DATA", data);
                sendBroadcast(intent);

                showNotification("New pinned message", data);
                Log.d(TAG, "data received: " + data);
                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Toast.makeText(MyMessagingService.this, data, Toast.LENGTH_LONG).show();
                    }
                };
                Message message = mHandler.obtainMessage();
                message.sendToTarget();

            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
                .setSmallIcon(android.R.drawable.star_on)
                .setChannelId("channelOne");
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());
    }



}
