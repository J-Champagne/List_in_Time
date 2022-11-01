package com.listintime.ui.media;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.listintime.model.Genre;
import com.listintime.model.Media;
import com.listintime.model.book.Book;
import com.listintime.model.book.Books;
import com.listintime.model.game.Game;
import com.listintime.model.game.Games;
import com.listintime.model.movies.Movie;
import com.listintime.model.movies.Movies;
import com.listintime.model.series.Serie;
import com.listintime.model.series.Series;
import com.listintime.network.BaseURLs;
import com.listintime.network.NetworkServices;
import com.listintime.network.RetrofitClient;
import com.listintime.util.AddMediaEvent;
import com.listintime.util.DeleteEventMedia;
import com.listintime.util.ListNameEvent;
import com.listintime.util.MediaType;
import com.listintime.util.RatingEvent;
import com.listintime.util.SetCommentEvent;
import com.listintime.util.SetFavoriteEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaListViewModel<T> extends AndroidViewModel {

    private MutableLiveData<ArrayList<Media>> mMedias;
    private ArrayList<Media> medias = new ArrayList<>();
    private MutableLiveData<ArrayList<T>> mDownloadMedias;
    private MutableLiveData<Boolean> errorMessage;
    private SharedPreferences prefs;
    private Gson gson;
    private String listName;

    public MediaListViewModel(Application application) {
        super(application);
        EventBus.getDefault().register(this);
        ListNameEvent listNameEvent = EventBus.getDefault().getStickyEvent(ListNameEvent.class);
        if(listNameEvent != null){
            listName = listNameEvent.listName;
            Log.i("List name", listName);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        gson = new GsonBuilder().create();
        mMedias = new MutableLiveData<>();
        mDownloadMedias = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        errorMessage.setValue(false);
        init();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void ratingEventReceived(RatingEvent ratingEvent) {
        medias.get(ratingEvent.position).setRating(ratingEvent.rating);
        prefs.edit().putString(listName, gson.toJson(medias)).apply();
        Log.i("Rating", medias.get(ratingEvent.position).getRating() + "");
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void SetCommentEventReceived(SetCommentEvent setCommentEvent) {
        medias.get(setCommentEvent.position).setComment(setCommentEvent.comment);
        prefs.edit().putString(listName, gson.toJson(medias)).apply();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void deleteEventReceived(DeleteEventMedia deleteEventMedia) {
        medias.remove(deleteEventMedia.position);
        prefs.edit().putString(listName, gson.toJson(medias)).apply();
        mMedias.setValue(medias);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void setFavoriteEventReceived(SetFavoriteEvent setFavoriteEvent) {
        Log.i("TEST", "FAVORITE");
        medias.get(setFavoriteEvent.position).setFavorite(setFavoriteEvent.favorite);
        prefs.edit().putString(listName, gson.toJson(medias)).apply();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void itemAddEventReceived(AddMediaEvent addMediaEvent) {
        switch (MediaType.getCurrentMediaFragment()) {
            case "book":
                Book book = (Book) addMediaEvent.media;
                medias.add(new Media(book.getTitle(), book.getAuthorName().get(0), BaseURLs.getBookImageURL(book.getCoverI()), "", 0, false, System.currentTimeMillis()));
                prefs.edit().putString(listName, gson.toJson(medias)).apply();
                break;
            case "movie":
                Movie movie = (Movie) addMediaEvent.media;
                medias.add(new Media(movie.getTitle(), Genre.genres.get(movie.getGenreIds().get(0)), BaseURLs.getTVImageURL(movie.getPosterPath()), "", 0, false, System.currentTimeMillis()));
                break;
            case "series":
                Serie serie = (Serie) addMediaEvent.media;
                medias.add(new Media(serie.getName(), Genre.genres.get(serie.getGenreIds().get(0)), BaseURLs.getTVImageURL(serie.getPosterPath()), "", 0, false, System.currentTimeMillis()));
                break;
            case "game":
                Game game = (Game) addMediaEvent.media;
                medias.add(new Media(game.getName(), game.getGenres().get(0).getName(), game.getBackgroundImage(), "", 0, false, System.currentTimeMillis()));
                prefs.edit().putString(listName, gson.toJson(medias)).apply();
                break;
        }
        mMedias.setValue(medias);
    }

    private void init() {
        populateLists();
        mMedias.setValue(medias);
    }

    private void populateLists() {
        if (prefs.contains(listName)) {
            medias = gson.fromJson(prefs.getString(listName, "EMPTY"), new TypeToken<ArrayList<Media>>() {}.getType());
        }
    }


    void sortByDate(){
        Collections.sort(medias, ((a, b) -> {
            if(a.getDate() < b.getDate()){
                return -1;
            }else if(a.getDate() > b.getDate()){
                return 1;
            }
            return 0;
        }));
        mMedias.setValue(medias);
    }

    void sortByAlphabet(){
        Collections.sort(medias, ((a, b) -> a.getName().compareTo(b.getName())));
        mMedias.setValue(medias);
    }

    void sortByRating(){
        Collections.sort(medias, ((a, b) -> {
            if(a.getRating() > b.getRating()){
                return -1;
            }else if(a.getRating() < b.getRating()){
                return 1;
            }
            return 0;
        }));
        mMedias.setValue(medias);
    }

    void sortByFavorite(){
        Collections.sort(medias, ((a, b) -> {
            if(a.isFavorite()){
                return -1;
            }
            return 0;
        }));
        mMedias.setValue(medias);
    }

    public LiveData<ArrayList<Media>> getLists() {
        return mMedias;
    }

    public LiveData<ArrayList<T>> getDownloadedMedias() {
        return mDownloadMedias;
    }

    public LiveData<Boolean> getErrorConnexion(){
        return errorMessage;
    }

    public void setErrorConnexion(boolean error){
        errorMessage.setValue(error);
    }

    void downloadSearchForBooks(String title, String author) {
        if (title.equals("")) {
            title = null;
        }
        if (author.equals("")) {
            author = null;
        }
        Call<Books> call = RetrofitClient.getClient(BaseURLs.OPEN_LIBRARY_URL).create(NetworkServices.class).getBooks(title, author);
        call.enqueue(new Callback<Books>() {
            @Override
            public void onResponse(Call<Books> call, Response<Books> response) {
                if (response.isSuccessful()) {
                    Books books = response.body();
                    if (!prefs.contains("books_database")) {
                        String bookData = gson.toJson(books);
                        prefs.edit().putString("books_database", bookData).apply();
                    } else {
                        Books bookData = gson.fromJson(prefs.getString("books_database", "EMPTY"), Books.class);
                        bookData.getBooks().addAll(books.getBooks());
                    }
                    mDownloadMedias.setValue((ArrayList<T>) books.getBooks());
                    for (Book book : books.getBooks()) {
                        Log.i("TITLE", book.getTitle());
                    }
                }else {
                    errorMessage.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<Books> call, Throwable t) {
                errorMessage.setValue(true);
            }
        });
    }



    void downloadSearchForGames(String title, String company) {
        if (title.equals("")) {
            title = null;
        }
        if (company.equals("")) {
            company = null;
        }else{
            company = company.replace(" ", "-").toLowerCase();
        }
        Call<Games> call = RetrofitClient.getClient(BaseURLs.RAWG_URL).create(NetworkServices.class).getGames(title, company);
        call.enqueue(new Callback<Games>() {
            @Override
            public void onResponse(Call<Games> call, Response<Games> response) {
                if (response.isSuccessful()) {
                    Games games = response.body();
                    if (!prefs.contains("games_database")) {
                        String bookData = gson.toJson(games);
                        prefs.edit().putString("games_database", bookData).apply();
                    } else {
                        Games gamesData = gson.fromJson(prefs.getString("games_database", "EMPTY"), Games.class);
                        gamesData.getGames().addAll(games.getGames());
                    }
                    mDownloadMedias.setValue((ArrayList<T>) games.getGames());
                    for(Game game : games.getGames()){
                        Log.i("Test", game.getName());
                    }
                }else {
                    errorMessage.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<Games> call, Throwable t) {
                errorMessage.setValue(true);
            }
        });
    }

    void downloadSearchForTV(String title) {
        if (title.equals("")) {
            title = null;
        }
        Call<Series> call = RetrofitClient.getClient(BaseURLs.THEMOVIEDB_URL).create(NetworkServices.class).getSeries(BaseURLs.THEMOVIEDB_KEY, title,"en-US", "1", "false");
        call.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> call, Response<Series> response) {
                if (response.isSuccessful()) {
                    Series series = response.body();
                    if (!prefs.contains("series_database")) {
                        String TVData = gson.toJson(series);
                        prefs.edit().putString("series_database", TVData).apply();
                    } else {
                        Series seriesData = gson.fromJson(prefs.getString("series_database", "EMPTY"), Series.class);
                        seriesData.getSeries().addAll(series.getSeries());
                    }
                    Log.i("DOWNLOADED", "TEST" + series.getSeries().size());
                    mDownloadMedias.setValue((ArrayList<T>) series.getSeries());
                    for(Serie serie : series.getSeries()){
                        Log.i("Test", serie.getName());
                    }
                }else{
                    errorMessage.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<Series> call, Throwable t) {errorMessage.setValue(true);
            }
        });
    }

    void downloadSearchForMovie(String title) {
        if (title.equals("")) {
            title = null;
        }
        Call<Movies> call = RetrofitClient.getClient(BaseURLs.THEMOVIEDB_URL).create(NetworkServices.class).getMovies(BaseURLs.THEMOVIEDB_KEY, title,"en-US", "1", "false");
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    Movies movies = response.body();
                    if (!prefs.contains("movies_database")) {
                        String TVData = gson.toJson(movies);
                        prefs.edit().putString("movies_database", TVData).apply();
                    } else {
                        Movies moviesData = gson.fromJson(prefs.getString("movies_database", "EMPTY"), Movies.class);
                        moviesData.getMovies().addAll(movies.getMovies());
                    }
                    Log.i("DOWNLOADED", "TEST" + movies.getMovies().size());
                    mDownloadMedias.setValue((ArrayList<T>) movies.getMovies());
                    for(Movie movie : movies.getMovies()){
                        Log.i("Test", movie.getTitle());
                    }
                }else{
                    errorMessage.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {errorMessage.setValue(true);
            }
        });
    }
}