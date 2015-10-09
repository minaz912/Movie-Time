package com.movietime.minaz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.movietime.minaz.MovieDbContract.MovieEntry;


public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "movies.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                    MovieEntry.COLUMN_NAME_ENTRY_ID + " INT, " +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                    MovieEntry.COLUMN_NAME_VOTE_AVG + " REAL, " +
                    MovieEntry.COLUMN_NAME_VOTE_CNT + " INT, " +
                    MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
                    MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

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
