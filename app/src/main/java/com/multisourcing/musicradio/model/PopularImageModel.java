package com.multisourcing.musicradio.model;

/**
 * Created by RIFAN on 16-Feb-16.
 */
public class PopularImageModel {

    String singer_id;
    String image;
    String name;

    public PopularImageModel() {
    }

    public PopularImageModel(String singer_id, String image, String name) {
        this.singer_id = singer_id;
        this.image = image;
        this.name = name;
    }

    public String getSinger_id () {
        return singer_id;
    }

    public void setSinger_id (String singer_id) {
        this.singer_id = singer_id;
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
