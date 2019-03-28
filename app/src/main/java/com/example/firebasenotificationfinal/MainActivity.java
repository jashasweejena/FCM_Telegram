package com.example.firebasenotificationfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private static String TAG = MainActivity.class.getSimpleName();
BroadcastReceiver broadcastReceiver;
RecyclerView recyclerView;
List<Forward> cardList;
FastAdapter<Forward> fastAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        ItemAdapter<Forward> itemAdapter = new ItemAdapter<>();
        cardList = new ArrayList<>();
        itemAdapter.add(cardList);
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(fastAdapter);
//        final FastItemAdapter<Forward> fastAdapter = new FastItemAdapter<>();
//        recyclerView.setAdapter(fastAdapter);

       //Added for testing only.
//        fastAdapter.add(cardList);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("channelOne", "channelOne", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        getToken();

        FirebaseMessaging.getInstance().subscribeToTopic("mofofofo")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success!";
                        if (!task.isSuccessful()) {
                            msg = "Fail!";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String dataPassed = intent.getStringExtra("DATA");
                String s = intent.getAction();
                cardList.add(new Forward(dataPassed));
//                itemAdapter.add(cardList);
                fastAdapter= FastAdapter.with(itemAdapter);
//                itemAdapter.notify();
                fastAdapter.notifyDataSetChanged();
                try{
                    Log.d(TAG, "cardList: " + cardList.get(cardList.size() - 1).getMessage());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(context,
                        "Triggered by Service!\n"
                                + "Data passed: " + dataPassed,
                        Toast.LENGTH_LONG).show();
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyMessagingService.MY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        //Starting service explicitly is handled by FireBase, hence not needed.
    }

    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}



