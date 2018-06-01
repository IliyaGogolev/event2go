package com.event2go.app.features.notifications.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.event2go.app.features.main.MainActivity;
import com.event2go.app.features.notifications.repository.NotificationsRecyclerFragment;
import com.event2go.app.features.notifications.data.PushNotificationType;
import com.event2go.app.utils.NavUtils;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Iliya Gogolev on 6/30/2015.
 */
public class PushBroadcastReceiver extends ParsePushBroadcastReceiver {

    private static final String TAG = PushBroadcastReceiver.class.toString();

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.d(TAG, "onPushOpen");
        JSONObject pushData = null;
        String command = null;

        try {
            pushData = new JSONObject(intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_DATA));
            command = pushData.getString("command");
            Log.d(TAG, "command = "+command);
            handleCommand(context, command);
        } catch (JSONException var7) {
            Log.e(TAG, "Unexpected JSONException when receiving push data: ", var7);


        }
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(Context context, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                //.setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void handleCommand(Context context, String command) {
        switch (command) {
            case PushNotificationType.INVITE_USER: {
                NavUtils.showActivityFromPushNotification(context, NotificationsRecyclerFragment.class.getName());
            }
        }
    }
}