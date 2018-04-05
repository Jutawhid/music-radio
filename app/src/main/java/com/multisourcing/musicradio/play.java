package com.multisourcing.musicradio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.volley.toolbox.NetworkImageView;

public class play extends AppCompatActivity {
    VideoView videoView;
    // String url = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
    MediaController mc;
    ImageButton ibShare;
    NetworkImageView nivAlbumArt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        nivAlbumArt = (NetworkImageView) findViewById(R.id.nivAlbumArt);
        videoView = (VideoView) findViewById(R.id.video_view);
        Intent iin = getIntent();
        final Bundle b = iin.getExtras();


        if (b != null) {
            mc = new MediaController(this);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);
            videoView.setMediaController(mc);

            final String j = (String) b.get("URL_SONG");
            Uri link = Uri.parse(j.replace(" ", "%20"));
            videoView.setVideoURI(link);
            videoView.requestFocus();
            videoView.start();

            mc.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Handle next click here
                    /*if (position != songList.size() - 1) {
                        position++;
                    } else {
                        position = 0;
                    }

                    videoView.setVideoURI(link);
                    videoView.requestFocus();
                    videoView.start();*/
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Handle previous click here
                }
            });
            videoView.setMediaController(mc);

        }

        ibShare =(ImageButton)findViewById(R.id.ibShare);
        ibShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "Welcome to music radio");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Gaming Apps");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.gamezonebd.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


    }
}