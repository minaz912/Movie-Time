package com.movietime.minaz;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivityFragment extends Fragment {

    //TODO: PUT YOUR API KEY HERE
    public static final String API_KEY = "XXXXXXXX";
    //
    static String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    static String BASE_PIC_URL = "http://image.tmdb.org/t/p/w185";
    GridView movieGridView;
    MovieDataSource movieDataSource;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        movieGridView = (GridView) view.findViewById(R.id.gridView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        boolean showFavsOnly = sharedPreferences.getBoolean(getString(R.string.pref_show_fav_key), false);

        if (! showFavsOnly) {
            String sortPreference = sharedPreferences.getString(
                    getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default));

            Uri movieListUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter("sort_by", sortPreference.concat(".desc"))
                    .appendQueryParameter("api_key", API_KEY)
                    .build();

            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024 * 10);
            Network network = new BasicNetwork(new HurlStack());

            RequestQueue mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();

            final ArrayList<Movie> movies = new ArrayList<>();
            MovieListAdapter adapter = new MovieListAdapter(getActivity(), movies,
                    R.layout.movie_list_item);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, buildMovieUri(1, sortPreference).toString(),
                            (String) null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("RESPONSE", response.toString());
                            movies.addAll(parseJsonResponse(response));

                            MovieListAdapter adapter = new MovieListAdapter(getActivity(), movies,
                                    R.layout.movie_list_item);
                            movieGridView.setAdapter(adapter);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("RESPONSE", error.toString());
                        }
                    });

            JsonObjectRequest jsObjRequestP2 = new JsonObjectRequest
                    (Request.Method.GET, buildMovieUri(2, sortPreference).toString(),
                            (String) null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("RESPONSE", response.toString());
                            movies.addAll(parseJsonResponse(response));
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("RESPONSE", error.toString());
                        }
                    });

            JsonObjectRequest jsObjRequestP3 = new JsonObjectRequest
                    (Request.Method.GET, buildMovieUri(3, sortPreference).toString(),
                            (String) null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("RESPONSE", response.toString());
                            movies.addAll(parseJsonResponse(response));
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("RESPONSE", error.toString());
                        }
                    });

            mRequestQueue.add(jsObjRequest);
            mRequestQueue.add(jsObjRequestP2);
            mRequestQueue.add(jsObjRequestP3);
        } else {
            movieDataSource = new MovieDataSource(getActivity());
            movieDataSource.openReadable();
            ArrayList<Movie> movies = movieDataSource.getAllMovies();
            MovieListAdapter adapter = new MovieListAdapter(getActivity(), movies,
                    R.layout.movie_list_item);
            movieGridView.setAdapter(adapter);

        }
    }

    private ArrayList<Movie> parseJsonResponse(JSONObject response) {
        ArrayList<Movie> movieList = new ArrayList<>();
        try {
            ArrayList<Integer> genreIDs = new ArrayList<>();
            JSONArray movieJsonArray = response.getJSONArray("results");
            for (int i = 0; i < movieJsonArray.length(); i++) {
                JSONObject movieJson = movieJsonArray.getJSONObject(i);
                Movie movie = new Movie(movieJson.getBoolean("adult"),
                        movieJson.getString("backdrop_path"), movieJson.getString("poster_path"),
                        null, movieJson.getInt("id"), movieJson.getString("original_language"),
                        movieJson.getString("title"), movieJson.getString("overview"),
                        (float) movieJson.getDouble("vote_average"), movieJson.getInt("vote_count"),
                        movieJson.getString("release_date"));
                // convert genre_ids to integer array
                JSONArray genreIDsJsonArray = movieJson.getJSONArray("genre_ids");
                for (int j = 0; j < genreIDsJsonArray.length(); j++) {
                    genreIDs.add(genreIDsJsonArray.getInt(j));
                }
                movie.setGenreIDs(genreIDs);
                movieList.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    private Uri buildMovieUri(int pageNum, String sortPreference) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("sort_by", sortPreference.concat(".desc"))
                .appendQueryParameter("page", String.valueOf(pageNum))
                .appendQueryParameter("api_key", API_KEY)
                .build();
    }
}
