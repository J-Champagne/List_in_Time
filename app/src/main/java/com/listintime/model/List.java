package com.listintime.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class List {

    @SerializedName("name")
    private String name;

    @SerializedName("medias")
    private ArrayList<Media> medias = new ArrayList<>();

    @SerializedName("date")
    private long date;

    public String getName() {
        return name;
    }

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMedias(ArrayList<Media> medias) {
        this.medias = medias;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
