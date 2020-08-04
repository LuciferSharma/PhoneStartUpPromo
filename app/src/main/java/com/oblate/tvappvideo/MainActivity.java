package com.oblate.tvappvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.time.Duration;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnPlay;
    VideoView videoPlayed;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        btnPlay = (Button) findViewById(R.id.btnPlay);
        videoPlayed = (VideoView) findViewById(R.id.videoPlay);
        mediaController = new MediaController(this);
        autoPlay();


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoPath = "android.resource://com.oblate.tvappvideo/" + R.raw.oblate;
                Uri uri = Uri.parse(videoPath);
                videoPlayed.setVideoURI(uri);
                videoPlayed.setMediaController(mediaController);
                mediaController.setAnchorView(videoPlayed);
                videoPlayed.start();

            }
        });

    }

    public void autoPlay() {
        String videoPath = "android.resource://com.oblate.tvappvideo/" + R.raw.oblate;
        Uri uri = Uri.parse(videoPath);
        videoPlayed.setVideoURI(uri);
        //no mediaController needed
        videoPlayed.start();
        long time;
        time = SystemClock.elapsedRealtime();
        int apprunTime=0;
        if( time <40000){
            apprunTime = 10000;
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


//                Intent _intent = new Intent(Intent.ACTION_MAIN);
//                _intent.addCategory(Intent.CATEGORY_HOME);
//                _intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(_intent);
                PackageManager pm = getPackageManager();
                ComponentName name = new ComponentName(MainActivity.this, MainActivity.class);
          //      pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                //  pm.clearPackagePreferredActivities(getPackageName());

                Intent i = new Intent("android.intent.action.MAIN");
                i.addCategory("android.intent.category.HOME");
                List<ResolveInfo> lst = pm.queryIntentActivities(i, 0);
                if (lst != null) {
                    for (ResolveInfo resolveInfo : lst) {
                        try {
                            Intent home = new Intent("android.intent.action.MAIN");
                            home.addCategory("android.intent.category.HOME");
                            home.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                            startActivity(home);
                            break;
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }

                // _intent.setClassName("com.android.launcher", "Launcher");
             //   pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                //   startActivity(_intent);
                //stoping the app, Must not show Dialog
                finish();
                System.exit(0);

            }
        }, apprunTime);

    }
}