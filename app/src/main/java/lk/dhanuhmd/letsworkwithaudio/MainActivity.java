package lk.dhanuhmd.letsworkwithaudio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import lk.dhanuhmd.letsworkwithaudio.database.DBHelper;
import lk.dhanuhmd.letsworkwithaudio.database.SongDb;
import lk.dhanuhmd.letsworkwithaudio.database.Table;
import lk.dhanuhmd.letsworkwithaudio.model.Genre;
import lk.dhanuhmd.letsworkwithaudio.model.Song;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Button pauseBtn, playBtn, stopBtn ;
    SQLiteDatabase database;
    MediaPlayer player;
    DBHelper dbHelper;
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

        dbHelper = new DBHelper(MainActivity.this);
        songsList = new ArrayList<>();
        arrayNameList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_red_light);

        try {
            if (android.os.Build.VERSION.SDK_INT > 23) {
                if(!permissionGranted) {
                    checkPermission();
                    return;
                }
            }
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM MUSIC", null);
            while (cursor.moveToNext()) {
                Song temp = new Song();
                temp.setSongName(cursor.getString(cursor.getColumnIndex("NAME")));
                temp.setSongPath(cursor.getString(cursor.getColumnIndex("PATH")));
                temp.setGenre(Genre.OTHER);
                temp.setArtist("");
                temp.setAlbum("");
                songsList.add(temp);
                arrayNameList.add(cursor.getString(cursor.getColumnIndex("NAME")));
            }
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_activated_1, arrayNameList );
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, ""+e , Toast.LENGTH_SHORT).show();
        }

        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player != null && player.isPlaying() ) {
                    player.pause();
                    pauseBtn.setText("RESUM");
                } else if (player != null && !player.isPlaying()) {
                    int length = player.getCurrentPosition();
                    player.seekTo(length);
                    player.start();
                    pauseBtn.setText("PAUSE");
                }
            }
        });

        playBtn = (Button) findViewById(R.id.plyBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( player != null && player.isPlaying()) {
                    player.stop();
                }
                if (android.os.Build.VERSION.SDK_INT > 23) {
                    if(!permissionGranted) {
                        checkPermission();
                        return;
                    }
                }
                player = MediaPlayer.create(MainActivity.this, /* Uri.parse(path)*/ R.raw.neked);
                player.start();
            }
        });

        stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player != null) {
                    player.stop();
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (android.os.Build.VERSION.SDK_INT > 23) {
                    if(!permissionGranted) {
                        checkPermission();
                        return;
                    }
                }
                String extStoreSd = System.getenv("SECONDARY_STORAGE");
                //File fileDirSd = new File(extStoreSd);
                //File dir for internal storage(programmatically external)
                String extStoreDv = System.getenv("EXTERNAL_STORAGE");
                File fileDirDv = new File(extStoreDv);

                database = dbHelper.getWritableDatabase();
                database.delete(Table.TABLE, null, null);

                SongDb.getSongsToDb(fileDirDv, database);
                //SongDb.getSongsToDb(fileDirSd, database);
                database.close();

                try {
                    database = dbHelper.getReadableDatabase();
                    Cursor cursor = database.rawQuery("SELECT * FROM MUSIC", null);
                    while (cursor.moveToNext()) {
                        Song temp = new Song();
                        temp.setSongName(cursor.getString(cursor.getColumnIndex("NAME")));
                        temp.setSongPath(cursor.getString(cursor.getColumnIndex("PATH")));
                        temp.setGenre(Genre.OTHER);
                        temp.setArtist("");
                        temp.setAlbum("");
                        songsList.add(temp);
                        arrayNameList.add(cursor.getString(cursor.getColumnIndex("NAME")));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_activated_1, arrayNameList );
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, ""+e , Toast.LENGTH_SHORT).show();
                }
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
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