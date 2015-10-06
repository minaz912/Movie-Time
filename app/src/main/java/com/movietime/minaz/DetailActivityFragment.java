package com.movietime.minaz;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        root = (LinearLayout) view.findViewById(R.id.movie_detail_root_linearlayout);

        if (movieID != 0) {
            title = (TextView) view.findViewById(R.id.movie_detail_title);
            overview = (TextView) view.findViewById(R.id.movie_detail_overview);
            backdrop = (ImageView) view.findViewById(R.id.movie_detail_backdrop);

            final ListView trailerListView = (ListView) view.findViewById(R.id.movie_detail_trailers_listview);
            final ListView reviewListView = (ListView) view.findViewById(R.id.movie_detail_reviews_listview);

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
                                TrailerListAdapter trailerListAdapter = new TrailerListAdapter(getActivity(),
                                        trailerList,
                                        R.layout.trailer_list_item);
                                trailerListView.setAdapter(trailerListAdapter);
                                setListViewHeightBasedOnItems(trailerListView, trailerListAdapter);
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
                                ReviewListAdapter reviewListAdapter = new ReviewListAdapter(getActivity(),
                                        reviewList,
                                        R.layout.review_list_item);
                                reviewListView.setAdapter(reviewListAdapter);
                                setListViewHeightBasedOnItems(reviewListView, reviewListAdapter);
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
            TextView noMovieSelectedTextView = new TextView(getContext());
            noMovieSelectedTextView.setText(getString(R.string.movie_detail_no_movie_selected));
            noMovieSelectedTextView.setTextSize(40);
            noMovieSelectedTextView.setGravity(Gravity.CENTER);
            root.removeAllViews();
            root.setGravity(Gravity.CENTER);
            root.addView(noMovieSelectedTextView);
        }

        return view;
    }

    public interface Callback {
        public void onItemSelected(Bundle bundle);
    }


    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView, ListAdapter adapter) {

        if (adapter != null) {

            int numberOfItems = adapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = adapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}

