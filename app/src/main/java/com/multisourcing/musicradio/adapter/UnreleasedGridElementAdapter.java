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
import com.multisourcing.musicradio.model.UnreleasedImageModel;

import java.util.List;

/**
 * Created by rifan on 16-Feb-16.
 */
public class UnreleasedGridElementAdapter extends RecyclerView.Adapter<UnreleasedGridElementAdapter.SimpleViewHolder>{
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Context context;
    String SINGER_IMAGE_URL = AppController.TAG_SINGER_IMAGE_URL;
    //    private List<String> elements;
    private List<UnreleasedImageModel> unreleasedImageModels;

    public UnreleasedGridElementAdapter(Context context, List<UnreleasedImageModel> unreleasedImageModels){
        this.context = context;
        this.unreleasedImageModels = unreleasedImageModels;

//        // Fill dummy list
//        for(int i = 0; i < unreleasedImageModels.size() ; i++){
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
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
//        holder.nivImages.setText(elements.get(position));

        UnreleasedImageModel unreleasedImageModel = unreleasedImageModels.get(position);
        final String strSingerId = unreleasedImageModel.getSinger_id();

        holder.nivImages.setImageUrl(SINGER_IMAGE_URL+ unreleasedImageModel.getImage(),imageLoader );
        holder.tvArtistName.setText(unreleasedImageModel.getName());
        holder.nivImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Position =" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AlbumListActivity.class);
                intent.putExtra("SINGER_ID",strSingerId);
                intent.putExtra("CATEGORY_ID","7");
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
        return this.unreleasedImageModels.size();
    }
}