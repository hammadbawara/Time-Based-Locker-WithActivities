package com.hz_apps.timebasedlocker.ui.photos;

import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedFile;

import java.util.ArrayList;
import java.util.List;

public class PhotosViewModel extends ViewModel {

    private List<SavedFile> savedFileList;

    public PhotosViewModel(){
        savedFileList = new ArrayList<>();
    }

    public List<SavedFile> getSavedFileList() {
        return savedFileList;
    }

    public void setSavedFileList(List<SavedFile> savedFileList) {
        this.savedFileList = savedFileList;
    }
}