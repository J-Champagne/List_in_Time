package com.listintime.ui.media;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.R;
import com.listintime.ui.adapter.DownloadedMediaListAdapter;
import com.listintime.ui.adapter.MediaListAdapter;
import com.listintime.util.AddMediaEvent;
import com.listintime.util.MediaType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MediaListFragment<T> extends Fragment {

    private MediaListViewModel<T> mediasViewModel;
    private RecyclerView recyclerView;
    private MediaListAdapter mediaListAdapter;
    private ConstraintLayout searchResult;
    private RecyclerView recyclerViewResult;
    private DownloadedMediaListAdapter<T> downloadedMediaListAdapter;
    private int lastSortClicked = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mediasViewModel = new ViewModelProvider(this).get(MediaListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_media_list, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        Button sortButton = root.findViewById(R.id.sortlist);
        Button addButton = root.findViewById(R.id.addmedia);
        Button searchResultCancel = root.findViewById(R.id.search_result_hide);
        searchResult = root.findViewById(R.id.searchresult);
        recyclerViewResult = root.findViewById(R.id.recyclerviewdownloaded);



        EventBus.getDefault().register(this);

        setSortButton(sortButton);
        setAddButton(addButton);
        searchResultCancel.setOnClickListener(v -> {
            searchResult.setVisibility(View.GONE);
        });

        mediasViewModel.getLists().observe(getViewLifecycleOwner(), list -> {
            mediaListAdapter = new MediaListAdapter(getContext(), list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(mediaListAdapter);
            mediaListAdapter.notifyDataSetChanged();
        });

        mediasViewModel.getDownloadedMedias().observe(getViewLifecycleOwner(), list -> {
            downloadedMediaListAdapter = new DownloadedMediaListAdapter<T>(getContext(), (List<T>) list);
            recyclerViewResult.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewResult.setAdapter(downloadedMediaListAdapter);
            mediaListAdapter.notifyDataSetChanged();
        });

        mediasViewModel.getErrorConnexion().observe(getViewLifecycleOwner(), error ->{
            if(error) {
                showErrorDialog();
                mediasViewModel.setErrorConnexion(false);
            }
        });

        return root;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void itemAddEventReceived(AddMediaEvent addMediaEvent) {
        Toast.makeText(getActivity(), "Added to the list.", Toast.LENGTH_SHORT).show();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Erreur de connexion")
                .setMessage("Une erreur de connexion est survenue, veuillez réessayer.").setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void setSortButton(Button sortButton) {
        sortButton.setOnClickListener(v -> {
            final String[] sortOptions = {"Added Date", "A-Z", "Rating", "Favorite"};
            AlertDialog.Builder addListDialog = new AlertDialog.Builder(getActivity());
            addListDialog.setTitle("Sort by :")
                    .setSingleChoiceItems(sortOptions, lastSortClicked,(dialog, position) -> {})
                    .setPositiveButton("Sort", (dialog, which) -> {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        lastSortClicked = lw.getCheckedItemPosition();
                        switch (lastSortClicked){
                            case 0: mediasViewModel.sortByDate();
                            break;
                            case 1: mediasViewModel.sortByAlphabet();
                            break;
                            case 2: mediasViewModel.sortByRating();
                            break;
                            case 3: mediasViewModel.sortByFavorite();
                            break;
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void setAddButton(Button addButton) {
        addButton.setOnClickListener(v -> {
            switch (MediaType.getCurrentMediaFragment()) {
                case "book":
                    customAlertDialot("Author");
                    break;
                case "movie":
                case "series":
                    customAlertDialot("");
                    break;
                case "game":
                    customAlertDialot("Company");
                    break;
            }
        });
    }

    private void customAlertDialot(String secondParam) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout titleLayout = new LinearLayout(getContext());
        titleLayout.setLayoutParams(params);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView titleLabel = new TextView(getContext());
        titleLabel.setText("Title: ");
        final EditText title = new EditText(getContext());
        titleLayout.addView(titleLabel);
        titleLayout.addView(title);

        LinearLayout secondParamLayout = new LinearLayout(getContext());
        secondParamLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondParamLayout.setLayoutParams(params);
        TextView secondParamLabel = new TextView(getContext());
        secondParamLabel.setText(secondParam + ": ");
        final EditText secondParamInput = new EditText(getContext());
        secondParamLayout.addView(secondParamLabel);
        secondParamLayout.addView(secondParamInput);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(titleLayout);
        if(!secondParam.equals("")) layout.addView(secondParamLayout);




        AlertDialog.Builder addListDialog = new AlertDialog.Builder(getActivity());
        addListDialog.setTitle("Add new " + MediaType.getCurrentMediaFragment())
                .setView(layout)
                .setPositiveButton("Search", (dialog, which) -> {
                    searchResult.setVisibility(View.VISIBLE);
                    switch (MediaType.getCurrentMediaFragment()) {
                        case "book":
                            mediasViewModel.downloadSearchForBooks(title.getText().toString(), secondParamInput.getText().toString());
                            break;
                        case "movie":
                            mediasViewModel.downloadSearchForMovie(title.getText().toString());
                            break;
                        case "series":
                            mediasViewModel.downloadSearchForTV(title.getText().toString());
                            break;
                        case "game":
                            mediasViewModel.downloadSearchForGames(title.getText().toString(), secondParamInput.getText().toString());
                            break;
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }





}