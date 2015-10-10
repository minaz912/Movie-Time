package com.movietime.minaz.controllers.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.movietime.minaz.controllers.MainActivity;
import com.movietime.minaz.data.models.Movie;
import com.movietime.minaz.R;
import com.movietime.minaz.Utils;
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
        Log.v("Movie List Count", String.valueOf(movieList.size()));
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
        Uri posterUri = Uri .parse(Utils.BASE_PIC_URL).buildUpon()
                .appendEncodedPath(getItem(position).getPosterPath())
                .build();
        Log.v("posterUri", posterUri.toString());
        Picasso.with(this.context).load(posterUri).fit().into(holder.movieImageView);
        holder.movieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie selectedMovie = getItem(position);

                Bundle movieInfo = new Bundle();
                movieInfo.putInt("movie_id", selectedMovie.getID());
                movieInfo.putString("movie_title", selectedMovie.getTitle());
                movieInfo.putString("movie_backdrop_path", selectedMovie.getBackdropPath());
                movieInfo.putString("movie_overview", selectedMovie.getOverview());
                movieInfo.putString("movie_poster_path", selectedMovie.getPosterPath());
                movieInfo.putFloat("movie_vote_avg", (float) selectedMovie.getVoteAvg());
                movieInfo.putInt("movie_vote_cnt", selectedMovie.getVoteCnt());
                movieInfo.putString("movie_release_date", selectedMovie.getReleaseDate());

                ((MainActivity) context).onItemSelected(movieInfo);
            }
        });

        return row;
    }

    private class ViewHolder {
        ImageView movieImageView;
    }
}
