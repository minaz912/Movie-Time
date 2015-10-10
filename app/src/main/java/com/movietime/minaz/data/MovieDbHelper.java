package com.movietime.minaz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "movies.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieDbContract.MovieEntry.TABLE_NAME + " (" +
                    MovieDbContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_ENTRY_ID + " INT, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_VOTE_AVG + " REAL, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_VOTE_CNT + " INT, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                    MovieDbContract.MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieDbContract.MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d("SQLITE CREATE SQL", SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
