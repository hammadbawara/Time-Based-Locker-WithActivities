package com.hz_apps.timebasedlocker.ui.videos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedVideo;

import java.util.List;

public class VideosViewModel extends ViewModel {

    private LiveData<List<SavedVideo>> savedVideoList;

    public VideosViewModel(){
        savedVideoList = new MutableLiveData<>();
    }

    public LiveData<List<SavedVideo>> getSavedVideoList() {
        return savedVideoList;
    }

    public void setSavedVideoList(LiveData<List<SavedVideo>> savedVideoList) {
        this.savedVideoList = savedVideoList;
    }
}