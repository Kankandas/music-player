package com.example.kankan.musicpractice;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaDescrambler;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView txtTabSong;
    ImageButton imgTabSong;
    ImageView imgSongPic;
    ListView lstView;
    RecyclerView recyclerView;
    ArrayList<String> arrayList;
    ArrayList<String> urilist;
    ArrayList<String> imgUriList;
    ArrayList<String> albumListForAlbum;
    ArrayList<String>albumListFromMedia;

    private final static int REQUEST_CODE_MOVE=100;
    private final static String UriString="Uri";
    private final static String UriList="UriList";
    private final static String SongList="SongList";
    private final static String Details="Details";
    private final static String PosMain="Pos";

    String str;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTabSong=(TextView)findViewById(R.id.txtTabSong);
        imgTabSong=(ImageButton)findViewById(R.id.imgTabSong);
        imgSongPic=(ImageView)findViewById(R.id.imgSongPic);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewKas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList=new ArrayList<>();
        urilist=new ArrayList<>();
        imgUriList=new ArrayList<>();
        albumListFromMedia=new ArrayList<>();
        albumListForAlbum=new ArrayList<>();

        getMusic();
        getUriImage();


        ImageAdapterMine imageAdapterMine=new ImageAdapterMine(MainActivity.this,imgUriList,albumListForAlbum,albumListFromMedia,arrayList,urilist);
        recyclerView.setAdapter(imageAdapterMine);


    }

    public void getMusic()
    {
        ContentResolver contentResolver=getContentResolver();

        Uri songsUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


        Cursor cursor=contentResolver.query(songsUri,null,null,null,null);

        if(cursor!=null && cursor.moveToFirst())
        {
            int SongTitle=cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songDuration=cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songData=cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumData=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do{
                String currentTitle=cursor.getString(SongTitle);
                String currentArtist=cursor.getString(songArtist);
                String path=cursor.getString(songData);
                String album=cursor.getString(albumData);
                albumListFromMedia.add(album);
                arrayList.add("Title:   "+currentTitle+"\nArtist:   "+currentArtist);
                urilist.add(Uri.fromFile(new File(path)).toString());
            }while (cursor.moveToNext());
        }
    }
    public void getUriImage()
    {
        ContentResolver contentResolver=getContentResolver();

        Uri uri= MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor cursor=contentResolver.query(uri,null,null,null,null);

        if(cursor!= null&& cursor.moveToFirst())
        {
            int imageColumn=cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int albumColumn=cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            do {
                String image=cursor.getString(imageColumn);
                String album=cursor.getString(albumColumn);
                albumListForAlbum.add(album);
                imgUriList.add(image);
            }while (cursor.moveToNext());
        }
    }
    public String getImageString(String str)
    {
        for(int i=0;i<albumListForAlbum.size();i++)
        {
            if(albumListForAlbum.get(i).equals(str))
            {
                return imgUriList.get(i);
            }
        }
        String strd="R.drawable.myapp";
        return strd;
    }
   public void setNotification(int pos)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this);

        builder.setSmallIcon(R.drawable.pause);
        builder.setContentTitle(arrayList.get(pos));
        builder.setContentText("Song Is Playins");

        Intent intent=new Intent(MainActivity.this,MainActivity.class);

        /*PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);*/

        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify("My Notify",123,builder.build());
    }

}
