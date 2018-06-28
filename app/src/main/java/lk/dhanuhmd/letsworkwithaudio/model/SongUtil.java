package lk.dhanuhmd.letsworkwithaudio.model;

import java.io.File;

/**
 * Created by Acer on 25/06/2018.
 */

public class SongUtil {

    //check whether file ia a song or not
    public static boolean isASong(File file) {
        if(file.toString().endsWith(".mp3")){
            return true;
        }
        return false;

    }

    public static boolean isASong(String path) {
        return true;
    }

    public static Song convertToASong(File file) {
        //Planed to work with content provider API. which can provide metadata of a audio file.
        Song song = new Song(file.getName(), file.getPath(), null, null, null);
        return song;
    }
}
