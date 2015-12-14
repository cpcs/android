package com.example.cpcs.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cpcs on 11/12/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TICKER = "Time for another selfie";
    private static final String TITLE = "Daily Selie";
    private static final int NOTIFICATION_ID = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID,
                new Notification.Builder(
                        context).setTicker(TICKER)
                        .setSmallIcon(android.R.drawable.ic_menu_camera)
                        .setAutoCancel(true)
                        .setContentTitle(TITLE)
                        .setContentText(TICKER)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(contentIntent).build());
    }
}
