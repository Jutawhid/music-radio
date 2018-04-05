package com.multisourcing.musicradio;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoPlayerMain extends AppCompatActivity implements UniversalVideoView.VideoViewCallback{

    private static final String TAG = "MainActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
   //private static final String VIDEO_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";


    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    /////////////////
    private List<SongListModel> songs = new ArrayList<SongListModel>();

    private SongAdapter songAdapter;
    String URL_SONGS;

    ListView lvAlbum;

    NetworkImageView nivAlbumArt, nivAlbumArtBlur;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    int songID = 0;
    String URL;
    String singerID, categoryID;
    RelativeLayout llList;

    VideoView videoView;
    MediaController mc;

    //////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_main);

        /////////////////
        singerID = getIntent().getExtras().getString("SINGER_ID");
        categoryID = getIntent().getExtras().getString("CATEGORY_ID");
        URL ="http://demo1.zapbd.com/apps/songslist-service.php?singerid="+singerID+ "&category_id=" + categoryID;

        imageLoader = AppController.getInstance().getImageLoader();
        lvAlbum = (ListView)findViewById(R.id.lvSongList);
        nivAlbumArt = (NetworkImageView) findViewById(R.id.nivAlbumArt);
        videoView = (VideoView)findViewById(R.id.video_view);

        llList = (RelativeLayout) findViewById(R.id.llList);
        mc = new MediaController(this);
        songAdapter = new SongAdapter(this, songs);
        lvAlbum.setAdapter(songAdapter);


        loadSongs();


        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                songs.get(songID).setVisible(false);
                songAdapter.notifyDataSetChanged();
                songID = position;
                URL_SONGS= "http://demo1.zapbd.com/apps/content/mp3/" + songs.get(position).getSong().replace(" ","%20");
                //songs.get(songID).setVisible(true);

               /* mMediaController.setMediaPlayer(mVideoView);
                mVideoView.setMediaController(mMediaController);*/
                // Uri link = Uri.parse(URL_SONGS.replace(" ","%20"));
                mVideoView.setVideoPath(URL_SONGS);
                mVideoView.requestFocus();
                mVideoView.seekTo(mSeekPosition);
                mVideoView.start();
                songs.get(songID).setVisible(true);

                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        songs.get(songID).setVisible(false);
                        songID++;
                        if(songID >= songs.size()){
                            songID=0;
                            songAdapter.notifyDataSetChanged();
                            songs.get(songID).setVisible(true);
                        }
                        else{
                            setVideoAreaSize(songID);
                            songAdapter.notifyDataSetChanged();
                            songs.get(songID).setVisible(true);
                        }
                    }
                });


            }
        });
        ////////////////

        mVideoLayout = findViewById(R.id.video_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);

        mVideoView.setVideoViewCallback(this);
        mStart = (TextView) findViewById(R.id.start);

       /* mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSeekPosition > 0) {
                    mVideoView.seekTo(mSeekPosition);
                }
                mVideoView.start();
                mMediaController.setTitle("Big Buck Bunny");
            }
        });*/

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
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

                        setVideoAreaSize(songID);

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
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }

    public void setVideoAreaSize(final int position) {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                songID=position;
                URL_SONGS="http://demo1.zapbd.com/apps/content/mp3/" + songs.get(songID).getSong().replace(" ","%20");
                mVideoView.setVideoPath(URL_SONGS);
                mVideoView.requestFocus();
                mVideoView.seekTo(mSeekPosition);
                mVideoView.start();

                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        songs.get(songID).setVisible(false);
                        songID++;
                        if(songID >= songs.size()){
                            songID=0;
                            songAdapter.notifyDataSetChanged();
                            songs.get(songID).setVisible(true);
                        }
                        else{
                            setVideoAreaSize(songID);
                            songAdapter.notifyDataSetChanged();
                            songs.get(songID).setVisible(true);
                        }
                    }
                });

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
        }

        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

}
