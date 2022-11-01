package com.listintime.network;

import com.listintime.model.book.Books;
import com.listintime.model.game.Games;

import com.listintime.model.movies.Movies;
import com.listintime.model.series.Series;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkServices {

    @GET("search.json")
    Call<Books> getBooks(@Query("title") String title,
                         @Query("author") String author);

    @GET("games")
    Call<Games> getGames(@Query("search") String title, @Query("developpers") String company);

    @GET("movie")
    Call<Movies> getMovies(@Query("api_key") String apiKey, @Query("query") String title, @Query("language") String language, @Query("page") String page, @Query("include_adult") String adult);

    @GET("tv")
    Call<Series> getSeries(@Query("api_key") String apiKey, @Query("query") String title, @Query("language") String language, @Query("page") String page, @Query("include_adult") String adult);
}
