package lk.dhanuhmd.letsworkwithaudio.database;

/**
 * Created by Dhanushka Dharmasena on 25/06/2018.
 */

public class Table {
    //table name
    public static final String TABLE = "MUSIC";
    //table columns
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_ARTIST = "artist";

    public static final String QUERY_CREATE = "CREATE TABLE MUSIC (NAME TEXT, PATH TEXT, GENRE TEXT, ALBUM TEXT, ARTIST TEXT)";
    public static final String QUERY_DELETE =
            "DROP TABLE '" + TABLE +"'";
}
