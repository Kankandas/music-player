package com.example.kankan.musicpractice;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerForAlbumActivity extends RecyclerView.Adapter<RecyclerForAlbumActivity.InnerClass>{


    ArrayList<String> albumlist;
    ArrayList<String> urilist;
    ArrayList<String > artistList;
    ArrayList<String> imgAList;
    ArrayList<String> albulmListForImage;
    Context context;

    public RecyclerForAlbumActivity(Context context,ArrayList<String > albumlist,ArrayList<String> urilist,ArrayList<String > artistList
                                        ,ArrayList<String> imgAList,ArrayList<String> albulmListForImage)
    {
        this.context=context;
        this.albumlist=albumlist;
        this.urilist=urilist;
        this.artistList=artistList;
        this.imgAList=imgAList;
        this.albulmListForImage=albulmListForImage;
    }

    @NonNull
    @Override
    public InnerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.my_layout,parent,false);

        return new InnerClass(view,albumlist,urilist,artistList,imgAList,albulmListForImage);

    }

    @Override
    public void onBindViewHolder(@NonNull InnerClass holder, int position) {

        String str=albumlist.get(position);
        holder.txtAlbum11.setText(str);
        String str1=getImageString(str);
        if(str1 !=null)
        {
            Bitmap bm= BitmapFactory.decodeFile(str1);
            holder.imgAlbum11.setImageBitmap(bm);
        }
        else
        {
            holder.imgAlbum11.setImageResource(R.drawable.myapp);
        }

    }

    @Override
    public int getItemCount() {
        return albumlist.size();
    }

    public class InnerClass extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ArrayList<String> albumlistInner;
        ArrayList<String> urilistInner;
        ArrayList<String > artistListInner;
        ArrayList<String> imgAListInner;
        ArrayList<String> albulmListForImageInner;
        TextView txtAlbum11;
        ImageView imgAlbum11;

        public InnerClass(View itemView,ArrayList<String > albumlistInner,ArrayList<String> urilistInner
                            ,ArrayList<String > artistListInner,ArrayList<String> imgAListInner,ArrayList<String> albulmListForImageInner) {
            super(itemView);
            this.albumlistInner=albumlistInner;
            this.urilistInner=urilistInner;
            this.artistListInner=artistListInner;
            this.imgAListInner=imgAListInner;
            this.albulmListForImageInner=albulmListForImageInner;
            imgAlbum11=(ImageView)itemView.findViewById(R.id.imgLayout);
            txtAlbum11=(TextView)itemView.findViewById(R.id.txtLayout);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            String str=albumlist.get(position);
            urilist.clear();
            artistList.clear();
            getAlbumPertcularList(str);
            Intent intent1=new Intent(v.getContext(),AlbumArtistActivity.class);
            intent1.putExtra("URILIST",urilist);
            intent1.putExtra("ARTISTLIST",artistList);
            v.getContext().startActivity(intent1);
        }
        public void getAlbumPertcularList(String string)
        {
            ContentResolver contentResolver=context.getContentResolver();
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
    }



    public String getImageString(String str)
    {
        for(int i=0;i<albulmListForImage.size();i++)
        {
            if(albulmListForImage.get(i).equals(str))
            {
                return imgAList.get(i);
            }
        }
        return null;
    }
}
