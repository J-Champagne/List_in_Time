package com.listintime.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.R;
import com.listintime.ui.viewholder.DownloadedMediaListViewHolder;

import java.util.List;

public class DownloadedMediaListAdapter <T> extends RecyclerView.Adapter<DownloadedMediaListViewHolder<T>> {

    private List<T> medias;
    private LayoutInflater inflater;
    private Context context;

    public DownloadedMediaListAdapter(Context context, List<T> medias){
        this.medias = medias;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public DownloadedMediaListViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.downloaded_media_list, parent, false);
        return new DownloadedMediaListViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedMediaListViewHolder holder, int position) {
        T media = medias.get(position);
        holder.bind(media, position, context);
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }
}
