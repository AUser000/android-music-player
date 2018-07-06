package lk.dhanuhmd.letsworkwithaudio;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.net.URI;

/**
 * Created by Dhanushka Dharmasena on 04/07/2018.
 */

public class PlayService extends Service {

    MediaPlayer player;
    private boolean isServiceIsRunning = false;
    static final int NOTIFICATION_ID = 543;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri uri = Uri.parse(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
        player = MediaPlayer.create(this, uri);
        player.start();
        startServiceWithNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceIsRunning = false;
        player.stop();
        player = null;
    }


    public void startServiceWithNotification() {
        if(isServiceIsRunning) return;
        isServiceIsRunning = true;
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        //notificationIntent.setAction(C.ACTION_MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText("ddd")
                //.setSmallIcon(icon)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(contentPendingIntent)
                .setOngoing(true)
//                .setDeleteIntent(contentPendingIntent)
                .build();

        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
    }
}
