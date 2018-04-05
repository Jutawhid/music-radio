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
import com.multisourcing.musicradio.model.KobitaImageModel;
import com.multisourcing.musicradio.model.VideoImageModel;

import java.util.List;

/**
 * Created by Md.SalahUddin on 6/27/2016.
 */
public class KobitaGridElementAdapter extends RecyclerView.Adapter<KobitaGridElementAdapter.SimpleViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Context context;
    //    private List<String> elements;
    private List<KobitaImageModel> kobitaImageModelList;

    String SINGER_IMAGE_URL = AppController.TAG_SINGER_IMAGE_URL;
    public KobitaGridElementAdapter(Context context, List<KobitaImageModel> kobitaImageModelList){
        this.context = context;
        this.kobitaImageModelList = kobitaImageModelList;

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

        KobitaImageModel kobitaImageModel = kobitaImageModelList.get(position);

        final String strSingerId = kobitaImageModel.getSinger_id();

        holder.nivImages.setImageUrl(SINGER_IMAGE_URL+ kobitaImageModel.getImage(),imageLoader );
        holder.tvArtistName.setText(kobitaImageModel.getName());

        holder.nivImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Position =" + strSingerId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AlbumListActivity.class);
                intent.putExtra("SINGER_ID",strSingerId);
                intent.putExtra("CATEGORY_ID","11");
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
        return this.kobitaImageModelList.size();
    }
}

