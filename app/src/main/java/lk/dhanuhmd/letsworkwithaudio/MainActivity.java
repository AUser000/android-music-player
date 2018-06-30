package lk.dhanuhmd.letsworkwithaudio;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import lk.dhanuhmd.letsworkwithaudio.database.DBHelper;
import lk.dhanuhmd.letsworkwithaudio.database.SongDb;
import lk.dhanuhmd.letsworkwithaudio.database.Table;
import lk.dhanuhmd.letsworkwithaudio.model.Genre;
import lk.dhanuhmd.letsworkwithaudio.model.Song;

public class MainActivity extends AppCompatActivity {

    private Button refrshBtn, playBtn, stopBtn ;
    SQLiteDatabase database;
    MediaPlayer player;
    DBHelper dbHelper;
    ListView listView;
    ArrayList<Song> songsList;
    ArrayList<String> arrayNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);
        songsList = new ArrayList<>();
        arrayNameList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

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
            Toast.makeText(MainActivity.this, "run ", Toast.LENGTH_SHORT).show();
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_activated_1, arrayNameList );
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, ""+e , Toast.LENGTH_SHORT).show();
        }

        refrshBtn = (Button) findViewById(R.id.refreshBtn);
        refrshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long time1 = timestamp.getTime();

                //File dir for SD card
                String extStoreSd = System.getenv("SECONDARY_STORAGE");
                File fileDirSd = new File(extStoreSd);
                //File dir for internal storage(programmatically external)
                String extStoreDv = System.getenv("EXTERNAL_STORAGE");
                File fileDirDv = new File(extStoreDv);

                database = dbHelper.getWritableDatabase();
                database.delete(Table.TABLE, null, null);

                SongDb.getSongsToDb(fileDirDv, database);
                SongDb.getSongsToDb(fileDirSd, database);
                database.close();
                long time2 = timestamp.getTime();
                Toast.makeText(MainActivity.this, "run ", Toast.LENGTH_SHORT).show();
            }
        });

        playBtn = (Button) findViewById(R.id.plyBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = dbHelper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM MUSIC", null);
                String string = null;
                String path = null;
                String name = null;
                cursor.moveToFirst();
                while(cursor.moveToNext()) {
                    string = cursor.getString(cursor.getColumnIndex("NAME")) + cursor.getString(cursor.getColumnIndex("PATH"));
                    path = cursor.getString(cursor.getColumnIndex("PATH"));
                    name = cursor.getString(cursor.getColumnIndex("NAME"));
                }
                try {
                    if ( player != null && player.isPlaying()) {
                        player.stop();
                    }
                    player = MediaPlayer.create(MainActivity.this, /* Uri.parse(path)*/ R.raw.neked);
                    player.start();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, " Error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });


        stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
            }
        });

    }
}
