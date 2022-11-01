package com.listintime.model.book;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {

    @SerializedName("title_suggest")
    @Expose
    private String titleSuggest;
    @SerializedName("cover_i")
    @Expose
    private int coverI;
    @SerializedName("author_key")
    @Expose
    private List<String> authorKey = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("first_publish_year")
    @Expose
    private int firstPublishYear;
    @SerializedName("author_name")
    @Expose
    private List<String> authorName = null;
    @SerializedName("language")
    @Expose
    private List<String> language = null;
    @SerializedName("last_modified_i")
    @Expose
    private int lastModifiedI;
    @SerializedName("first_sentence")
    @Expose
    private List<String> firstSentence = null;
    @SerializedName("publish_year")
    @Expose
    private List<Integer> publishYear = null;
    @SerializedName("publish_date")
    @Expose
    private List<String> publishDate = null;

    public String getTitleSuggest() {
        return titleSuggest;
    }

    public void setTitleSuggest(String titleSuggest) {
        this.titleSuggest = titleSuggest;
    }

    public int getCoverI() {
        return coverI;
    }

    public void setCoverI(int coverI) {
        this.coverI = coverI;
    }

    public List<String> getAuthorKey() {
        return authorKey;
    }

    public void setAuthorKey(List<String> authorKey) {
        this.authorKey = authorKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(int firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public List<String> getAuthorName() {
        return authorName;
    }

    public void setAuthorName(List<String> authorName) {
        this.authorName = authorName;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public int getLastModifiedI() {
        return lastModifiedI;
    }

    public void setLastModifiedI(int lastModifiedI) {
        this.lastModifiedI = lastModifiedI;
    }

    public List<String> getFirstSentence() {
        return firstSentence;
    }

    public void setFirstSentence(List<String> firstSentence) {
        this.firstSentence = firstSentence;
    }

    public List<Integer> getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(List<Integer> publishYear) {
        this.publishYear = publishYear;
    }

    public List<String> getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(List<String> publishDate) {
        this.publishDate = publishDate;
    }

}