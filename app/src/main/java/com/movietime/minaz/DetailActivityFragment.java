package com.movietime.minaz;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailActivityFragment extends Fragment {

    static String BASE_DETAIL_URL = "http://api.themoviedb.org/3/movie";
    TextView title;
    TextView overview;
    ImageView backdrop;
    LinearLayout root;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        title = (TextView) view.findViewById(R.id.movie_detail_title);
        overview = (TextView) view.findViewById(R.id.movie_detail_overview);
        backdrop = (ImageView) view.findViewById(R.id.movie_detail_backdrop);

        root = (LinearLayout) view.findViewById(R.id.movie_detail_root_linearlayout);
        final ListView trailerListView = (ListView) view.findViewById(R.id.movie_detail_trailers_listview);
        final ListView reviewListView = (ListView) view.findViewById(R.id.movie_detail_reviews_listview);

        // if  fragment was started with arguments (in 2-pane view), get the movieInfo bundle from the arguments
        // otherwise, the fragment was inflated via the detail activity and we should get the bundle via the intent used to launch
        // the detail activity.
        Bundle movieInfo = getArguments();
        if (movieInfo == null) {
            movieInfo = getActivity().getIntent().getExtras();
        }
        int movieID = movieInfo.getInt("movie_id", 0);
        final String movieTitle = movieInfo.getString("movie_title", null);
        String movieOverview = movieInfo.getString("movie_overview", null);
        String movieBackdropPath = movieInfo.getString("movie_backdrop_path", null);
        if (movieID != 0) {
            Toast.makeText(getActivity(), "Movie ID: " + movieID, Toast.LENGTH_LONG).show();

            title.setText(movieTitle);
            overview.setText(movieOverview);

            String absBackdropPath = MainActivityFragment.BASE_PIC_URL + movieBackdropPath;
            Picasso.with(getActivity()).load(absBackdropPath).fit().into(backdrop);

            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024 * 10);
            Network network = new BasicNetwork(new HurlStack());

            RequestQueue requestQueue = new RequestQueue(cache, network);
            requestQueue.start();

            Uri trailerUri = Uri.parse(BASE_DETAIL_URL).buildUpon()
                    .appendPath(String.valueOf(movieID))
                    .appendPath("videos")
                    .appendQueryParameter("api_key", "81513cb04a6f257d51c40a4b89653f13")
                    .build();

            Uri reviewUri = Uri.parse(BASE_DETAIL_URL).buildUpon()
                    .appendPath(String.valueOf(movieID))
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", "81513cb04a6f257d51c40a4b89653f13")
                    .build();

            JsonObjectRequest trailerRequest = new JsonObjectRequest(
                    Request.Method.GET, trailerUri.toString(),
                    (String) null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("trailer response", response.toString());
                            try {
                                JSONArray movieTrailers = response.getJSONArray("results");

                                ArrayList<Trailer> trailerList = new ArrayList<>();

                                for (int i = 0; i < movieTrailers.length(); i++) {
                                    JSONObject trailer = movieTrailers.getJSONObject(i);
                                    trailerList.add(new Trailer(trailer.getString("id"), trailer.getString("name"),
                                            trailer.getString("key")));
                                }
                                TrailerListAdapter adapter = new TrailerListAdapter(getActivity(),
                                        trailerList,
                                        R.layout.trailer_list_item);
                                trailerListView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("trailer response", error.toString());
                        }
                    }
            );

            JsonObjectRequest reviewRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    reviewUri.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("review response", response.toString());
                            try {
                                JSONArray movieReviews = response.getJSONArray("results");
                                ArrayList<Review> reviewList = new ArrayList<>();

                                for (int i = 0; i < movieReviews.length(); i++) {
                                    JSONObject review = movieReviews.getJSONObject(i);
                                    reviewList.add(new Review(review.getString("id"),
                                            review.getString("author"),
                                            review.getString("content")));
                                }
                                ReviewListAdapter adapter = new ReviewListAdapter(getActivity(),
                                        reviewList,
                                        R.layout.review_list_item);
                                reviewListView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("review response", error.toString());
                        }
                    }
            );

            requestQueue.add(trailerRequest);
            requestQueue.add(reviewRequest);

        } else {
            Toast.makeText(getActivity(), "Invalid movie ID", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public interface Callback {
        public void onItemSelected(Bundle bundle);
    }
}

