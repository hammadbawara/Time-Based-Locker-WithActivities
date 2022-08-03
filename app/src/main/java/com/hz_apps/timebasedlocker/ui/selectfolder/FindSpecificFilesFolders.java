package com.hz_apps.timebasedlocker.ui.selectfolder;

import java.io.File;
import java.util.ArrayList;

public class FindSpecificFilesFolders {

    // global variables
    private final String path;
    private final String[] extensionsList;
    private String[] ignoreFoldersList;
    private ArrayList<Folder> foldersList = new ArrayList<>();

    public FindSpecificFilesFolders(String path, String[] extensions) {
        this.path = path;
        this.extensionsList = extensions;
        this.findFolders();
    }
    // constructor
    public FindSpecificFilesFolders(String path, String[] extensions, String[] ignoreFoldersList) {
        this.path = path;
        this.extensionsList = extensions;
        this.ignoreFoldersList = ignoreFoldersList;
        // calling findFolders fucntion
        this.findFolders();
    }

    private void findFolders(){

        File homeDirectoryPath = new File(path);

        // checking if pathFile is a folder if it is a path then okay.
        if (!homeDirectoryPath.isDirectory()){
            throw new Error("The path you provided is not a valid directory.");
        }

        // ignoring folders
        File[] filesList;
        if (ignoreFoldersList != null){
            filesList = ignoreFolders(homeDirectoryPath);

            for (File path : filesList){
                if (path == null) continue;
                this.createFoldersList(path);
            }

        }else{
            this.createFoldersList(homeDirectoryPath);
        }

    }

    // ignore folders
    private File[] ignoreFolders(File homeDirectoryPath){
        // Getting home directory
        File[] filesList = homeDirectoryPath.listFiles();

        int numberOfIgnoreFolders = ignoreFoldersList.length;
        int numberOfFolderIgnored = 0;

        // ignoring folders : simple technique comparing all all folders with ignoreFolders
        // if they match then set that folder to "null".
        for (int i=0; i<filesList.length; i++){
            for (String ignoreFolder : ignoreFoldersList){
                if (filesList[i].getName().equals(ignoreFolder)){
                    filesList[i] = null;
                    numberOfFolderIgnored += 1;
                    break;
                }
            }
            // When all folders ignored then no need to further check
            if (numberOfFolderIgnored == numberOfIgnoreFolders){
                break;
            }
        }
        return filesList;
    }

    private void createFoldersList(File path){

        // removing hidden directory
        if (path.getName().startsWith(".")) return;

        File[] filesList = path.listFiles();

        if (filesList == null) return;

        /*
         * - checking if path is dir or file
         * - if path is dir then again call same method
         * - if it is file then checks if file is image by extension
         * - if file is image then add folder into foldersList
         * - At last return method
         */
        for (File file : filesList){
            if (file.isDirectory()){
                createFoldersList(file);
            }
            else{
                for (String extension : extensionsList){
                    if (file.getName().endsWith(extension)){
                        Folder folder = new Folder(file.getParentFile().getAbsolutePath(), file);
                        foldersList.add(folder);
                        return;
                    }
                }
            }
        }

    }



    public ArrayList<Folder> getFoldersList(){
        return foldersList;
    }

}
