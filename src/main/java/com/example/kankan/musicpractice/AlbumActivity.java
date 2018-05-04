package com.example.kankan.musicpractice;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AlbumActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    ArrayList<String> albumlist;
    ArrayList<String> urilist;
    ArrayList<String > artistList;
    ArrayList<String> imgAList;
    ArrayList<String> albulmListForImage;
    RecyclerView reView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        reView=(RecyclerView)findViewById(R.id.reView);
        reView.setLayoutManager(new LinearLayoutManager(this));
        albumlist=new ArrayList<>();
        urilist=new ArrayList<>();
        artistList=new ArrayList<>();
        imgAList=new ArrayList<>();
        albulmListForImage=new ArrayList<>();
        getList();
        getFinalArtistList();
        getFinalArtistList();
        getImageForAlbum();

        RecyclerForAlbumActivity recyclerForAlbumActivity=new RecyclerForAlbumActivity(AlbumActivity.this,albumlist,urilist,artistList,imgAList,albulmListForImage);

        reView.setAdapter(recyclerForAlbumActivity);





    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str=albumlist.get(position);
        urilist.clear();
        artistList.clear();
        getAlbumPertcularList(str);
        Intent intent=new Intent(AlbumActivity.this,AlbumArtistActivity.class);
        intent.putExtra("URILIST",urilist);
        intent.putExtra("ARTISTLIST",artistList);
        startActivity(intent);

    }

    public void getAlbumPertcularList(String string)
    {
        ContentResolver contentResolver=getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection={ MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST};
        Cursor cursor=contentResolver.query(uri,projection, MediaStore.Audio.Media.ALBUM+" LIKE ? ",new String[]{string},null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do{
                int dataColumn=cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int artistColumn=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                String Data=cursor.getString(dataColumn);
                String artist=cursor.getString(artistColumn);
                urilist.add(Data);
                artistList.add(artist);

            }while (cursor.moveToNext());
        }
    }

    public void getList()
    {
        ContentResolver contentResolver=getContentResolver();

        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor=contentResolver.query(uri,null,null,null,null);

        if(cursor !=null && cursor.moveToFirst())
        {
            do {
                int artistColumn=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                String artist=cursor.getString(artistColumn);
                albumlist.add(artist);
            }while(cursor.moveToNext());
        }
    }
    public void getFinalArtistList()
    {
        for(int i=0;i<albumlist.size();i++)
        {
            for(int j=i+1;j<albumlist.size();j++)
            {
                if(albumlist.get(i).equals(albumlist.get(j)))
                {
                    albumlist.remove(albumlist.get(j));
                }
            }
        }
    }
    public void getImageForAlbum()
    {
        ContentResolver contentResolver=getContentResolver();

        Uri uri= MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor cursor=contentResolver.query(uri,null,null,null,null);

        if(cursor !=null && cursor.moveToFirst())
        {
            int imgColumn=cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int albumColumn=cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            do {
                String imgString=cursor.getString(imgColumn);
                String albumString=cursor.getString(albumColumn);
                albulmListForImage.add(albumString);
                imgAList.add(imgString);
            }while(cursor.moveToNext());
        }
    }

}
