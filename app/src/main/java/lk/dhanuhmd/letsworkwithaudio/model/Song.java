package lk.dhanuhmd.letsworkwithaudio.model;

/**
 * Created by Dhanushka Dharmasena on 25/06/2018.
 */

public class Song {
    private String songName;
    private String songPath;
    private String artist;
    private String album;
    private Genre genre;

    public Song() {}

    public Song(String songName, String songPath, String artist, String album, Genre genre) {
        this.songName = songName;
        this.songPath = songPath;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
