package com.hz_apps.timebasedlocker.ui.ShowSavedFiles;

import androidx.lifecycle.ViewModel;

import com.hz_apps.timebasedlocker.Datebase.SavedFile;

import java.util.ArrayList;
import java.util.List;

public class SavedFilesViewModel extends ViewModel {

    private List<SavedFile> savedFilesList;

    public SavedFilesViewModel() {
        savedFilesList = new ArrayList<>();
    }

    public List<SavedFile> getSavedFilesList() {
        return savedFilesList;
    }

    public void setSavedFilesList(List<SavedFile> savedFilesList) {
        this.savedFilesList = savedFilesList;
    }
}
