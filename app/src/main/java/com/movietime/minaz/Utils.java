package com.movietime.minaz;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


public class Utils {
    //TODO: PUT YOUR API KEY HERE
    public static final String API_KEY = "XXXXXXXX";
    //
    public static String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    public static String BASE_PIC_URL = "http://image.tmdb.org/t/p/w185";
    public static String BASE_DETAIL_URL = "http://api.themoviedb.org/3/movie";
    public static String BASE_BACKDROP_PIC_URL = "http://image.tmdb.org/t/p/w300";
    public final static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

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

    public static Uri buildMovieUri(int pageNum, String sortPreference) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("sort_by", sortPreference.concat(".desc"))
                .appendQueryParameter("page", String.valueOf(pageNum))
                .appendQueryParameter("api_key", API_KEY)
                .build();
    }
}
