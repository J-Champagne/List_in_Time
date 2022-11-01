package com.listintime.ui.viewholder;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.listintime.MainActivity;
import com.listintime.R;
import com.listintime.model.List;
import com.listintime.util.CopyEventList;
import com.listintime.util.DeleteEventList;
import com.listintime.util.ListNameEvent;

import org.greenrobot.eventbus.EventBus;

public class ListViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private ImageView remove;
    private ImageView copy;
    private ConstraintLayout layout;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.listname);
        remove = itemView.findViewById(R.id.remove);
        copy = itemView.findViewById(R.id.copy);
        layout = itemView.findViewById(R.id.layout);
    }

    public void bind(List list, int position, Context context){
        name.setText(list.getName());

        layout.setOnClickListener(v -> {
            Log.i("TEST", "open media fragment");
            ((MainActivity)context).getSupportActionBar().setTitle(list.getName());
            EventBus.getDefault().postSticky(new ListNameEvent(list.getName()));
            Navigation.findNavController(v).navigate(R.id.nav_media);
        });

        copy.setOnClickListener(v ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView nameLabel = new TextView(context);
            nameLabel.setText("Name: ");
            EditText name = new EditText(context);
            linearLayout.addView(nameLabel);
            linearLayout.addView(name);
            builder.setTitle("Copy")
                    .setMessage("Do you want to copy this list?")
                    .setView(linearLayout)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if(name.getText().toString().equals("") || name.getText().toString().equals(list.getName())){
                            Toast.makeText(context, "Name cannot be empty and has to be different than original list.", Toast.LENGTH_SHORT).show();
                        }else{
                            EventBus.getDefault().post(new CopyEventList(position, name.getText().toString()));
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
        });

        remove.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Remove")
                    .setMessage("Do you want to remove this list?")
                    .setPositiveButton("Yes", (dialog, which) -> EventBus.getDefault().post(new DeleteEventList(position)))
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
        });
    }

}
