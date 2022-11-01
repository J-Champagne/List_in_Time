package com.listintime;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.listintime.model.List;
import com.listintime.model.Lists;
import com.listintime.util.AddListEvent;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String[] userNameStr = {""};
        Gson gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.userName);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_movies, R.id.nav_series, R.id.nav_books, R.id.nav_games, R.id.nav_media).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (!prefs.contains("userName")) {
            AlertDialog.Builder userNameDialog = new AlertDialog.Builder(this);
            userNameDialog.setTitle("Choose your username");
            final EditText userNameIn = new EditText(this);
            userNameIn.setInputType(InputType.TYPE_CLASS_TEXT);
            userNameDialog.setView(userNameIn);
            userNameDialog.setPositiveButton("OK", (dialog, which) -> {
                username = userNameIn.getText().toString();
                prefs.edit().putString("userName", username).apply();
                userName.setText(username);
            });
            userNameDialog.show();
        } else {
            username = prefs.getString("userName", "User");
            userName.setText(username);
        }

        if (!prefs.contains("Books"))
            prefs.edit().putString( "Books" , gson.toJson(new Lists())).apply();
        if (!prefs.contains("Movies"))
            prefs.edit().putString( "Movies" , gson.toJson(new Lists())).apply();
        if (!prefs.contains("Series"))
            prefs.edit().putString( "Series" , gson.toJson(new Lists())).apply();
        if (!prefs.contains("Games"))
            prefs.edit().putString( "Games" , gson.toJson(new Lists())).apply();
        createList(prefs, toolbar);
    }


    private void createList(SharedPreferences prefs, Toolbar toolbar) {
        Button addListButton = toolbar.findViewById(R.id.addlist);
        Gson gson = new Gson();

        addListButton.setOnClickListener(v -> {
            final CharSequence[] medias = {"Games", "Books", "Series", "Movies"};
            final TextView textView = new TextView(this);
            final EditText listName = new EditText(this);
            final LinearLayout ll = new LinearLayout(this);
            textView.setText("Enter name :");
            textView.setTextSize(12);
            textView.setPadding(2,2,2,2);
            listName.setLines(1);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.addView(textView);
            ll.addView(listName);

            AlertDialog.Builder addListDialog = new AlertDialog.Builder(this);
            addListDialog.setTitle("Add a list")
                    .setView(ll)
                    .setSingleChoiceItems(medias, 0, (dialog, which) -> {  })
                    .setPositiveButton("Add", (dialog, which) -> {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        Lists lists = gson.fromJson(prefs.getString( ((String) checkedItem) ,"Empty"), Lists.class);
                        List list = new List();
                        list.setDate(System.currentTimeMillis());
                        list.setName(listName.getText().toString());
                        lists.add(list);
                        prefs.edit().putString( ((String) checkedItem).trim(), gson.toJson(lists)).apply();
                        EventBus.getDefault().post(new AddListEvent(true));
                        Toast.makeText(getApplicationContext(), "List created", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}