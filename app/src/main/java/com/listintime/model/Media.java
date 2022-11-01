package com.listintime.model;

import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("name")
    private String name;

    @SerializedName("information")
    private String information;

    @SerializedName("comment")
    private String comment;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("rating")
    private int rating;

    @SerializedName("favorite")
    private boolean favorite = false;

    @SerializedName("addedDate")
    private long date;

    public Media(){

    }

    public Media(String name, String information, String imageURL, String comment, int rating, boolean favorite, long date) {
        this.name = name;
        this.information = information;
        this.comment = comment;
        this.rating = rating;
        this.favorite = favorite;
        this.imageURL = imageURL;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
