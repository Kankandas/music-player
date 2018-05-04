package com.example.kankan.musicpractice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
/**/

public class PlayActivity extends AppCompatActivity  implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    TextView txtTitle;
    ArrayList<String> uriList;
    ArrayList<String> songList;
    ArrayList<String> imgUriList;
    ArrayList<String> albumListForAlbum;
    ArrayList<String>albumListFromMedia;
    static MediaPlayer mp;

    Button btnNext,btnPrev,btnStop;
    ImageView imgSong;
    SeekBar seekBar;
    int position=0;
    static int currentPosForTab=0;
    Thread thread;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnPrev=(Button)findViewById(R.id.btnPrev);
        btnStop=(Button)findViewById(R.id.btnPlay);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        imgSong=(ImageView)findViewById(R.id.imgSong);


        Bundle bundle = getIntent().getExtras();

        String passed = bundle.getString("Uri");
        String details = bundle.getString("Details");
        position = bundle.getInt("pos");
        //setNotification(position);
        songList = bundle.getStringArrayList("SongList");
        uriList = bundle.getStringArrayList("UriList");
        imgUriList=bundle.getStringArrayList("IMAGE_URI_LIST");
        albumListForAlbum=bundle.getStringArrayList("ALBUM_LIST_FOR_ALBUM");
        albumListFromMedia=bundle.getStringArrayList("ALBUM_LIST_FOR_MEDIA");
        String imageGet=bundle.getString("IMAGEGET");

        Uri uri = Uri.parse(passed);
        currentPosForTab=position;
        txtTitle.setText(details);
        if(mp!=null)
        {
            mp.stop();;
            mp.release();
        }
        mp=MediaPlayer.create(this,uri);
        if(imageGet !=null)
        {
            Bitmap bm=BitmapFactory.decodeFile(imageGet);
            imgSong.setImageBitmap(bm);
        }
        else
        {
            imgSong.setImageResource(R.drawable.myapp);
        }
        mp.start();
        updateProgressBar();
        btnPrev.setOnClickListener(PlayActivity.this);
        btnNext.setOnClickListener(PlayActivity.this);
        btnStop.setOnClickListener(PlayActivity.this);
        seekBar.setMax(mp.getDuration());
        thread =new Thread(){
            @Override
            public void run() {

                int duration=mp.getDuration();
                int currentPos=0;
                while(currentPos<duration)
                {
                    try
                    {
                        sleep(500);
                        currentPos=mp.getCurrentPosition();
                        seekBar.setProgress(currentPos);
                        handler.postDelayed(this,100);

                    }
                    catch (Exception e)
                    {
                        //e.printStackTrace();
                    }
                }

            }
        };

        seekBar.setOnSeekBarChangeListener(PlayActivity.this);
            }
    public void updateProgressBar() {
        handler.postDelayed(moveSeekBarThread, 100);
    }
    private Runnable moveSeekBarThread = new Runnable() {

        public void run() {
            if (mp.isPlaying()) {

                int mediaPos_new = mp.getCurrentPosition();
                int mediaMax_new = mp.getDuration();
                seekBar.setMax(mediaMax_new);
                seekBar.setProgress(mediaPos_new);

                handler.postDelayed(this, 100); // Looping the thread after 0.1
                // second
                // seconds
            }
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser)
        {
            mp.seekTo(progress);
            seekBar.setProgress(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mp.seekTo(seekBar.getProgress());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent1=new Intent(PlayActivity.this,MainActivity.class);
       // intent1.putExtra("GOTOTABURI",uriList.get(position));
        intent1.putExtra("GOTOTABSONG",songList.get(position));
       // Boolean isPlay=false;
        //if(mp.isPlaying())
        //{
        //    isPlay=true;
        //}
        //intent1.putExtra("TABPLAY",isPlay);
        //int currentPosition=mp.getCurrentPosition();
        //intent1.putExtra("TABCURRENTPOS",currentPosition);
        //intent1.putExtra("IMAGE_KAS_URI",imgUriList.get(position));
        setResult(RESULT_OK,intent1);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnNext:
                mp.stop();
                mp.release();
                position=(position+1)%songList.size();
                String strImage=albumListFromMedia.get(position);
                String getImage=getImageString(strImage);
                if(getImage !=null)
                {
                    Bitmap bitmap=BitmapFactory.decodeFile(getImage);
                    imgSong.setImageBitmap(bitmap);
                }
                else
                {
                    imgSong.setImageResource(R.drawable.myapp);
                }

                currentPosForTab=position;
                Uri uri=Uri.parse(uriList.get((position)).toString());
                mp=MediaPlayer.create(this,uri);
                txtTitle.setText(songList.get(position));
                mp.start();
                //RecentActivity.setValueToArrayList(songList.get(position));
                setNotification(position);
                updateProgressBar();
                seekBar.setMax(mp.getDuration());
                break;
            case R.id.btnPrev:
                mp.stop();
                mp.release();
                position=((position-1)<0? songList.size()-1:position-1)%songList.size();
                String strImage1=albumListFromMedia.get(position);
                String getImage1=getImageString(strImage1);
                if(getImage1 !=null)
                {
                    Bitmap bitmap1=BitmapFactory.decodeFile(getImage1);
                    imgSong.setImageBitmap(bitmap1);
                }
                else
                {
                    imgSong.setImageResource(R.drawable.myapp);
                }

                currentPosForTab=position;
                Uri uri1=Uri.parse(uriList.get((position)).toString());
                mp=MediaPlayer.create(this,uri1);
                txtTitle.setText(songList.get(position));
                mp.start();
                //RecentActivity.setValueToArrayList(songList.get(position));
                setNotification(position);
                updateProgressBar();
                seekBar.setMax(mp.getDuration());
                break;
            case R.id.btnPlay:
                if(mp.isPlaying())
                {
                    btnStop.setText("Play");
                    mp.pause();
                }
                else
                {
                    btnStop.setText("Pause");
                    mp.start();
                    updateProgressBar();
                }
                break;
        }
    }
    public void setNotification(int pos)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(PlayActivity.this);

        builder.setSmallIcon(R.drawable.pause);
        builder.setContentTitle(songList.get(pos));
        builder.setContentText("Song Is Playins");

        /*Intent intent=new Intent(PlayActivity.this,PlayActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(PlayActivity.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);*/
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify("Song Notify",1234,builder.build());
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
        return null;
    }
}
