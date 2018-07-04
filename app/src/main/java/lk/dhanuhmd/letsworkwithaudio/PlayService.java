package lk.dhanuhmd.letsworkwithaudio;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.net.URI;

/**
 * Created by Dhanushka Dharmasena on 04/07/2018.
 */

public class PlayService extends Service {

    MediaPlayer player;

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
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }


}
