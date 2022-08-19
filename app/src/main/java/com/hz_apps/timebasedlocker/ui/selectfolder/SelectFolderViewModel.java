package com.hz_apps.timebasedlocker.ui.selectfolder;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SelectFolderViewModel extends ViewModel {
    private ArrayList<Folder> foldersList;


    public SelectFolderViewModel() {
        foldersList = new ArrayList<>();
    }

    public ArrayList<Folder> getFoldersList() {
        return foldersList;
    }

    public void setFoldersList(ArrayList<Folder> foldersList) {
        this.foldersList = foldersList;
    }
}
