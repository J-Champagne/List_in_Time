package com.listintime.model.book;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Books {

    @SerializedName("docs")
    @Expose
    private List<Book> books = null;

    public List<Book> getBooks() {
        return books;
    }

    public void setDocs(List<Book> books) {
        this.books = books;
    }

}