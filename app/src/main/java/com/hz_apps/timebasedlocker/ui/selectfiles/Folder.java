package com.hz_apps.timebasedlocker.ui.selectfiles;

import java.io.File;

public class Folder extends File {

    private final File firstImage;

    public Folder(String path, File firstImage){
        super(path);
        this.firstImage = firstImage;
    }
    
    public File getFirstImage(){
        return firstImage;
    }
}
