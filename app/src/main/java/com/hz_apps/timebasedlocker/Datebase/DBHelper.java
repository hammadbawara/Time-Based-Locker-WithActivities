package com.hz_apps.timebasedlocker.Datebase;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Table values
    private final String ORIGINAL_PATH = "orignal_path";
    private final String PATH = "path";
    private final String NAME = "name";
    private final String UNLOCK_DATE_TIME = "unlock_date_time";
    private final String LOCK_DATE_TIME = "lock_date_time";
    private final String IS_FILE = "is_file";
    private final String FILE_TYPE = "file_type";
    private final String IS_ALLOWED_TO_EXTEND_TIME = "is_allowed_to_extend_date_time";
    private final String IS_ALLOWED_TO_SEE_PHOTO = "is_allowed_to_see_photo";
    private final String IS_ALLOWED_TO_SEE_TITLE = "is_allowed_to_see_title";
    public static final int LAST_SAVED_VIDEO_KEY = 1;
    public static final int LAST_SAVED_PHOTO_KEY = 2;
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_OTHERS = 2;
    private static final String FOLDER_FILES_TABLE_NAME = "folder_files_table_name";

    // Tables names
    public static final String SAVED_VIDEO_TABLE = "saved_videos";
    public static final String SAVED_PHOTO_TABLE = "saved_photos";
    private static final String DB_RECORD_TABLE = "DBRecord";
    public static final String VIDEO_FOLDERS_TABLE = "video_folders_list";
    public static final String PHOTO_FOLDERS_TABLE = "photo_folders_list";
    private static final String OTHER_FOLDERS_TABLE = "others_files_folders_list";

    public static DBHelper getINSTANCE(){
        return dbHelper;
    }

    private final String CREATE_TABLE_QUERY = "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            ORIGINAL_PATH + " TEXT, " + // 1
            PATH + " TEXT," + // 2
            NAME + " TEXT, " + // 3
            UNLOCK_DATE_TIME+" TEXT, " + // 4
            LOCK_DATE_TIME + " TEXT, " + // 5
            IS_FILE + " INTEGER, " + // 6
            FILE_TYPE + " INTEGER, " + //7
            IS_ALLOWED_TO_EXTEND_TIME +" INTEGER, " + // 8
            IS_ALLOWED_TO_SEE_PHOTO + " INTEGER, " + // 9
            IS_ALLOWED_TO_SEE_TITLE + " INTEGER)"; // 10

    // Tracking database
    public static boolean isAnyFileInserted = false;

    private static DBHelper dbHelper;

    public static void createInstanceForOverApplication(Application application){
        if (dbHelper == null){
            dbHelper = new DBHelper(application.getApplicationContext(), "filesDB.db");
        }
    }

    private SQLiteDatabase db;

    public DBHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SAVED_VIDEO_TABLE + CREATE_TABLE_QUERY);
        db.execSQL("CREATE TABLE " + SAVED_PHOTO_TABLE + CREATE_TABLE_QUERY);

        db.execSQL("CREATE TABLE " + DB_RECORD_TABLE + "(_key INTEGER PRIMARY KEY, value INTEGER)");

        final String CREATE_FOLDERS_TABLE_QUERY = "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT," +
                FOLDER_FILES_TABLE_NAME + " TEXT" +
                ")";

        db.execSQL("CREATE TABLE " + PHOTO_FOLDERS_TABLE + CREATE_FOLDERS_TABLE_QUERY);
        db.execSQL("CREATE TABLE " + VIDEO_FOLDERS_TABLE + CREATE_FOLDERS_TABLE_QUERY);
        db.execSQL("CREATE TABLE " + OTHER_FOLDERS_TABLE + CREATE_FOLDERS_TABLE_QUERY);

        db.execSQL("INSERT INTO "  + DB_RECORD_TABLE + "(_key, value) VALUES(1, 1)");
        db.execSQL("INSERT INTO "  + DB_RECORD_TABLE + "(_key, value) VALUES(2, 1)");
        createFolderFirstTime(db, TYPE_VIDEO, "Videos");
        createFolderFirstTime(db, TYPE_OTHERS, "Other Files");
        createFolderFirstTime(db, TYPE_PHOTO, "Photos");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * This method insert savedFile into database
     * @param savedFile : savedFile that would be saved into database
     * @param nameOfTable : table name where files would store
     * @return: it returns List<SavedFiles>
     */
    public boolean insert_file(SavedFile savedFile, String nameOfTable){

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ORIGINAL_PATH, savedFile.getOriginalPath());
        values.put(PATH, savedFile.getPath());
        values.put(NAME, savedFile.getName());
        values.put(UNLOCK_DATE_TIME, savedFile.getUnlockDateTime().toString());
        values.put(LOCK_DATE_TIME, savedFile.getLockDateTime().toString());
        values.put(IS_FILE, savedFile.isFile());
        values.put(FILE_TYPE, savedFile.getFileType());
        values.put(IS_ALLOWED_TO_EXTEND_TIME, savedFile.isAllowedToExtendDateTime());
        values.put(IS_ALLOWED_TO_SEE_TITLE, savedFile.isAllowedToSeeTitle());
        values.put(IS_ALLOWED_TO_SEE_PHOTO, savedFile.isAllowedToSeePhoto());

        long result = db.insert(nameOfTable, null, values);
        isAnyFileInserted = true;
        return result != -1;
    }
    /**
     * @param folderName : Folder name
     * @param FileType : Types of files will be in the folder. e.g TYPE_VIDEOS, TYPE_PHOTOS and TYPE_OTHERS
     * @return if the folder created then true else return false
     */
    public boolean create_folder(String folderName, int FileType){
        db = getWritableDatabase();

        String tableName = getFolderTableName(FileType);
        String folderFilesTableName = getFolderFilesTableName(tableName);

        ContentValues values = new ContentValues(1);
        values.put(NAME, folderName);
        values.put(FOLDER_FILES_TABLE_NAME, folderFilesTableName);

        db.insert(tableName, null, values);

        // creating folder for saving folder files
        db.execSQL("CREATE TABLE " + folderFilesTableName + CREATE_TABLE_QUERY);
        return false;
    }

    public List<SavedFolder> getSavedFolders(int FILE_TYPE){
        db = getReadableDatabase();
        String tableName = getFolderTableName(FILE_TYPE);
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        List<SavedFolder> savedFolderList = new ArrayList<>();

        while (cursor.moveToNext()){
            SavedFolder folder = new SavedFolder();
            folder.setId(cursor.getInt(0));
            folder.setName(cursor.getString(1));
            folder.setFilesTable(cursor.getString(2));
            savedFolderList.add(folder);
        }

        cursor.close();

        return savedFolderList;
    }

    /**
     * This method get saved files from database
     * @param table : tables names from where database needs to get
     * @return it return List<SavedFiles>.
     */
    public List<SavedFile> getSavedFiles(String table){
        db = getReadableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM  " + table,null );

        List<SavedFile> savedFilesList = new ArrayList<>();

        while (cursor.moveToNext()){
            SavedFile savedFile = new SavedFile();
            savedFile.setId(cursor.getInt(0));
            savedFile.setOriginalPath(cursor.getString(1));
            savedFile.setPath(cursor.getString(2));
            savedFile.setName(cursor.getString(3));
            savedFile.setUnlockDateTime(DateAndTime.parse(cursor.getString(4)));
            savedFile.setLockDateTime(DateAndTime.parse(cursor.getString(5)));
            savedFile.setFile(IntToBoolean(cursor.getInt(6)));
            savedFile.setFileType(cursor.getInt(7));
            savedFile.setAllowedToExtendDateTime(IntToBoolean(cursor.getInt(8)));
            savedFile.setAllowedToSeePhoto(IntToBoolean(cursor.getInt(9)));
            savedFile.setAllowedToSeeTitle(IntToBoolean(cursor.getInt(10)));
            savedFilesList.add(savedFile);
        }
        cursor.close();

        return savedFilesList;
    }


    public int getDBRecord(int key){
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DBRecord WHERE _key=" + key, null);
        cursor.moveToNext();
        int record = cursor.getInt(1);
        cursor.close();
        return record;
    }

    public void updateDBRecord(int key, int value){
        db = getWritableDatabase();
        db.execSQL("UPDATE " + DB_RECORD_TABLE + " SET value=" + value + " WHERE _key=" +key );
    }

    private boolean IntToBoolean(int i){
        return i != 0;
    }


    public void updateFileDateAndTime(String newDateAndTime, int id, String table) {
        db = getWritableDatabase();
        db.execSQL("UPDATE " + table + " SET " + UNLOCK_DATE_TIME + " = `" + newDateAndTime + "` WHERE id=" +id);
    }

    public void deleteFileFromDB(String table, int id) {
        db = getWritableDatabase();
        db.delete(table, "id=" + id, null);
    }

    private String getFolderTableName(int FileType){
        String tableName = "";
        switch (FileType){
            case TYPE_VIDEO:
                tableName = VIDEO_FOLDERS_TABLE;
                break;
            case TYPE_PHOTO:
                tableName = PHOTO_FOLDERS_TABLE;
                break;
            case TYPE_OTHERS:
                tableName = OTHER_FOLDERS_TABLE;
        }
        return tableName;
    }

    private String getFolderFilesTableName(String tableName) {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select MAX(id) from " + tableName, null);
        cursor.moveToNext();
        int maxId = cursor.getInt(0)+1;
        cursor.close();
        return tableName.charAt(0) + "_" + maxId;

    }

    // Due to the FATAL EXCEPTION: pool-1-thread-1. This function create folder first time.
    private void createFolderFirstTime(SQLiteDatabase db, int FILE_TYPE, String folderName){
        String tableName = getFolderTableName(FILE_TYPE);
        String folderFilesTableName = tableName.charAt(0) + "_" + "0";

        ContentValues values = new ContentValues(2);
        values.put(NAME, folderName);
        values.put(FOLDER_FILES_TABLE_NAME, folderFilesTableName);
        db.insert(tableName, null, values);

        // creating folder for saving folder files
        db.execSQL("CREATE TABLE " + folderFilesTableName + CREATE_TABLE_QUERY);
    }
}
