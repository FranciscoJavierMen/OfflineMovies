package com.example.offlinemovies.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.offlinemovies.R;
import com.example.offlinemovies.data.local.entity.MovieEntity;
import com.example.offlinemovies.data.remote.ApiConstants;

import java.util.List;

public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<MovieEntity> mValues;

    MyMovieRecyclerViewAdapter(Context context, List<MovieEntity> mValues) {
        this.context = context;
        this.mValues = mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Glide.with(context)
                .load(ApiConstants.IMAGE_API_PREFIX.concat(holder.mItem.getPosterPath()))
                .into(holder.imgCover);
    }

    public void setData(List<MovieEntity> movieEntityList){
        this.mValues = movieEntityList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (mValues != null){
            return mValues.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView imgCover;
        MovieEntity mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            imgCover = view.findViewById(R.id.imgCover);
        }
    }
}
