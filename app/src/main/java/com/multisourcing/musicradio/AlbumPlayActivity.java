package com.multisourcing.musicradio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.multisourcing.musicradio.adapter.SongAdapter;
import com.multisourcing.musicradio.app.AppController;
import com.multisourcing.musicradio.model.SongListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlbumPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private List<SongListModel> songs = new ArrayList<SongListModel>();
    private SongAdapter songAdapter;
    String URL_SONGS;
    String URL_ALBUM_ART;
    String URL_ALBUM_ART_BIG;
    String URL_ALBUM_ART_BLUR;
    String URL_MP3;
    ListView lvSongs;
    MediaPlayer mediaPlayer;
    NetworkImageView nivAlbumArt,nivAlbumArtBlur;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();

    public int currentlyPlaying;

    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public int oneTimeOnly = 0;
    int songID = 0;
    ImageButton ibPrev, ibPlay, ibPause, ibNext, ibFastForward, ibFastRewind;
    TextView tvStartTime, tvEndTime;
    RelativeLayout llList;
    ImageButton ibShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        URL_SONGS = getIntent().getExtras().getString("URL_SONG");
        URL_ALBUM_ART = getIntent().getExtras().getString("URL_ALBUM_ART");
        URL_ALBUM_ART_BIG = getIntent().getExtras().getString("URL_ALBUM_ART_BIG");
        URL_ALBUM_ART_BLUR = getIntent().getExtras().getString("URL_ALBUM_ART_BLUR");
        imageLoader = AppController.getInstance().getImageLoader();
        lvSongs = (ListView) findViewById(R.id.lvSongList);
        nivAlbumArt = (NetworkImageView) findViewById(R.id.nivAlbumArt);
        ibNext = (ImageButton) findViewById(R.id.ibNext);
        ibPlay = (ImageButton) findViewById(R.id.ibPlay);
        ibFastRewind = (ImageButton) findViewById(R.id.ibFastRewind);
        ibFastForward = (ImageButton) findViewById(R.id.ibFastForward);
        ibPrev = (ImageButton) findViewById(R.id.ibPrev);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        llList = (RelativeLayout)findViewById(R.id.llList);
        ibShare = (ImageButton)findViewById(R.id.ibShare);
        seekbar.setClickable(false);
        ibNext.setOnClickListener(this);
        ibPlay.setOnClickListener(this);
        ibPrev.setOnClickListener(this);
        ibFastRewind.setOnClickListener(this);
        ibFastForward.setOnClickListener(this);
        ibShare.setOnClickListener(this);

        songAdapter = new SongAdapter(this, songs);
        lvSongs.setAdapter(songAdapter);


        loadSongs();

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songs.get(songID).setVisible(false);
                songAdapter.notifyDataSetChanged();
                songID = position;
                stopPlaying();
                URL_MP3 = "http://demo1.zapbd.com/apps/content/mp3/" + songs.get(position).getSong().replace(" ", "%20");
                songs.get(songID).setVisible(true);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(URL_MP3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        ibPlay.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);

                        finalTime = mp.getDuration();
                        startTime = mp.getCurrentPosition();
                        if (oneTimeOnly == 0) {
                            seekbar.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }
                        tvEndTime.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                        );

                        tvStartTime.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                        );

                        seekbar.setProgress((int) startTime);
                        myHandler.postDelayed(UpdateSongTime, 100);
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        songs.get(songID).setVisible(false);
                        songID++;
                        if(songID >= songs.size()){
                            songID=0;
                            songs.get(songID).setVisible(true);
                        }
                        else{
                            startPlaying(songID);
                            songAdapter.notifyDataSetChanged();
                            songs.get(songID).setVisible(true);
                        }

                    }
                });

                mediaPlayer.prepareAsync();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibPrev:
                songs.get(songID).setVisible(false);
                songAdapter.notifyDataSetChanged();
                songID--;
               // currentlyPlaying--;
                if(songID<0){
                    songID=0;
                }

                if (songID >= 0) {
                    startPlaying(songID);
                    //startPlaying(currentlyPlaying);
                    songs.get(songID).setVisible(true);
                    songAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.ibPlay:
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        Log.i("Status:", " Paused");
                        ibPlay.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
                    }
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        Log.i("Status:", " Playing");
                        ibPlay.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);
                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();
                        if (oneTimeOnly == 0) {
                            seekbar.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }
                        tvEndTime.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                        );

                        tvStartTime.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                        );

                        seekbar.setProgress((int) startTime);
                        myHandler.postDelayed(UpdateSongTime, 100);
                    }
                }
                break;

            case R.id.ibShare:
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Welcome to music radio");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/
                shareTextUrl(songID);
                break;

            case R.id.ibNext:
                songs.get(songID).setVisible(false);
                songAdapter.notifyDataSetChanged();
                songID++;
                //currentlyPlaying++;

                if (songID==songs.size()){
                    ibNext.setEnabled(false);
                }

                if (songID <= songs.size()) {
                    startPlaying(songID);
                   // startPlaying(currentlyPlaying);
                    songs.get(songID).setVisible(true);
                    songAdapter.notifyDataSetChanged();
                }

                break;
            case R.id.ibFastForward:
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ibFastRewind:
                int temp1 = (int) startTime;

                if ((temp1 - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void loadSongs() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_SONGS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                nivAlbumArt.setImageUrl(URL_ALBUM_ART_BIG, imageLoader);
                Glide.with(AlbumPlayActivity.this).load(URL_ALBUM_ART_BLUR).asBitmap().into(new SimpleTarget<Bitmap>(700,300) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            llList.setBackground(drawable);
                        }
                    }
                });
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jObj = response.getJSONObject(i);
                        SongListModel songModel = new SongListModel();

                        Log.i(">>REQ", jObj.toString());
                        songModel.setAlbum_id(jObj.getString("album_id"));
                        songModel.setCategory_id(jObj.getString("category_id"));
                        songModel.setId(jObj.getString("id"));
                        songModel.setSinger_id(jObj.getString("singer_id"));
                        songModel.setSong(jObj.getString("song"));

                        songs.add(songModel);
                        //startPlaying("http://demo1.zapbd.com/apps/content/mp3/" + songs.get(songID).getSong().replace(" ", "%20"));
                        startPlaying(songID);
                        songs.get(songID).setVisible(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                songAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    private void startPlaying(final int position) {
        stopPlaying();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource("http://demo1.zapbd.com/apps/content/mp3/" + songs.get(position).getSong().replace(" ", "%20"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                songID=position; // member field (int)
                finalTime = mp.getDuration();
                startTime = mp.getCurrentPosition();
                ibPlay.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);
                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }
                tvEndTime.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                tvStartTime.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                songs.get(songID).setVisible(false);
                songID++;
                if(songID >= songs.size()){
                    songID=0;
                    songAdapter.notifyDataSetChanged();
                    songs.get(songID).setVisible(true);
                }
                else{
                    startPlaying(songID);
                    songAdapter.notifyDataSetChanged();
                    songs.get(songID).setVisible(true);
                }
            }
        });

        mediaPlayer.prepareAsync();
    }



    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

            myHandler.removeCallbacks(UpdateSongTime);
        }
    }

    private void shareTextUrl(int position) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        songID=position;
        share.putExtra(Intent.EXTRA_SUBJECT, "Download Gaming Apps");
        //share.putExtra(Intent.EXTRA_TEXT, "http://demo1.zapbd.com/apps/content/mp3/" + songs.get(position).getSong().replace(" ", "%20")); http://www.gamezonebd.com/
        share.putExtra(Intent.EXTRA_TEXT, "http://www.gamezonebd.com/");

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    @Override
    public void onBackPressed() {
        stopPlaying();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            stopPlaying();
            finish();
            return true;
        }

        if (id == R.id.action_home) {
            stopPlaying();
            Intent intent = new Intent(AlbumPlayActivity.this,HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            seekbar.setProgress((int) startTime);
            tvStartTime.setText(String.format("%d:%d",

                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );

            myHandler.postDelayed(this, 100);
        }
    };


}
