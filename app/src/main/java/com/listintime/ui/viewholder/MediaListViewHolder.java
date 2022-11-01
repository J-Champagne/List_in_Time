package com.listintime.ui.viewholder;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.R;
import com.listintime.model.Media;
import com.listintime.util.DeleteEventMedia;
import com.listintime.util.RatingEvent;
import com.listintime.util.SetCommentEvent;
import com.listintime.util.SetFavoriteEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class MediaListViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {
    private ConstraintLayout layout;
    private TextView name;
    private TextView information;
    private TextView comment;
    private ImageView editButton;
    private ImageView deleteButton;
    private ImageView mediaImage;
    private ImageView favorite;
    private Spinner spinner;
    private Media media;
    private int position;

    public MediaListViewHolder(@NonNull View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.layout);
        name = itemView.findViewById(R.id.mediaName);
        information = itemView.findViewById(R.id.mediaInformation);
        comment = itemView.findViewById(R.id.comment);
        editButton = itemView.findViewById(R.id.editButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);
        mediaImage = itemView.findViewById(R.id.mediaImage);
        favorite = itemView.findViewById(R.id.favoriteButton);
        spinner = itemView.findViewById(R.id.ratingSpinner);
    }

    public void bind(Media media, int position, Context context){
        this.media = media;
        this.position = position;

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.rating_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(media.getRating());


        layout.setOnClickListener(v -> {
            if(comment.getVisibility() == View.GONE && !media.getComment().equals("")){
                comment.setVisibility(View.VISIBLE);
            }else{
                comment.setVisibility(View.GONE);
            }
        });

        if(media.isFavorite()){
            favorite.setBackgroundResource(R.drawable.ic_baseline_grade_24);
        }else{
            favorite.setBackgroundResource(R.drawable.ic_baseline_star_border_24);
        }

        favorite.setOnClickListener(view -> {
            if(media.isFavorite()){
                favorite.setBackgroundResource(R.drawable.ic_baseline_star_border_24);
                media.setFavorite(false);
            }else{
                favorite.setBackgroundResource(R.drawable.ic_baseline_grade_24);
                media.setFavorite(true);
            }
            EventBus.getDefault().post(new SetFavoriteEvent(media.isFavorite(), position));
        });

        name.setText(media.getName());
        information.setText(media.getInformation());
        comment.setText(media.getComment());

        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(media.getImageURL()).into(mediaImage);

        editButton.setOnClickListener(v ->{
            AlertDialog.Builder editBuilder = new AlertDialog.Builder(context);
            editBuilder.setTitle("Edit");
            editBuilder.setMessage("Enter a new comment");
            final EditText newCommentEdit = new EditText(context);
            newCommentEdit.setText(media.getComment());
            newCommentEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            editBuilder.setView(newCommentEdit);
            editBuilder.setPositiveButton("OK", (dialog, which) -> {
                comment.setText(newCommentEdit.getText().toString());
                EventBus.getDefault().post(new SetCommentEvent(comment.getText().toString(), position));
            }).show();
        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);
            deleteBuilder.setTitle("Delete")
                    .setMessage("Do you want to delete this media")
                    .setPositiveButton("Yes", (dialog, which) -> EventBus.getDefault().post(new DeleteEventMedia(position)))
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        EventBus.getDefault().post(new RatingEvent(i, position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        adapterView.setSelection(media.getRating());
    }
}
