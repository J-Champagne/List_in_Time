package com.listintime.util;

public class MediaType {
    public static final String BOOK = "book";
    public static final String MOVIE = "movie";
    public static final String SERIES = "series";
    public static final String GAME = "game";

    private static String currentMediaFragment;

    public static String getCurrentMediaFragment() {
        return currentMediaFragment;
    }

    public static void setCurrentMediaFragment(String currentMediaFragment) {
        MediaType.currentMediaFragment = currentMediaFragment;
    }
}
