package lk.dhanuhmd.letsworkwithaudio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteProgram;
import android.widget.Toast;

import java.io.File;

import lk.dhanuhmd.letsworkwithaudio.MainActivity;
import lk.dhanuhmd.letsworkwithaudio.model.Song;
import lk.dhanuhmd.letsworkwithaudio.model.SongUtil;


/**
 * Created by Dhanushka Dharmasena on 25/06/2018.
 */

public class SongDb {

    public static void getSongsToDb(File file, SQLiteDatabase database) {
        File[] fileList = file.listFiles();
        for (File tempFile: fileList) {
            if(tempFile.isDirectory()){
                getSongsToDb(tempFile ,database);
            } else {
                if(SongUtil.isASong(tempFile)) {
                    Song song = SongUtil.convertToASong(tempFile);
                    ContentValues values = new ContentValues();
                    values.put("NAME", song.getSongName());
                    values.put("PATH", song.getSongPath());
                    values.put("ALBUM", "");
                    values.put("GENRE", "");
                    values.put("ARTIST", "");
                    database.insert("MUSIC", null, values);
                }
            }
        }
    }
}
