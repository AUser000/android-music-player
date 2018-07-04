package lk.dhanuhmd.letsworkwithaudio;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import lk.dhanuhmd.letsworkwithaudio.model.Song;

public class MainActivity extends AppCompatActivity {

    private Button pauseBtn,stopBtn ;
    ImageButton playButton, forwordButton, backwordButton;
    MediaPlayer player;
    ListView listView;
    ArrayList<Song> songsList;
    ArrayList<String> arrayNameList;
    private boolean permissionGranted;
    private static final int REQUEST_PERMISSION_WRITE = 101;///
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songsList = new ArrayList<>();
        arrayNameList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        playButton = (ImageButton) findViewById(R.id.plyBtn);

        if (android.os.Build.VERSION.SDK_INT > 23) {
            if(!permissionGranted) {
                checkPermission();
            }
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = contentResolver.query(uri, null, selection, null, sortOrder);
        int count = 0;
        if(cur != null) {
            count = cur.getCount();
            if(count > 0) {
                while(cur.moveToNext()) {
                    String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    arrayNameList.add(data);
                }
            }
        }
        cur.close();
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_activated_1, arrayNameList );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String entry= (String) parent.getAdapter().getItem(position);
                if ( player != null && player.isPlaying()) {
                    player.stop();
                }
                if (android.os.Build.VERSION.SDK_INT > 23) {
                    if(!permissionGranted) {
                        checkPermission();
                        if(!permissionGranted) {
                            return;
                        }
                    }
                }
                player = MediaPlayer.create(MainActivity.this, Uri.parse(entry));
                player.start();
                playButton.setImageResource(R.drawable.ic_action_name);
            }
        });

//        pauseBtn = (Button) findViewById(R.id.pauseBtn);
//        pauseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(player != null && player.isPlaying() ) {
//                    player.pause();
//                    pauseBtn.setText("RESUM");
//                } else if (player != null && !player.isPlaying()) {
//                    int length = player.getCurrentPosition();
//                    player.seekTo(length);
//                    player.start();
//                    pauseBtn.setText("PAUSE");
//                }
//            }
//        }); @android:drawable/ic_media_next


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player != null) {
                    player.stop();
                    playButton.setImageResource(R.drawable.ic_play);
                }
            }
        });


    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) || Environment.MEDIA_MOUNTED.equals(state));
    }

    private boolean checkPermission() {
        if(!isExternalStorageReadable() || !isExternalStorageWritable()) {
            return  false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}