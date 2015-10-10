package com.movietime.minaz.data.models;

import java.util.ArrayList;


public class Movie {
    private boolean isAdult;
    private String backdropPath;
    private String posterPath;
    private ArrayList<Integer> genreIDs;
    private int id;
    private String language;
    private String title;
    private String overview;
    private float voteAvg;
    private int voteCnt;
    private String releaseDate;

    public Movie() {}

    public Movie(boolean isAdult, String backdropPath, String posterPath, ArrayList<Integer> genreIDs,
                 int id, String language, String title, String overview, float voteAvg, int voteCnt, String releaseDate) {
        this.isAdult = isAdult;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.genreIDs = genreIDs;
        this.id = id;
        this.language = language;
        this.title = title;
        this.overview = overview;
        this.voteAvg = voteAvg;
        this.voteCnt = voteCnt;
        this.releaseDate = releaseDate;
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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
