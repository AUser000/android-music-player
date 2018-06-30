package lk.dhanuhmd.letsworkwithaudio.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

import static android.os.Environment.getDataDirectory;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.*;

/**
 * Created by Dhanushka Dharmasena on 25/06/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "music.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, getExternalStorageDirectory()
                + File.separator+"Music Player"+ File.separator+ DB_NAME, null, DB_VERSION);
    }

//    public SQLiteDatabase getWritableDatabase(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(Table.QUERY_DELETE);
//        return super.getWritableDatabase();
//    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Table.QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Table.QUERY_DELETE);
        onCreate(sqLiteDatabase);
    }


}
