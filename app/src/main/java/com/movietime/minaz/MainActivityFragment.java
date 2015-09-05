package com.movietime.minaz;

import android.net.Uri;
import android.os.Bundle;
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

    static String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    static String BASE_PIC_URL = "http://image.tmdb.org/t/p/w185";
    static String params = "sort_by=popularity.desc&api_key=81513cb04a6f257d51c40a4b89653f13";
    GridView movieGridView;

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

        Uri movieListUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("sort_by", "popularity.desc")
                .appendQueryParameter("api_key", "81513cb04a6f257d51c40a4b89653f13")
                .build();

        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024 * 10);
        Network network = new BasicNetwork(new HurlStack());

        RequestQueue mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, movieListUri.toString(), (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("RESPONSE", response.toString());
                        ArrayList<Movie> movies = parseJsonResponse(response);

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

        mRequestQueue.add(jsObjRequest);
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
                        movieJson.getDouble("vote_average"), movieJson.getInt("vote_count"));
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
}
