package com.hz_apps.timebasedlocker.ui.selectfolder;

import java.io.File;
import java.io.Serializable;

public class Folder extends File implements Serializable {

    private final File firstImage;

    public Folder(String path, File firstImage){
        super(path);
        this.firstImage = firstImage;
    }
    
    public File getFirstImage(){
        return firstImage;
    }
}
