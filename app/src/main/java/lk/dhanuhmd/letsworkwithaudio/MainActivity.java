package lk.dhanuhmd.letsworkwithaudio;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;

import lk.dhanuhmd.letsworkwithaudio.database.DBHelper;
import lk.dhanuhmd.letsworkwithaudio.database.SongDb;
import lk.dhanuhmd.letsworkwithaudio.model.Song;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.os.Environment.getRootDirectory;

public class MainActivity extends AppCompatActivity {

    private Button btn, playBtn ;
    SQLiteDatabase database;
    MediaPlayer player;
    TextView textView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(MainActivity.this);
        btn = (Button) findViewById(R.id.refreshBtn);
        btn.setOnClickListener(new View.OnClickListener() {
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

                SongDb.getSongsToDb(fileDirDv, database);
                //SongDb.getSongsToDb(fileDirSd, database);
                database.close();
                long time2 = timestamp.getTime();
                Toast.makeText(MainActivity.this, "run ", Toast.LENGTH_SHORT).show();
            }
        });

        playBtn = (Button) findViewById(R.id.plyBtn);
        textView = (TextView) findViewById(R.id.textView);
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
                textView.setText(path);
                File newfile = new File(path);

                try {
                    player.setDataSource(path);
                    player.prepare();
                    player.start();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, newfile.getPath() +"\n Error" + e, Toast.LENGTH_SHORT).show();
                    textView.setText(path + "\n\n" +e);
                    e.printStackTrace();
                }

            }
        });

    }



}
