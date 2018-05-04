package com.example.kankan.musicpractice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ImageAdapterMine extends RecyclerView.Adapter<ImageAdapterMine.MyInnerClass> {

    Context context;
    ArrayList<String> imgUriList;
    ArrayList<String> albumListForAlbum;
    ArrayList<String> albumListForMedia;
    ArrayList<String> arrayList;
    ArrayList<String> urilist;
    private final static String UriString="Uri";
    private final static String UriList="UriList";
    private final static String SongList="SongList";
    private final static String Details="Details";
    private final static String PosMain="Pos";

    public ImageAdapterMine(Context context,ArrayList<String> imgUriList,ArrayList<String> albumListForAlbum,ArrayList<String> albumListForMedia,
                            ArrayList<String> arrayList,ArrayList<String > urilist)
    {
        this.context=context;
        this.imgUriList=imgUriList;
        this.albumListForAlbum=albumListForAlbum;
        this.albumListForMedia=albumListForMedia;
        this.arrayList=arrayList;
        this.urilist=urilist;
    }


    @NonNull
    @Override
    public MyInnerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.my_layout,parent,false);
        return new MyInnerClass(view,imgUriList,albumListForAlbum,albumListForMedia,arrayList,urilist);
    }
    public class MyInnerClass extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ArrayList<String> imgUriListInner;
        ArrayList<String> albumListForAlbumInner;
        ArrayList<String> albumListForMediaInner;
        ArrayList<String> arrayListInner;
        ArrayList<String> urilistInner;
        TextView txtViwe222;
        ImageView imgView222;
        public MyInnerClass(View itemView,ArrayList<String> imgUriListInner,ArrayList<String> albumListForAlbumInner,ArrayList<String> albumListForMediaInner
                ,ArrayList<String> arrayListInner,ArrayList<String> urilistInner) {
            super(itemView);
            this.albumListForAlbumInner=albumListForAlbumInner;
            this.albumListForMediaInner=albumListForMediaInner;
            this.arrayListInner=arrayListInner;
            this.imgUriListInner=imgUriListInner;
            this.urilistInner=urilistInner;
            txtViwe222=(TextView)itemView.findViewById(R.id.txtLayout);
            imgView222=(ImageView)itemView.findViewById(R.id.imgLayout);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            Intent intent=new Intent(v.getContext(),PlayActivity.class);
            intent.putExtra("Pos",position);
            intent.putExtra(Details,arrayList.get(position));
            intent.putExtra(SongList,arrayList);
            intent.putExtra(UriList,urilist);
            intent.putExtra(UriString,urilist.get(position));
            String str=albumListForMediaInner.get(position);
            String strImageGet=getImageString(str);
            intent.putExtra("IMAGEGET",strImageGet);
            intent.putExtra("ALBUM_LIST_FOR_ALBUM",albumListForAlbumInner);
            intent.putExtra("ALBUM_LIST_FOR_MEDIA",albumListForMediaInner);
            intent.putExtra("IMAGE_URI_LIST",imgUriListInner);
            v.getContext().startActivity(intent);


        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyInnerClass holder, int position) {

        String str=arrayList.get(position);

        String strget=albumListForMedia.get(position);
        String strput=getImageString(strget);
        holder.txtViwe222.setText(str);
        if(strput !=null) {
            Bitmap bm = BitmapFactory.decodeFile(strput);
            holder.imgView222.setImageBitmap(bm);
        }
        else{
            holder.imgView222.setImageResource(R.drawable.myapp);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
