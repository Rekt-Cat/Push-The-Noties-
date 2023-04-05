package com.example.pushthenoties;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

     NotificationManager mNotificationManager;
     FirebaseFirestore dbRoot=FirebaseFirestore.getInstance();
    int id=0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if ((remoteMessage.getData().get("userId")).equals(FirebaseAuth.getInstance().getUid())) {
            DocumentReference documentReference = dbRoot.collection("NotiSent").document(FirebaseAuth.getInstance().getUid());
            Map<String, String> map = new HashMap<>();
            map.put("title", remoteMessage.getData().get("title"));
            map.put("description", remoteMessage.getData().get("body"));
            documentReference.collection("allSentNoties").document().set(map);
            Log.d("fm", "cannot");
        }
        else {

            DocumentReference documentReference = dbRoot.collection("NotiReceived").document(FirebaseAuth.getInstance().getUid());
            Map<String, String> map = new HashMap<>();
            map.put("title", remoteMessage.getNotification().getTitle());
            map.put("description", remoteMessage.getNotification().getBody());
            documentReference.collection("allReceivedNoties").document().set(map);

            documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("msg", "SUCCESS!!!");
                }
            });



            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                r.setLooping(false);
            }

            // vibration
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {100, 300, 300, 300};
            v.vibrate(pattern, -1);


            int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());


            Intent resultIntent = new Intent(this, ReceivedNotiActivity.class);
            Log.d("pol", ""+remoteMessage.getNotification().getTitle());
            resultIntent.putExtra("title",remoteMessage.getData().get("title"));
            resultIntent.putExtra("body",remoteMessage.getData().get("body"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(resourceImage);
            } else {
                builder.setSmallIcon(resourceImage);
            }
            builder.setContentTitle(remoteMessage.getNotification().getTitle());
            builder.setContentText(remoteMessage.getNotification().getBody());
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);

            mNotificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }


            mNotificationManager.notify(id++, builder.build());

        }
    }


}


