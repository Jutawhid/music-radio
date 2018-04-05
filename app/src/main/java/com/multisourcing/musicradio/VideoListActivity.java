package com.multisourcing.musicradio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.multisourcing.musicradio.adapter.SongAdapter;
import com.multisourcing.musicradio.app.AppController;
import com.multisourcing.musicradio.model.SongListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoListActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;
    int songID = 0;
    ListView lvAlbum;
    private List<SongListModel> songs = new ArrayList<SongListModel>();
    private SongAdapter adapter;
    String singerID, categoryID;
    LinearLayout ll1;
    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //URL = getIntent().getExtras().getString("URL");
        singerID = getIntent().getExtras().getString("SINGER_ID");
        categoryID = getIntent().getExtras().getString("CATEGORY_ID");
        URL ="http://demo1.zapbd.com/apps/songslist-service.php?singerid="+singerID+ "&category_id=" + categoryID;
        setContentView(R.layout.activity_album_video_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lvAlbum = (ListView)findViewById(R.id.lvSongList);
        adapter = new SongAdapter(this,songs);
        lvAlbum.setAdapter(adapter);
        loadAlbum();

        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                songs.get(songID).setVisible(false);
                adapter.notifyDataSetChanged();
                songID = position;
                String URL_SONGS= "http://demo1.zapbd.com/apps/content/mp3/" + songs.get(position).getSong();
                songs.get(songID).setVisible(true);

                Intent intent = new Intent(VideoListActivity.this, VideoPlayerMain.class);
                intent.putExtra("URL_SONG",URL_SONGS);

                startActivity(intent);

            }
        });

    }
    public void loadAlbum(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {

                for(int i=0; i<response.length();i++){
                    try {
                        JSONObject jObj = response.getJSONObject(i);
                        SongListModel albumList =new  SongListModel();

                        Log.i(">>REQ", jObj.toString());

                        albumList.setSong(jObj.getString("song"));

                        songs.add(albumList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error) {

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
