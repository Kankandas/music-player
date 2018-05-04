package com.example.kankan.musicpractice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AlbumArtistActivity extends AppCompatActivity {

    ListView lstViewALBUM;
    static MediaPlayer mediaPlayer;
    ArrayList<String> urilist;
    ArrayList<String> artistList;
    int pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_artist);

        urilist=new ArrayList<>();
        artistList=new ArrayList<>();
       urilist.clear();
       artistList.clear();


        lstViewALBUM=(ListView)findViewById(R.id.lstViewALBUM);

        Bundle bundle=getIntent().getExtras();
        urilist=bundle.getStringArrayList("URILIST");
        artistList=bundle.getStringArrayList("ARTISTLIST");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AlbumArtistActivity.this,R.layout.my_layout,R.id.txtLayout,artistList);

        lstViewALBUM.setAdapter(arrayAdapter);

        lstViewALBUM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AlbumArtistActivity.this,PlayActivity.class);
                intent.putExtra("pos",position);
                intent.putExtra("Details",artistList.get(position));
                intent.putExtra("SongList",artistList);
                intent.putExtra("UriList",urilist);
                intent.putExtra("Uri",urilist.get(position));
                startActivity(intent);
            }
        });


    }
}
