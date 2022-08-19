package com.hz_apps.timebasedlocker.ui.videos;

import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedFolder;

import java.util.ArrayList;
import java.util.List;

public class VideosViewModel extends ViewModel {

    private List<SavedFolder> savedFolderList;

    public VideosViewModel() {
        savedFolderList = new ArrayList<>();
    }

    public List<SavedFolder> getSavedFolderList() {
        return savedFolderList;
    }

    public void setSavedFolderList(List<SavedFolder> savedFolderList) {
        this.savedFolderList = savedFolderList;
    }
}