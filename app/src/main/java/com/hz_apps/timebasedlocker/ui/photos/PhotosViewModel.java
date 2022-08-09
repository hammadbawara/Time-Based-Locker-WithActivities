package com.hz_apps.timebasedlocker.ui.photos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedPhoto;

public class PhotosViewModel extends ViewModel {

    private LiveData<SavedPhoto> savedPhotosList;

    public PhotosViewModel() {
        savedPhotosList = new MutableLiveData<>();
    }

    public LiveData<SavedPhoto> getSavedPhotosList() {
        return savedPhotosList;
    }

    public void setSavedPhotosList(LiveData<SavedPhoto> savedPhotosList) {
        this.savedPhotosList = savedPhotosList;
    }
}