package com.movietime.minaz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class DetailActivityFragment extends Fragment {

    static String BASE_DETAIL_URL = "http://api.themoviedb.org/3/movie";
    TextView title;
    TextView overview;
    ImageView backdrop;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        title = (TextView) view.findViewById(R.id.movie_detail_title);
        overview = (TextView) view.findViewById(R.id.movie_detail_overview);
        backdrop = (ImageView) view.findViewById(R.id.movie_detail_backdrop);

        Bundle movieInfo = getActivity().getIntent().getExtras();
        int movieID = movieInfo.getInt("movie_id", 0);
        String movieTitle = movieInfo.getString("movie_title");
        String movieOverview = movieInfo.getString("movie_overview");
        String movieBackdropPath = movieInfo.getString("movie_backdrop_path");
        if (movieID != 0) {
            Toast.makeText(getActivity(), "Movie ID: " + movieID, Toast.LENGTH_LONG).show();

            title.setText(movieTitle);
            overview.setText(movieOverview);

            String absBackdropPath = MainActivityFragment.BASE_PIC_URL + movieBackdropPath;
            Picasso.with(getActivity()).load(absBackdropPath).fit().into(backdrop);
        } else {
            Toast.makeText(getActivity(), "Invalid movie ID", Toast.LENGTH_LONG).show();
        }

        return view;
    }
}

