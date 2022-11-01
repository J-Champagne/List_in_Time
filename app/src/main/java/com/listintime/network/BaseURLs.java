package com.listintime.network;

public class BaseURLs {

    public static final String OPEN_LIBRARY_URL = "https://openlibrary.org/";
    public static final String RAWG_URL = "https://api.rawg.io/api/";
    public static final String THEMOVIEDB_URL = "https://api.themoviedb.org/3/search/";
    public static final String THEMOVIEDB_KEY = "b4c994350c131ffd4cc01990d68365bd";

    public static String getTVImageURL(String url){
        return "https://image.tmdb.org/t/p/w185" + url;
    }

    public static String getBookImageURL(int coverID){
        return "https://covers.openlibrary.org/b/ID/coverID-M.jpg".replace("coverID", String.valueOf(coverID));
    }

}
