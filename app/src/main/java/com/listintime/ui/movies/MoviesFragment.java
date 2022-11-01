package com.listintime.ui.movies;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.R;
import com.listintime.ui.adapter.ListAdapter;
import com.listintime.util.MediaType;

public class MoviesFragment extends Fragment {

    private MoviesViewModel moviesViewModel;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private int lastSortClicked = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movies, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        MediaType.setCurrentMediaFragment(MediaType.MOVIE);

        Button sortButton = root.findViewById(R.id.sortlist);
        setSortButton(sortButton);

        moviesViewModel.getLists().observe(getViewLifecycleOwner(), lists -> {
            listAdapter = new ListAdapter(getContext(), lists);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        });
        return root;
    }

    private void setSortButton(Button sortButton) {
        sortButton.setOnClickListener(v -> {
            final String[] sortOptions = {"Date", "A-Z"};
            AlertDialog.Builder addListDialog = new AlertDialog.Builder(getActivity());
            addListDialog.setTitle("Sort by :")
                    .setSingleChoiceItems(sortOptions, lastSortClicked,(dialog, position) -> {})
                    .setPositiveButton("Sort", (dialog, which) -> {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        lastSortClicked = lw.getCheckedItemPosition();
                        switch (lastSortClicked){
                            case 0: moviesViewModel.sortByDate();
                                break;
                            case 1: moviesViewModel.sortByAlphabet();
                                break;
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}