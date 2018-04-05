package com.multisourcing.musicradio.model;

/**
 * Created by RIFAN on 10-Feb-16.
 */
public class AlbumModel {

    String album_id;
    String image;
    String name;
    String img_big;
    String img_big_blar;


    public AlbumModel() {
    }

    public AlbumModel(String album_id, String image, String name, String img_big, String img_big_blar) {
        this.album_id = album_id;
        this.image = image;
        this.name = name;
        this.img_big = img_big;
        this.img_big_blar = img_big_blar;
    }

    public String getImg_big() {
        return img_big;
    }

    public void setImg_big(String img_big) {
        this.img_big = img_big;
    }

    public String getImg_big_blar() {
        return img_big_blar;
    }

    public void setImg_big_blar(String img_big_blar) {
        this.img_big_blar = img_big_blar;
    }

    public String getAlbum_id () {
        return album_id;
    }

    public void setAlbum_id (String album_id) {
        this.album_id = album_id;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
