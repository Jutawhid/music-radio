package com.multisourcing.musicradio.app;

/**
 * Created by RIFAN on 16-Feb-16.
 */

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.multisourcing.musicradio.util.LruBitmapCache;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    public static String TAG_KOBITA_IMAGES = "http://demo1.zapbd.com/apps/kobita-song-service.php";
    public static String TAG_ISLAMIC_IMAGES = "http://demo1.zapbd.com/apps/islamic-song-service.php";
    public static String TAG_VIDEO_IMAGES = "http://demo1.zapbd.com/apps/video-song-service.php";
    public static String TAG_POPULAR_IMAGES = "http://demo1.zapbd.com/apps/popular-song-service.php";
    public static String TAG_UNRELEASED_IMAGES = "http://demo1.zapbd.com/apps/unreleased-song-service.php";
    public static String TAG_NEW_RELEASED_IMAGES = "http://demo1.zapbd.com/apps/newreleased-song-service.php";
    public static String TAG_SINGER_IMAGE_URL = "http://demo1.zapbd.com/apps/content/singer/";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
