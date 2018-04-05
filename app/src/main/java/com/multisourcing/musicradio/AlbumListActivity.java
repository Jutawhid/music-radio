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
import com.multisourcing.musicradio.adapter.AlbumAdapter;
import com.multisourcing.musicradio.app.AppController;
import com.multisourcing.musicradio.model.AlbumModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlbumListActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;

    ListView lvAlbum;
    private List<AlbumModel> album = new ArrayList<AlbumModel>();
    private AlbumAdapter adapter;
    String singerID, categoryID;
    LinearLayout ll1;
    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        singerID = getIntent().getExtras().getString("SINGER_ID");
        categoryID = getIntent().getExtras().getString("CATEGORY_ID");
        URL ="http://demo1.zapbd.com/apps/albumlist-service.php?singerid="+singerID+"&category_id="+categoryID;
        setContentView(R.layout.activity_album);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lvAlbum = (ListView)findViewById(R.id.lvSongList);
        adapter = new AlbumAdapter(this,album);
        lvAlbum.setAdapter(adapter);
        loadAlbum();



        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                String URL_SONGS =  "http://demo1.zapbd.com/apps/songlist-service.php?albumid="+album.get(position).getAlbum_id();
                String URL_ALBUM_ART = "http://demo1.zapbd.com/apps/content/album/"+album.get(position).getImage();
                String URL_ALBUM_ART_BIG = "http://demo1.zapbd.com/apps/content/album/"+album.get(position).getImg_big();
                String URL_ALBUM_ART_BLUR = "http://demo1.zapbd.com/apps/content/album/"+album.get(position).getImg_big_blar();

                Intent intent = new Intent(AlbumListActivity.this, AlbumPlayActivity.class);
                intent.putExtra("URL_SONG",URL_SONGS);
                intent.putExtra("URL_ALBUM_ART",URL_ALBUM_ART);
                intent.putExtra("URL_ALBUM_ART_BIG",URL_ALBUM_ART_BIG);
                intent.putExtra("URL_ALBUM_ART_BLUR",URL_ALBUM_ART_BLUR);
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
                        AlbumModel albumList =new  AlbumModel();

                        Log.i(">>REQ", jObj.toString());
                        albumList.setAlbum_id(jObj.getString("album_id"));
                        albumList.setImage(jObj.getString("image"));
                        albumList.setName(jObj.getString("name"));
                        albumList.setImg_big(jObj.getString("img_big"));
                        albumList.setImg_big_blar(jObj.getString("img_big_blar"));

                        album.add(albumList);

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
