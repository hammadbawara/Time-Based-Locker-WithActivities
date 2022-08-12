package com.hz_apps.timebasedlocker.ui.photos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.Datebase.SavedPhoto;

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