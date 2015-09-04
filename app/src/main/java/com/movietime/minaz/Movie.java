package com.movietime.minaz;

import java.util.ArrayList;

/**
 * Created by minaz on 01/09/15.
 */
public class Movie {
    private boolean isAdult;
    private String backdropPath;
    private ArrayList<Integer> genreIDs;
    private int id;
    private String language;
    private String title;
    private String overview;
    private double voteAvg;
    private int voteCnt;

    public Movie(boolean isAdult, String backdropPath, ArrayList<Integer> genreIDs,
                 int id, String language, String title, String overview, double voteAvg, int voteCnt) {
        this.isAdult = isAdult;
        this.backdropPath = backdropPath;
        this.genreIDs = genreIDs;
        this.id = id;
        this.language = language;
        this.title = title;
        this.overview = overview;
        this.voteAvg = voteAvg;
        this.voteCnt = voteCnt;
    }

    public ArrayList<Integer> getGenreIDs() {
        return genreIDs;
    }

    public void setGenreIDs(ArrayList<Integer> genreIDs) {
        this.genreIDs = genreIDs;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(float voteAvg) {
        this.voteAvg = voteAvg;
    }

    public int getVoteCnt() {
        return voteCnt;
    }

    public void setVoteCnt(int voteCnt) {
        this.voteCnt = voteCnt;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }
}
