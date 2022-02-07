package com.nrlm.melasalesoffline.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.activity.SalesDetailActivity;
import com.nrlm.melasalesoffline.activity.SplashScreen;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "firebase_tag" ;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String jobType = data.get("type");
            handleNow(data);

           /* if (jobType.equalsIgnoreCase(JobType.LONG.name())) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleLongRunningJob();
            } else {
                // Handle message within 10 seconds
                handleNow(data);
            }*/
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void handleNow(Map<String, String> data) {
        if (data.containsKey("title") && data.containsKey("message")) {
            sendNotification(data.get("title"), data.get("message"));
        }
    }


    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pushnotification", "yes");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = "my_channel_01";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.aajeevika)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Channel human readable title
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Cloud Messaging Service",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("hiiii");
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
