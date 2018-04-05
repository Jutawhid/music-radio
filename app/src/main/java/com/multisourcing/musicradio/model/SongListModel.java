package com.multisourcing.musicradio.model;

/**
 * Created by RIFAN on 10-Feb-16.
 */
public class SongListModel {

    String id;
    String singer_id;
    String album_id;
    String category_id;
    String song;

    public SongListModel() {
    }

    public SongListModel(String id, String singer_id, String album_id, String category_id, String song) {
        this.id = id;
        this.singer_id = singer_id;
        this.album_id = album_id;
        this.category_id = category_id;
        this.song = song;
    }
    private boolean visible;

    public boolean isVisible() {
        return this.visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(String singer_id) {
        this.singer_id = singer_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }
}
