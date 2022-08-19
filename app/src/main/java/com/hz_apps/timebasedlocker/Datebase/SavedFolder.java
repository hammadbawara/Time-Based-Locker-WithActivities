package com.hz_apps.timebasedlocker.Datebase;

public class SavedFolder {
    private int id;
    private String name;
    // Files inside the folder table
    private String filesTable;

    public SavedFolder(int id, String name, String files_table) {
        this.id = id;
        this.name = name;
        this.filesTable = files_table;
    }

    public SavedFolder(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilesTable() {
        return filesTable;
    }

    public void setFilesTable(String filesTable) {
        this.filesTable = filesTable;
    }
}
