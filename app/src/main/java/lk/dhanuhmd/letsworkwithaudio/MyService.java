package lk.dhanuhmd.letsworkwithaudio;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
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
    static final int NOTIFICATION = 1;

    public final IBinder iBinder = new LocalBinder();
    private NotificationManager mNM;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    public void playMusic(String entry){

        player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
        player.start();
//
//        if(player == null) {
//            player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
//            player.start();
//        } else if (player != null && player.isPlaying()) {
//            player.stop();
//            player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
//            player.start();
//        } else {
//            player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
//            player.start();
//        }
        notifyNotification(entry);
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


    public void notifyNotification(String songName) {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this)
                        .setContentTitle("Lets work with audio")
                        .setContentText("playing now")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(android.R.drawable.ic_media_previous, " ", pendingIntent)
                        .addAction(android.R.drawable.ic_media_play, "", pendingIntent)
                        .addAction(android.R.drawable.ic_media_next, "", pendingIntent)
                        .setContentIntent(pendingIntent)
                        .setTicker(songName)
                        .build();


        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
    }
}
