package org.jaunt.client;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MsgFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static int count = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        KeyManager.setSharedPreferenceString(getApplicationContext(), "fcm_token", s);
        Log.e(TAG, "onNewToken: " + s);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        String url = "https://hyudbprojectj.name/register/fcm/token";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uniqueId = preferences.getString("id", "Unknown");
        OkHttpClient client = new OkHttpClient();

        Log.d(TAG, uniqueId + " / " + token);

        RequestBody formBody = new FormBody.Builder()
                .add("uniqueId", uniqueId)
                .add("token", token)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//Here notification is recieved from server
        try {
            String title = remoteMessage.getData().get("title");
            Log.d(TAG, title);
            Log.d(TAG, remoteMessage.getData().toString());
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
//you can use your launcher Activity insted of SplashActivity, But if the Activity you used here is not launcher Activty than its not work when App is in background.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//Add Any key-value to pass extras to intent
        intent.putExtra("pushnotification", "yes");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//For Android Version Orio and greater than orio.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel("Report", "Report", importance);
            mChannel.setDescription(messageBody);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            mNotifyManager.createNotificationChannel(mChannel);
        }
//For Android Version lower than oreo.
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Report");
        mBuilder.setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_notify))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#FFD600"))
                .setContentIntent(pendingIntent)
                .setChannelId("Report")
                .setPriority(NotificationCompat.PRIORITY_LOW);

        mNotifyManager.notify(count, mBuilder.build());
        count++;
    }

}

//
//    @Override
//    public void onNewToken(String token) {
//        String t = token;
//        Log.d(TAG, "Refreshed token: " + token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
////        sendRegistrationToServer(token);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // ...
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
////                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
////                handleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }
//}