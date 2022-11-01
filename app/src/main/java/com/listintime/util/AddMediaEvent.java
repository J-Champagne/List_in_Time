package com.listintime.util;

public class AddMediaEvent<T> {
    public T media;

    public AddMediaEvent(T media){
        this.media = media;
    }
}