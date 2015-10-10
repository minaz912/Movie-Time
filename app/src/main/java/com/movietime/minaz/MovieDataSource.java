package com.movietime.minaz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import static com.movietime.minaz.MovieDbContract.MovieEntry;


public class MovieDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MovieDbHelper dbHelper;
    private String[] allColumns = { MovieEntry.COLUMN_NAME_ENTRY_ID,
            MovieEntry.COLUMN_NAME_TITLE, MovieEntry.COLUMN_NAME_OVERVIEW, MovieEntry.COLUMN_NAME_VOTE_AVG,
            MovieEntry.COLUMN_NAME_VOTE_CNT, MovieEntry.COLUMN_NAME_BACKDROP_PATH,
            MovieEntry.COLUMN_NAME_POSTER_PATH, MovieEntry.COLUMN_NAME_RELEASE_DATE};


    public MovieDataSource(Context context) {
        dbHelper = new MovieDbHelper(context);
    }

    // Database methods
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openReadable() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Movie createMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_NAME_ENTRY_ID, movie.getID());
        values.put(MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.COLUMN_NAME_VOTE_AVG, movie.getVoteAvg());
        values.put(MovieEntry.COLUMN_NAME_VOTE_CNT, movie.getVoteCnt());
        values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        long insertId = database.insert(MovieEntry.TABLE_NAME, null,
                values);
        Log.d("movie database insertID", String.valueOf(insertId));
        Cursor cursor = database.query(MovieEntry.TABLE_NAME,
                allColumns, MovieEntry._ID + " = " + insertId, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            Movie newMovie = cursorToMovie(cursor);
            cursor.close();
            return newMovie;
        } else {
            Log.d("movie database error", "FAILURE INSERTING MOVIE");
            return null;
        }
    }

    public void deleteMovie(int apiId) {
        System.out.println("Movie deleted with id: " + apiId);
        database.delete(MovieEntry.TABLE_NAME, MovieEntry.COLUMN_NAME_ENTRY_ID
                + " = " + apiId, null);
    }

    public Movie getMovieByApiId(int id) {
        Cursor cursor = database.query(MovieEntry.TABLE_NAME,
                null, MovieEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursorToMovie(cursor);
        }
        return null;
    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        Cursor cursor = database.query(MovieEntry.TABLE_NAME,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            movies.add(movie);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return movies;
    }

    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setID(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ENTRY_ID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW)));
        movie.setVoteAvg(cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVG)));
        movie.setVoteCnt(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_CNT)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH)));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BACKDROP_PATH)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE)));
        return movie;
    }
    //

}
