package com.multisourcing.musicradio.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.multisourcing.musicradio.R;
import com.multisourcing.musicradio.app.AppController;
import com.multisourcing.musicradio.model.AlbumModel;

import java.util.List;

/**
 * Created by RIFAN on 10-Feb-16.
 */
public class AlbumAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<AlbumModel> albumItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AlbumAdapter(Activity activity, List<AlbumModel> albumItem) {
        this.activity = activity;
        this.albumItem = albumItem;
    }

    @Override
    public int getCount () {
        return albumItem.size();
    }

    @Override
    public Object getItem (int position) {
        return albumItem.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.album_lisr_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.nivAlbum);
        TextView tvAlbumName = (TextView) convertView.findViewById(R.id.tvAlbumName);
        // getting movie data for the row
        AlbumModel album = albumItem.get(position);

        // thumbnail image
        thumbNail.setImageUrl("http://demo1.zapbd.com/apps/content/album/"+album.getImage(), imageLoader);

        // title
        tvAlbumName.setText(album.getName());
        return convertView;
    }



}
