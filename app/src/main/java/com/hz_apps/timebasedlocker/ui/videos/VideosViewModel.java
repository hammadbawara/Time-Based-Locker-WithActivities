package com.hz_apps.timebasedlocker.ui.videos;

import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedFile;

import java.util.ArrayList;
import java.util.List;

public class VideosViewModel extends ViewModel {

    private List<SavedFile> savedVideosList;

    public VideosViewModel(){
        savedVideosList = new ArrayList<>();
    }

    public List<SavedFile> getSavedVideosList() {
        return savedVideosList;
    }

    public void setSavedVideosList(List<SavedFile> savedVideosList) {
        this.savedVideosList = savedVideosList;
    }
}