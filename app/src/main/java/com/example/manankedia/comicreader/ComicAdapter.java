package com.example.manankedia.comicreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder>{


    private Context mCtx;
    private List<Comic> mComics;

    public ComicAdapter(Context ctx, List<Comic> comics) {
        mCtx = ctx;
        mComics = comics;
    }
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View v;
        if(((RecyclerView) parent).getLayoutManager() instanceof GridLayoutManager){
            v = inflater.inflate(R.layout.comic_item_grid, null);
        }else{
            v = inflater.inflate(R.layout.comic_item, null);
        }
        return new ComicViewHolder(v);



    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, final int position) {

        Comic comic = mComics.get(position);
        holder.mTextViewTitle.setText(comic.getTitle());
        holder.mTextViewDate.setText(comic.getDate());
        Glide.with(mCtx).load(comic.getUrl()).thumbnail(.1f).into(holder.mImageView);
        holder.mParentLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("Click","Clicked on" + mComics.get(position));

                Toast.makeText(mCtx, mComics.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mCtx, ComicAdsViewer.class);
                intent.putExtra("comicUrl", mComics.get(position).getUrl());
                intent.putExtra("title",mComics.get(position).getTitle());
                intent.putExtra("date",mComics.get(position).getDate());
                intent.putExtra("alt", mComics.get(position).getAlt());
                SharedPreferences sharedPreferences = mCtx.getSharedPreferences("ViewMode", mCtx.MODE_PRIVATE);

                int x = sharedPreferences.getInt("comicsOpened",0);
                if(sharedPreferences.getInt("currentViewMode",0) == 0){
                    x++;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("comicsOpened", x);
                    editor.apply();
                }
                intent.putExtra("comicsOpened",x);

                mCtx.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mComics.size();
    }

    class ComicViewHolder extends RecyclerView.ViewHolder{

        TextView mTextViewTitle, mTextViewDate;
        ImageView mImageView;
        LinearLayout mParentLayout;

        public ComicViewHolder(View itemView) {
            super(itemView);

            mTextViewDate = itemView.findViewById(R.id.dateTextView);
            mTextViewTitle = itemView.findViewById(R.id.comicName);
            mParentLayout = itemView.findViewById(R.id.itemLayout);
            mImageView = itemView.findViewById(R.id.comicImage);



        }
    }


}
