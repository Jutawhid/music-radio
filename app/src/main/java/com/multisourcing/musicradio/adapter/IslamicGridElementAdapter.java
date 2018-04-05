package com.multisourcing.musicradio.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.multisourcing.musicradio.AlbumListActivity;
import com.multisourcing.musicradio.R;
import com.multisourcing.musicradio.app.AppController;
import com.multisourcing.musicradio.model.IslamicImageModel;
import com.multisourcing.musicradio.model.VideoImageModel;

import java.util.List;

/**
 * Created by Md.SalahUddin on 6/27/2016.
 */
public class IslamicGridElementAdapter  extends RecyclerView.Adapter<IslamicGridElementAdapter.SimpleViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Context context;
    //    private List<String> elements;
    private List<IslamicImageModel> islamicImageModelList;

    String SINGER_IMAGE_URL = AppController.TAG_SINGER_IMAGE_URL;
    public IslamicGridElementAdapter(Context context, List<IslamicImageModel> islamicImageModelList){
        this.context = context;
        this.islamicImageModelList = islamicImageModelList;

//        // Fill dummy list
//        for(int i = 0; i < popularImageModelList.size() ; i++){
//            this.elements.add(i, "Position : " + i);
//        }
    }
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final NetworkImageView nivImages;
        public final TextView tvArtistName;

        public SimpleViewHolder(View view) {
            super(view);
            nivImages = (NetworkImageView) view.findViewById(R.id.nivImagespopular);
            tvArtistName = (TextView)view.findViewById(R.id.tvArtistName);
        }
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.grid_element_popular, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
//        holder.nivImages.setText(elements.get(position));

        IslamicImageModel islamicImageModel = islamicImageModelList.get(position);

        final String strSingerId = islamicImageModel.getSinger_id();

        holder.nivImages.setImageUrl(SINGER_IMAGE_URL+ islamicImageModel.getImage(),imageLoader );
        holder.tvArtistName.setText(islamicImageModel.getName());

        holder.nivImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Position =" + strSingerId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AlbumListActivity.class);
                intent.putExtra("SINGER_ID",strSingerId);
                intent.putExtra("CATEGORY_ID","10");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.islamicImageModelList.size();
    }
}


