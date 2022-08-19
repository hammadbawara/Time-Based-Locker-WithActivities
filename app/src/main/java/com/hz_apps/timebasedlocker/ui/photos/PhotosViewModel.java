package com.hz_apps.timebasedlocker.ui.photos;

import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.Datebase.SavedFolder;

import java.util.ArrayList;
import java.util.List;

public class PhotosViewModel extends ViewModel {

    private List<SavedFolder> savedFolderList;

    public PhotosViewModel(){
        savedFolderList = new ArrayList<>();
    }

    public List<SavedFolder> getSavedFolderList() {
        return savedFolderList;
    }

    public void setSavedFolderList(List<SavedFolder> savedFolderList) {
        this.savedFolderList = savedFolderList;
    }
}