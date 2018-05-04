package com.example.kankan.musicpractice;

import android.Manifest;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

public class TabActivity extends ActivityGroup {
    TabHost tabHost;
    static MediaPlayer mp;

    private static final int REQ_CODE_MOVE=100;

    int position=0;
    public boolean isAccessed(){
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return false;
            }
            else {
                return true;
            }
        }
        else{
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        isAccessed();

        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup(getLocalActivityManager());




        TabHost.TabSpec tabSpec=tabHost.newTabSpec("One");
        Intent intent1=new Intent(TabActivity.this,MainActivity.class);
        tabSpec.setContent(intent1);
        tabSpec.setIndicator("TRACKS");
        tabHost.addTab(tabSpec);

        tabSpec=tabHost.newTabSpec("Two");
        Intent intent2=new Intent(TabActivity.this,RecentActivity.class);
        tabSpec.setContent(intent2);
        tabSpec.setIndicator("RECENT");
        tabHost.addTab(tabSpec);

        tabSpec=tabHost.newTabSpec("Three");
        Intent intent3=new Intent(TabActivity.this,AlbumActivity.class);
        tabSpec.setIndicator("ALBUM");
        tabSpec.setContent(intent3);
        tabHost.addTab(tabSpec);


    }


}
