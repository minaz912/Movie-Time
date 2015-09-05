package com.movietime.minaz;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Movie> movieList;
    int layoutResourceId;

    public MovieListAdapter(Context context, ArrayList<Movie> movieList, int layoutResourceId) {
        this.context = context;
        this.movieList = movieList;
        this.layoutResourceId = layoutResourceId;
        Log.v("MOVIE LIST COUNT", String.valueOf(movieList.size()));
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.movieImageView = (ImageView) row.findViewById(R.id.movieImageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Uri posterUri = Uri.parse(MainActivityFragment.BASE_PIC_URL).buildUpon()
                .appendEncodedPath(getItem(position).getBackdropPath())
                .build();
        Log.v("posterUri", posterUri.toString());
        Picasso.with(this.context).load(posterUri).fit().into(holder.movieImageView);

        return row;
    }

    private class ViewHolder {
        ImageView movieImageView;
    }
}
