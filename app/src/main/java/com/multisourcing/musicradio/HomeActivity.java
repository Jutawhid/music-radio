package com.multisourcing.musicradio;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.multisourcing.musicradio.adapter.IslamicGridElementAdapter;
import com.multisourcing.musicradio.adapter.KobitaGridElementAdapter;
import com.multisourcing.musicradio.adapter.NewReleasedGridElementAdapter;
import com.multisourcing.musicradio.adapter.PopularGridElementAdapter;
import com.multisourcing.musicradio.adapter.UnreleasedGridElementAdapter;
import com.multisourcing.musicradio.adapter.VideoGridElementAdapter;
import com.multisourcing.musicradio.app.AppController;
import com.multisourcing.musicradio.model.IslamicImageModel;
import com.multisourcing.musicradio.model.KobitaImageModel;
import com.multisourcing.musicradio.model.NewReleasedImageModel;
import com.multisourcing.musicradio.model.PopularImageModel;
import com.multisourcing.musicradio.model.UnreleasedImageModel;
import com.multisourcing.musicradio.model.VideoImageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HorizontalGridView newHorizontalGridView,popularHorizontalGridView,unreleasedHorizontalGridView,videoHorizontalGridView,islamicHorizontalGridView,kobitaHorizontalGridView;
    KobitaGridElementAdapter kobitaAdapter;
    IslamicGridElementAdapter islamicAdapter;
    VideoGridElementAdapter videoAdapter;
    PopularGridElementAdapter popularAdapter;
    UnreleasedGridElementAdapter unreleasedGridElementAdapter;
    NewReleasedGridElementAdapter newReleasedGridElementAdapter;
    List<PopularImageModel> popularimageModelList = new ArrayList<PopularImageModel>();
    List<UnreleasedImageModel> unreleasedimageModelList = new ArrayList<UnreleasedImageModel>();
    List<NewReleasedImageModel> newreleasedimageModelList = new ArrayList<NewReleasedImageModel>();
    List<VideoImageModel> videoimageModelList = new ArrayList<VideoImageModel>();
    List<IslamicImageModel> islamicimageModelList = new ArrayList<IslamicImageModel>();
    List<KobitaImageModel> kobitaimageModelList = new ArrayList<KobitaImageModel>();
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        kobitaHorizontalGridView = (HorizontalGridView) findViewById(R.id.gridViewKobita);
        islamicHorizontalGridView = (HorizontalGridView) findViewById(R.id.gridViewIslamic);
        videoHorizontalGridView = (HorizontalGridView) findViewById(R.id.gridViewVideo);
        newHorizontalGridView = (HorizontalGridView) findViewById(R.id.gridViewNewReleased);
        popularHorizontalGridView = (HorizontalGridView) findViewById(R.id.gridViewPopular);
        unreleasedHorizontalGridView = (HorizontalGridView) findViewById(R.id.gridViewUnreleased);
        kobitaAdapter = new KobitaGridElementAdapter(this, kobitaimageModelList);
        islamicAdapter = new IslamicGridElementAdapter(this, islamicimageModelList);
        videoAdapter = new VideoGridElementAdapter(this, videoimageModelList);
        popularAdapter = new PopularGridElementAdapter(this, popularimageModelList);
        unreleasedGridElementAdapter = new UnreleasedGridElementAdapter(this, unreleasedimageModelList);
        newReleasedGridElementAdapter = new NewReleasedGridElementAdapter(this, newreleasedimageModelList);

        newHorizontalGridView.setAdapter(newReleasedGridElementAdapter);
        popularHorizontalGridView.setAdapter(popularAdapter);
        unreleasedHorizontalGridView.setAdapter(unreleasedGridElementAdapter);
        videoHorizontalGridView.setAdapter(videoAdapter);
        islamicHorizontalGridView.setAdapter(islamicAdapter);
        kobitaHorizontalGridView.setAdapter(kobitaAdapter);
        parsePopularImages();
        parseUnreleasedImages();
        parseNewReleasedImages();
        parseVideoImages();
        parseIslamicImages();
        parseKobitaImages();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public void onBackPressed () {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected (MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void parsePopularImages () {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppController.TAG_POPULAR_IMAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        PopularImageModel model = new PopularImageModel();

                        model.setSinger_id(obj.getString("singer_id"));
                        model.setImage(obj.getString("image"));
                        model.setName(obj.getString("name"));

                        popularimageModelList.add(model);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                popularAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void parseUnreleasedImages () {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppController.TAG_UNRELEASED_IMAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        UnreleasedImageModel model = new UnreleasedImageModel();

                        model.setSinger_id(obj.getString("singer_id"));
                        model.setImage(obj.getString("image"));
                        model.setName(obj.getString("name"));

                        unreleasedimageModelList.add(model);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                unreleasedGridElementAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void parseNewReleasedImages () {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppController.TAG_NEW_RELEASED_IMAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        NewReleasedImageModel model = new NewReleasedImageModel();

                        model.setSinger_id(obj.getString("singer_id"));
                        model.setImage(obj.getString("image"));
                        model.setName(obj.getString("name"));

                        newreleasedimageModelList.add(model);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                newReleasedGridElementAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    public void parseVideoImages () {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppController.TAG_VIDEO_IMAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        VideoImageModel model = new VideoImageModel();

                        model.setSinger_id(obj.getString("singer_id"));
                        model.setImage(obj.getString("image"));
                        model.setName(obj.getString("name"));

                        videoimageModelList.add(model);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               videoAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    public void parseIslamicImages () {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppController.TAG_ISLAMIC_IMAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        IslamicImageModel model = new IslamicImageModel();

                        model.setSinger_id(obj.getString("singer_id"));
                        model.setImage(obj.getString("image"));
                        model.setName(obj.getString("name"));

                        islamicimageModelList.add(model);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                islamicAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    public void parseKobitaImages () {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppController.TAG_KOBITA_IMAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        KobitaImageModel model = new KobitaImageModel();

                        model.setSinger_id(obj.getString("singer_id"));
                        model.setImage(obj.getString("image"));
                        model.setName(obj.getString("name"));

                        kobitaimageModelList.add(model);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                kobitaAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
