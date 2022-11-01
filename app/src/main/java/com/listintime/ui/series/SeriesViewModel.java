package com.listintime.ui.series;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.listintime.model.List;
import com.listintime.model.Lists;
import com.listintime.util.AddListEvent;
import com.listintime.util.CopyEventList;
import com.listintime.util.DeleteEventList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class SeriesViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<List>> mLists;
    private Lists list = new Lists();
    private SharedPreferences prefs;
    private Gson gson;

    public SeriesViewModel(Application application) {
        super(application);
        EventBus.getDefault().register(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        gson = new GsonBuilder().create();
        mLists = new MutableLiveData<>();
        init();
    }

    private void init() {
        populateLists();
        mLists.setValue(list);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void addListEventReceived(AddListEvent addListEvent) {
        list.clear();
        populateLists();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void deleteEventReceived(DeleteEventList deleteEventList) {
        prefs.edit().remove(list.get(deleteEventList.position).getName()).apply();
        list.remove(deleteEventList.position);
        prefs.edit().putString("Series", gson.toJson(list)).apply();
        mLists.setValue(list);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void copyEventReceived(CopyEventList copyEventList) {
        list.add(gson.fromJson(gson.toJson(list.get(copyEventList.position)), List.class));
        list.get(list.size()-1).setName(copyEventList.name);
        prefs.edit().putString("Series", gson.toJson(list)).apply();
        mLists.setValue(list);
    }


    private void populateLists() {
        if (prefs.contains("Series")) {
            list.addAll(Objects.requireNonNull(gson.fromJson(prefs.getString("Series", "EMPTY"), Lists.class)));
            Log.i("List size", list.size() + "");
        }
    }

    void sortByDate(){
        Collections.sort(list, ((a, b) -> {
            if(a.getDate() < b.getDate()){
                return -1;
            }else if(a.getDate() > b.getDate()){
                return 1;
            }
            return 0;
        }));
        mLists.setValue(list);
    }

    void sortByAlphabet(){
        Collections.sort(list, ((a, b) -> a.getName().compareTo(b.getName())));
        mLists.setValue(list);
    }

    public LiveData<ArrayList<List>> getLists(){return mLists;}
}