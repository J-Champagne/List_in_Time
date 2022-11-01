package com.listintime.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.R;
import com.listintime.model.List;
import com.listintime.ui.viewholder.ListViewHolder;

import java.util.ArrayList;
import java.util.Collections;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private ArrayList<List> lists;
    private LayoutInflater inflater;
    private Context context;

    public ListAdapter(Context context, ArrayList<List> lists){
        this.lists = lists;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        List list = lists.get(position);
        holder.bind(list, position, context);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void sortByAlphabet(){
        Collections.sort(lists, (list1, list2) -> list1.getName().compareTo(list2.getName()));
        notifyDataSetChanged();
    }
}
