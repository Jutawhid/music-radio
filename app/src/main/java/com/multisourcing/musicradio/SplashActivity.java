package com.multisourcing.musicradio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.multisourcing.musicradio.util.CheckInterNetConnection;

public class SplashActivity extends Activity {

    CheckInterNetConnection check;
    Boolean isInternetPresent = false;

    @SuppressWarnings("deprecation")
    public void showAlertDialogInterNet(final Context context, String title,
                                        String message, Boolean status) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//        alertDialog.setIcon((status) ? R.drawable.cd
//                : R.drawable.cd);
        alertDialog.setButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity.this.finish();
                context.startActivity(new Intent(
                        Settings.ACTION_WIRELESS_SETTINGS));

            }

        });
        alertDialog.show();

    }

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        check = new CheckInterNetConnection(getApplicationContext());
        isInternetPresent = check.isConnectingToInternet();
        if (isInternetPresent) {
            setContentView(R.layout.activity_splash);

            ImageView ivSplash = (ImageView) findViewById(R.id.ivSplash);

            Glide.with(this)
                    .load(R.drawable.anim_splash_bg)
                    .asGif()
                    .into(ivSplash);

            new Handler().postDelayed(new Runnable() {

				/*
                 * Showing splash screen with a timer. This will be useful when
				 * you want to show case your app logo / company
				 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this,
                            HomeActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            showAlertDialogInterNet(SplashActivity.this, "No Internet",
                    "No Internet Connection, Please go to settings", false);

        }

    }
}
