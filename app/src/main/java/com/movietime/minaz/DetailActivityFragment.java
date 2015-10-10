package com.movietime.minaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
    final static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    TextView title;
    TextView overview;
    TextView yearReleased;
    TextView voteAverage;
    ImageView backdrop;
    Button markFavorite;
    LinearLayout root;
    Boolean isFavorite;

    MovieDataSource movieDataSource;


    public DetailActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDataSource = new MovieDataSource(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // if  fragment was started with arguments (in 2-pane view), get the movieInfo bundle from the arguments
        // otherwise, the fragment was inflated via the detail activity and we should get the bundle via the intent used to launch
        // the detail activity.
        Bundle movieInfo = getArguments();
        if (movieInfo == null) {
            movieInfo = getActivity().getIntent().getExtras();
        }
        final int movieID = movieInfo.getInt("movie_id", 0);
        final String movieTitle = movieInfo.getString("movie_title", null);
        final String movieOverview = movieInfo.getString("movie_overview", null);
        final String movieBackdropPath = movieInfo.getString("movie_backdrop_path", null);
        final String moviePosterPath = movieInfo.getString("movie_poster_path", null);
        final int movieVoteCnt = movieInfo.getInt("movie_vote_cnt", 0);
        final float movieVoteAvg = movieInfo.getFloat("movie_vote_avg", 5);
        final String movieReleaseDate = movieInfo.getString("movie_release_date", "2015");


        root = (LinearLayout) view.findViewById(R.id.movie_detail_root_linearlayout);

        if (movieID != 0) {
            title = (TextView) view.findViewById(R.id.movie_detail_title);
            overview = (TextView) view.findViewById(R.id.movie_detail_overview);
            backdrop = (ImageView) view.findViewById(R.id.movie_detail_backdrop);
            yearReleased = (TextView) view.findViewById(R.id.movie_detail_year_released);
            voteAverage = (TextView) view.findViewById(R.id.movie_detail_vote_avg);

            final ListView trailerListView = (ListView) view.findViewById(R.id.movie_detail_trailers_listview);
            final ListView reviewListView = (ListView) view.findViewById(R.id.movie_detail_reviews_listview);

            title.setText(movieTitle);
            overview.setText(movieOverview);
            voteAverage.setText(String.valueOf(movieVoteAvg).concat("/10"));

            String releaseDate = (movieReleaseDate.contains("-")) ?
                    movieReleaseDate.substring(0, movieReleaseDate.indexOf("-")): movieReleaseDate;
            yearReleased.setText(releaseDate);

            markFavorite = (Button) view.findViewById(R.id.movie_detail_mark_favorite);
            movieDataSource.openReadable();
            Movie favoriteMovie = movieDataSource.getMovieByApiId(movieID);
            movieDataSource.close();
            if (favoriteMovie != null) {
                isFavorite = true;
                markFavorite.setText(getString(R.string.movie_detail_remove_from_favorites,
                        "REMOVE FROM FAVORITES"));
            } else {
                isFavorite = false;
                markFavorite.setText(getString(R.string.movie_detail_set_as_favorite,
                        "SET AS FAVORITE"));
            }

            markFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movieDetails = new Movie();
                    movieDetails.setBackdropPath(movieBackdropPath);
                    movieDetails.setPosterPath(moviePosterPath);
                    movieDetails.setID(movieID);
                    movieDetails.setVoteCnt(movieVoteCnt);
                    movieDetails.setVoteAvg(movieVoteAvg);
                    movieDetails.setOverview(movieOverview);
                    movieDetails.setTitle(movieTitle);
                    movieDetails.setReleaseDate(movieReleaseDate);

                    movieDataSource.open();
                    if (isFavorite) {
                        movieDataSource.deleteMovie(movieDetails.getID());
                        markFavorite.setText(getString(R.string.movie_detail_set_as_favorite,
                                "SET AS FAVORITE"));
                    } else {
                        movieDataSource.createMovie(movieDetails);
                        markFavorite.setText(getString(R.string.movie_detail_remove_from_favorites,
                                "REMOVE FROM FAVORITES"));
                    }
                    movieDataSource.close();
                    isFavorite = !isFavorite;

                }
            });


            String absBackdropPath = MainActivityFragment.BASE_PIC_URL + movieBackdropPath;
            Log.v("backdrop path", absBackdropPath);
            Picasso.with(getActivity()).load(absBackdropPath).into(backdrop);

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
                                final TrailerListAdapter trailerListAdapter = new TrailerListAdapter(getActivity(),
                                        trailerList,
                                        R.layout.trailer_list_item);
                                trailerListView.setAdapter(trailerListAdapter);

                                trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Trailer trailer = trailerListAdapter.getItem(position);
                                        Intent viewTrailer = new Intent(Intent.ACTION_VIEW);
                                        String youtubeUrl = YOUTUBE_BASE_URL.concat(String.valueOf(trailer.getKey()));
                                        viewTrailer.setData(Uri.parse(youtubeUrl));
                                        getActivity().startActivity(viewTrailer);
                                    }
                                });

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

                                if (reviewList.size() > 0) {
                                    ReviewListAdapter reviewListAdapter = new ReviewListAdapter(getActivity(),
                                            reviewList,
                                            R.layout.review_list_item);
                                    reviewListView.setAdapter(reviewListAdapter);
                                } else {
                                    TextView noReviewsTextView = (TextView) view.findViewById(R.id.movie_detail_no_reviews_textview);
                                    noReviewsTextView.setVisibility(View.VISIBLE);
                                }
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
        void onItemSelected(Bundle bundle);
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

