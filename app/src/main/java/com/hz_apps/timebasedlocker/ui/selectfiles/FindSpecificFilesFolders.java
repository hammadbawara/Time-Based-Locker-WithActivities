package com.hz_apps.timebasedlocker.ui.selectfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindSpecificFilesFolders {

    // global variables
    private final String path;
    private final String[] extensionsList;
    private String[] ignoreFoldersList;
    private ArrayList<Folder> foldersList = new ArrayList<>();


    // constructor
    public FindSpecificFilesFolders(String path, String[] extensions) {
        this.path = path;
        this.extensionsList = extensions;
        this.findFolders();
    }

    /*
     * set folders that will be ignored.
     */
    public void setIgnoreFoldersList(String[] ignoreFolders) {
        this.ignoreFoldersList = ignoreFolders;
    }

    private void findFolders(){

        File pathFile = new File(path);

        // checking if pathFile is a folder if it is a path then okay.
        if (!pathFile.isDirectory()){
            System.out.println("Error");
            return;
        }
        
        // ignoring folders
        List<File> filesList;
        if (ignoreFoldersList != null){
            filesList = ignoreFolders(pathFile);

            for (File path : filesList){
                this.createFoldersList(path);
            }

        }else{
            this.createFoldersList(pathFile);
        }
        
    }

    private List<File> ignoreFolders(File pathFile){
        List<File> filesList = new ArrayList<>(Arrays.asList(pathFile.listFiles()));

        for (int i=0; i<filesList.size(); i++){
            for (String ignoreFolder : ignoreFoldersList){
                if (filesList.get(i).getAbsolutePath().equals(ignoreFolder)){
                    filesList.remove(i);
                }
            }
        }

        return filesList;
    }

    private void createFoldersList(File path){

        // removing hidden directory
        if (path.getName().startsWith(".")) return;

        File[] filesList = path.listFiles();

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
