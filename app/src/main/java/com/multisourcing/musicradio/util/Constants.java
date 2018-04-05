package com.multisourcing.musicradio.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.multisourcing.musicradio.R;

/**
 * Created by rifan on 13-Mar-16.
 */
public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.multisourcing.musicradio.action.main";
        public static String INIT_ACTION = "com.multisourcing.musicradio.action.init";
        public static String PREV_ACTION = "com.multisourcing.musicradio.action.prev";
        public static String PLAY_ACTION = "com.multisourcing.musicradio.action.play";
        public static String NEXT_ACTION = "com.multisourcing.musicradio.action.next";
        public static String STARTFOREGROUND_ACTION = "com.multisourcing.musicradio.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.multisourcing.musicradio.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_logo, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}