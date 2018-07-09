package lk.dhanuhmd.letsworkwithaudio;


import android.app.Notification;
import android.app.NotificationManager;
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

        if(player == null) {
            player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
            player.start();
        } else if (player != null && player.isPlaying()) {
            player.stop();
            player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
            player.start();
        } else {
            player = MediaPlayer.create(getApplicationContext(), Uri.parse(entry));
            player.start();
        }
        notifyNotification(entry);
        //showNotification();
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

//    private void startInForeground()
//    {
//        NotificationCompat.Builder mNotifyBuilder;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            MyNotificationManager notificationManager = new MyNotificationManager(getApplicationContext());
//            notificationManager.createServiceNotificationChannel();
//
//            mNotifyBuilder = new NotificationCompat.Builder(this, notificationManager.getChannelId());
//        } else {
//            mNotifyBuilder = new NotificationCompat.Builder(this);
//        }
//
//        mNotifyBuilder
//                .setSmallIcon(R.drawable.ic_logo_notify)
//                .setPriority(NotificationCompat.PRIORITY_MAX);
//
//        startForeground(hashCode(), mNotifyBuilder.build());
//    }

    public void notifyNotification(String songName) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play);

        // Notification
        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(contentPendingIntent)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(songName)
                .setContentText("ddd")
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setOngoing(true)
                .addAction(R.mipmap.ic_launcher, "sdf", contentPendingIntent)
                .setDeleteIntent(contentPendingIntent)
                .build();

        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        //CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                //.setSmallIcon(R.drawable.ic_play)  // the status icon
                .setTicker("FUCKING SERVICE")  // the status text
                //.setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("THE EX FILEX")  // the label of the entry
                .setContentText("FUCK IS THIS")  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
}
