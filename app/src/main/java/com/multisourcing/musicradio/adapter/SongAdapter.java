package com.multisourcing.musicradio.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.multisourcing.musicradio.R;
import com.multisourcing.musicradio.model.SongListModel;

import java.util.List;

/**
 * Created by RIFAN on 10-Feb-16.
 */
public class SongAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<SongListModel> songItem;
    public ImageView ibEq;

    public SongAdapter(Activity activity, List<SongListModel> songItem) {
        this.activity = activity;
        this.songItem = songItem;
    }


    @Override
    public int getCount () {
        return songItem.size();
    }

    @Override
    public Object getItem (int position) {
        return songItem.get(position);
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
            convertView = inflater.inflate(R.layout.song_list_row, null);

        TextView tvSongName = (TextView)convertView.findViewById(R.id.tvSongName);
        ibEq = (ImageView)convertView.findViewById(R.id.ivEq);
//        ImageButton ibPlay = (ImageButton)convertView.findViewById(R.id.ibPlay);
//        RelativeLayout rlMain = (RelativeLayout)convertView.findViewById(R.id.rlMain);
        SongListModel songs = songItem.get(position);
        tvSongName.setText(songs.getSong());
        //boolean visibility = songItem.get(position).isVisible();
        boolean visibility = songItem.get(position).isVisible();
        ibEq.setVisibility(visibility?View.VISIBLE:View.GONE);
//        rlMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//                try {
//                    String url = "http://apps.nfmenergy.com/content/mp3/"+songs.getSong().replace(" ","%20");
//                    Intent serviceIntent = new Intent(activity, NotificationService.class);
//                    serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
//                    serviceIntent.putExtra("song_url",url);
//                    activity.startService(serviceIntent);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//
//                    Log.i("MEDIA PLAYER >>>", e.toString());
//                }
//            }
//
//
//        });

        return convertView;
    }


}
