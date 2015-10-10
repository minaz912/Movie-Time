package com.movietime.minaz.data.models;

/**
 * Created by minaz on 28/09/15.
 */
public class Trailer {

    public Trailer(String id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    private String id;
    private String key;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
