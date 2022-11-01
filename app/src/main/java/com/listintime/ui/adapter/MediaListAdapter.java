package com.listintime.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.R;
import com.listintime.model.Media;
import com.listintime.ui.viewholder.MediaListViewHolder;

import java.util.ArrayList;

public class MediaListAdapter extends RecyclerView.Adapter<MediaListViewHolder> {

    private ArrayList<Media> medias;
    private LayoutInflater inflater;
    private Context context;

    public MediaListAdapter(Context context, ArrayList<Media> medias){
        this.medias = medias;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.media_list, parent, false);
        return new MediaListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaListViewHolder holder, int position) {
        Media media = medias.get(position);
        holder.bind(media, position, context);
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }
}
