package com.movietime.minaz;

import android.provider.BaseColumns;

/**
 * Created by minaz on 08/10/15.
 */
public class MovieDbContract {

    public MovieDbContract() {}



    /* Inner class that defines the table contents */
    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_ENTRY_ID = "api_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_VOTE_AVG = "vote_avg";
        public static final String COLUMN_NAME_VOTE_CNT = "vote_cnt";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_BACKDROP_PATH = "bdrop_path";
    }

}
