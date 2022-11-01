package com.listintime.util;

public class SetFavoriteEvent {
    public boolean favorite;
    public int position;

    public SetFavoriteEvent(boolean favorite, int position){
        this.favorite = favorite;
        this.position = position;
    }
}
