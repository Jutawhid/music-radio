package com.multisourcing.musicradio;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.multisourcing.musicradio.adapter.SongAdapter;
import com.multisourcing.musicradio.app.AppController;
import com.multisourcing.musicradio.model.SongListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoPlayerActivity extends AppCompatActivity {
    private List<SongListModel> songs = new ArrayList<SongListModel>();
    private SongAdapter songAdapter;
    String URL_SONGS;
    String URL_MP3;
    ListView lvAlbum;
    MediaPlayer mediaPlayer;
    NetworkImageView nivAlbumArt, nivAlbumArtBlur;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();

    private int forwardTime = 5000;
    private int backwardTime = 5000;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public int oneTimeOnly = 0;
    int songID = 0;
    String URL;
    String singerID, categoryID;
    RelativeLayout llList;

    VideoView videoView;
    MediaController mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        singerID = getIntent().getExtras().getString("SINGER_ID");
        categoryID = getIntent().getExtras().getString("CATEGORY_ID");
        URL ="http://demo1.zapbd.com/apps/songslist-service.php?singerid="+singerID+ "&category_id=" + categoryID;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageLoader = AppController.getInstance().getImageLoader();
        lvAlbum = (ListView)findViewById(R.id.lvSongList);
        nivAlbumArt = (NetworkImageView) findViewById(R.id.nivAlbumArt);
//        nivAlbumArtBlur = (NetworkImageView) findViewById(R.id.nivAlbumArtBlur);
        videoView = (VideoView)findViewById(R.id.video_view);

        llList = (RelativeLayout) findViewById(R.id.llList);
        mc = new MediaController(this);

//        ibPause = (ImageButton)findViewById(R.id.ibPause);

//        ibPause.setOnClickListener(this);
        songAdapter = new SongAdapter(this, songs);
        lvAlbum.setAdapter(songAdapter);

//        songAdapter.ibEq.setVisibility(View.GONE);
        loadSongs();
//        URL_MP3 = "http://apps.nfmenergy.com/content/mp3/"+songs.get(0).getSong().replace(" ", "%20");

        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                songs.get(songID).setVisible(false);
                songAdapter.notifyDataSetChanged();
                songID = position;
                URL_SONGS= "http://demo1.zapbd.com/apps/content/mp3/" + songs.get(position).getSong().replace(" ","%20");
                //songs.get(songID).setVisible(true);

                try {
                    mc.setAnchorView(videoView);
                    mc.setMediaPlayer(videoView);
                    videoView.setMediaController(mc);
                    // Uri link = Uri.parse(URL_SONGS.replace(" ","%20"));
                    videoView.setVideoPath(URL_SONGS);
                    videoView.requestFocus();
                    videoView.start();


                    songs.get(songID).setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                /////////////
                mc.setPrevNextListeners(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Handle next click here
                        songs.get(songID).setVisible(false);
                        songAdapter.notifyDataSetChanged();
                        songID++;

                         if(songID==songs.size()){
                             songs.get(songID).setVisible(false);
                        }
                        if(songID<=songs.size()){
                            videoView.setVideoPath(URL_SONGS);
                            videoView.requestFocus();
                            videoView.start();
                            songs.get(songID).setVisible(true);
                            songAdapter.notifyDataSetChanged();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Handle previous click here
                    }
                });
                ////////////

            }
        });

    }
    public void loadSongs() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jObj = response.getJSONObject(i);
                        SongListModel albumList =new  SongListModel();

                        Log.i(">>REQ", jObj.toString());

                        albumList.setSong(jObj.getString("song"));

                        songs.add(albumList);
                        URL_SONGS="http://demo1.zapbd.com/apps/content/mp3/" + songs.get(songID).getSong().replace(" ","%20");
                        mc.setAnchorView(videoView);
                        mc.setMediaPlayer(videoView);
                        videoView.setMediaController(mc);
                        //Uri link = Uri.parse(URL_SONGS.replace(" ","%20"));
                        videoView.setVideoPath(URL_SONGS);
                        videoView.requestFocus();
                        videoView.start();


                        songs.get(songID).setVisible(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /////////////
                mc.setPrevNextListeners(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Handle next click here
                        songs.get(songID).setVisible(false);
                        songAdapter.notifyDataSetChanged();
                        songID++;
                        if(songID==songs.size()){
                            videoView.setVideoPath(URL_SONGS);
                            videoView.requestFocus();
                            videoView.start();
                        }
                        if(songID<=songs.size()){
                            videoView.setVideoPath(URL_SONGS);
                            videoView.requestFocus();
                            videoView.start();
                            songAdapter.notifyDataSetChanged();
                            songs.get(songID).setVisible(true);

                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Handle previous click here
                    }
                });
                ////////////
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
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    private void startPlaying(final int position) {

    }

    @Override
    public void onBackPressed() {
        // stopPlaying();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}