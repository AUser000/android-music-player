package lk.dhanuhmd.letsworkwithaudio;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {

    MediaPlayer player = null;
    static final int NOTIFICATION_ID = 543;

    public final IBinder iBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void playMusic(String entry){

        if(player == null) {
            player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
            startServiceWithNotification(entry);
            player.start();
        }
    }

    public void playRawMusic() {
        if(player != null) {}

    }

    public void stopMusic() {
        if(player != null) {
            player.stop();
            player = null;
        }
    }

    public boolean isPlayerPlaying() {
        Boolean isPlaying = false;
        if(player != null) {
            if(!player.isPlaying()) {
                isPlaying = true;
            }
        }
        return isPlaying;
    }

    public void doPlayerNull() {
        player = null;
    }

    public void startServiceWithNotification(String songName) {

        Intent notificationIntent = new Intent(this, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);

        // Notification
        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(contentPendingIntent)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(songName)
                .setContentText("ddd")
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .setDeleteIntent(contentPendingIntent)
                .build();

        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
    }
}
