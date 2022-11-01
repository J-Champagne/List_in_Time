package com.listintime.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.R;
import com.listintime.model.book.Book;
import com.listintime.model.game.Game;
import com.listintime.model.movies.Movie;
import com.listintime.model.series.Serie;
import com.listintime.network.BaseURLs;
import com.listintime.util.AddMediaEvent;
import com.listintime.util.MediaType;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class DownloadedMediaListViewHolder<T> extends RecyclerView.ViewHolder {
    private ConstraintLayout layout;
    private TextView name;
    private ImageView image;

    public DownloadedMediaListViewHolder(@NonNull View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.layout);
        name = itemView.findViewById(R.id.mediaName);
        image = itemView.findViewById(R.id.mediaImage);
    }

    public void bind(T media, int position, Context context){
        Picasso.get().setLoggingEnabled(true);
        switch (MediaType.getCurrentMediaFragment()){
            case "book":
                Book book = (Book) media;
                name.setText(book.getTitle());
                Picasso.get().load(BaseURLs.getBookImageURL(book.getCoverI())).into(image);
                break;
            case "movie":
                Movie movie = (Movie) media;
                name.setText(movie.getTitle());
                Picasso.get().load(BaseURLs.getTVImageURL(movie.getPosterPath())).into(image);
                break;
            case "series":
                Serie serie = (Serie) media;
                name.setText(serie.getName());
                Picasso.get().load(BaseURLs.getTVImageURL(serie.getPosterPath())).into(image);
                break;
            case "game":
                Game game = (Game) media;
                name.setText(game.getName());
                Picasso.get().load(game.getBackgroundImage()).into(image);
                break;
        }
        layout.setOnClickListener(v -> {
            EventBus.getDefault().post(new AddMediaEvent<>(media));
        });
    }
}
