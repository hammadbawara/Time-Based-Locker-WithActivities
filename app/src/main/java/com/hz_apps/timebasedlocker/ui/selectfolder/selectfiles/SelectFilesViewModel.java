package com.hz_apps.timebasedlocker.ui.selectfolder.selectfiles;

import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;

public class SelectFilesViewModel extends ViewModel {

    private ArrayList<File> filesList;

    public SelectFilesViewModel(){
        filesList = new ArrayList<>();
    }

    public ArrayList<File> getFilesList() {
        return filesList;
    }

    public void setFilesList(ArrayList<File> filesList) {
        this.filesList = filesList;
    }

    public void addFileInFilesList(File file){
        filesList.add(file);
    }
}