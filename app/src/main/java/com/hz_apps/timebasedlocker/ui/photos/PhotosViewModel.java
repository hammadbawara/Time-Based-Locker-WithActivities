package com.hz_apps.timebasedlocker.ui.photos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedPhoto;

import java.util.List;

public class PhotosViewModel extends ViewModel {

    private LiveData<List<SavedPhoto>> savedPhotosList;

    public PhotosViewModel() {
        savedPhotosList = new MutableLiveData<>();
    }

    public LiveData<List<SavedPhoto>> getSavedPhotosList() {
        return savedPhotosList;
    }

    public void setSavedPhotosList(LiveData<List<SavedPhoto>> savedPhotosList) {
        this.savedPhotosList = savedPhotosList;
    }
}