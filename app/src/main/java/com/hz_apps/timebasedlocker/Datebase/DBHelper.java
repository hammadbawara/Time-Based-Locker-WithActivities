package com.hz_apps.timebasedlocker.Datebase;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    /*
    These are key for DBRecord Table. In DBRecord LAST_SAVED_VIDEO_KEY mean key to get last saved video from db.
     */
    public static final int LAST_SAVED_VIDEO_KEY = 1;
    public static final int LAST_SAVED_PHOTO_KEY = 2;
    public static final int VIDEO_TYPE = 0;
    public static final int PHOTO_TYPE = 1;
    public static final int OTHER_TYPE = 2;
    // Tables names
    public static final String SAVED_VIDEO_TABLE = "saved_videos";
    public static final String SAVED_PHOTO_TABLE = "saved_photos";
    public static final String VIDEO_FOLDERS_TABLE = "video_folders_list";
    public static final String PHOTO_FOLDERS_TABLE = "photo_folders_list";
    private final String FOLDER_FILES_TABLE_NAME = "folder_files_table_name";
    private final String DB_RECORD_TABLE = "DBRecord";
    private final String OTHER_FOLDERS_TABLE = "others_files_folders_list";
    // Tracking database
    public static boolean isAnyChangeInFiles = false;
    public static boolean isAnyChangeInFolders = false;
    private static DBHelper dbHelper;
    // Table values
    private final String ORIGINAL_PATH = "orignal_path";
    private final String PATH = "path";
    private final String NAME = "name";
    private final String UNLOCK_DATE_TIME = "unlock_date_time";
    private final String LOCK_DATE_TIME = "lock_date_time";
    private final String FILE_TYPE = "file_type";
    private final String IS_ALLOWED_TO_EXTEND_TIME = "is_allowed_to_extend_date_time";
    private final String IS_ALLOWED_TO_SEE_PHOTO = "is_allowed_to_see_photo";
    private final String IS_ALLOWED_TO_SEE_TITLE = "is_allowed_to_see_title";
    private final String IS_FILE_UNLOCKED = "is_unlocked";
    private final String CREATE_TABLE_QUERY = "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            ORIGINAL_PATH + " TEXT, " + // 1
            PATH + " TEXT," + // 2
            NAME + " TEXT, " + // 3
            UNLOCK_DATE_TIME + " TEXT, " + // 4
            LOCK_DATE_TIME + " TEXT, " + // 5
            FILE_TYPE + " INTEGER, " + //6
            IS_ALLOWED_TO_EXTEND_TIME + " INTEGER, " + // 7
            IS_ALLOWED_TO_SEE_PHOTO + " INTEGER, " + // 8
            IS_ALLOWED_TO_SEE_TITLE + " INTEGER," +  // 9
            IS_FILE_UNLOCKED + " INTEGER)"; // 10
    private SQLiteDatabase db;
    private DBChangeListener dbChangeListener;

    public DBHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    public static DBHelper getINSTANCE() {
        return dbHelper;
    }

    public static void createInstanceForOverApplication(Application application) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(application.getApplicationContext(), "filesDB.db");
        }
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

        db.execSQL("INSERT INTO " + DB_RECORD_TABLE + "(_key, value) VALUES(1, 1)");
        db.execSQL("INSERT INTO " + DB_RECORD_TABLE + "(_key, value) VALUES(2, 1)");
        createFolderFirstTime(db, VIDEO_TYPE, "Videos");
        createFolderFirstTime(db, OTHER_TYPE, "Other Files");
        createFolderFirstTime(db, PHOTO_TYPE, "Photos");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * This method insert savedFile into database
     *
     * @param savedFile   : savedFile that would be saved into database
     * @param nameOfTable : table name where files would store
     */
    public void insert_file(SavedFile savedFile, String nameOfTable) {

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ORIGINAL_PATH, savedFile.getOriginalPath());
        values.put(PATH, savedFile.getPath());
        values.put(NAME, savedFile.getName());
        values.put(UNLOCK_DATE_TIME, savedFile.getUnlockDateTime().toString());
        values.put(LOCK_DATE_TIME, savedFile.getLockDateTime().toString());
        values.put(FILE_TYPE, savedFile.getFileType());
        values.put(IS_ALLOWED_TO_EXTEND_TIME, savedFile.isAllowedToExtendDateTime());
        values.put(IS_ALLOWED_TO_SEE_TITLE, savedFile.isAllowedToSeeTitle());
        values.put(IS_ALLOWED_TO_SEE_PHOTO, savedFile.isAllowedToSeePhoto());
        values.put(IS_FILE_UNLOCKED, 0);
        db.insert(nameOfTable, null, values);
        isAnyChangeInFiles = true;
    }

    /**
     * @param folderName : Folder name
     * @param FileType   : Types of files will be in the folder. e.g TYPE_VIDEOS, TYPE_PHOTOS and TYPE_OTHERS
     */
    public void create_folder(String folderName, int FileType) {
        db = getWritableDatabase();

        String tableName = getFolderTableName(FileType);
        String folderFilesTableName = generateFolderFilesTableName(tableName);

        ContentValues values = new ContentValues(1);
        values.put(NAME, folderName);
        values.put(FOLDER_FILES_TABLE_NAME, folderFilesTableName);

        db.insert(tableName, null, values);

        // creating folder for saving folder files
        db.execSQL("CREATE TABLE " + folderFilesTableName + CREATE_TABLE_QUERY);

        if (dbChangeListener != null) {
            dbChangeListener.onFolderChange();
        }
    }

    public List<SavedFolder> getSavedFolders(int FILE_TYPE) {
        db = getReadableDatabase();
        String tableName = getFolderTableName(FILE_TYPE);
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        List<SavedFolder> savedFolderList = new ArrayList<>();

        while (cursor.moveToNext()) {
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
     *
     * @param table : tables names from where database needs to get
     * @return it return List<SavedFiles>.
     */
    public List<SavedFile> getSavedFiles(String table) {
        db = getReadableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM  " + table, null);

        List<SavedFile> savedFilesList = new ArrayList<>();

        while (cursor.moveToNext()) {
            SavedFile savedFile = new SavedFile();
            savedFile.setId(cursor.getInt(0));
            savedFile.setOriginalPath(cursor.getString(1));
            savedFile.setPath(cursor.getString(2));
            savedFile.setName(cursor.getString(3));
            savedFile.setUnlockDateTime(DateAndTime.parse(cursor.getString(4)));
            savedFile.setLockDateTime(DateAndTime.parse(cursor.getString(5)));
            savedFile.setFileType(cursor.getInt(6));
            savedFile.setAllowedToExtendDateTime(IntToBoolean(cursor.getInt(7)));
            savedFile.setAllowedToSeePhoto(IntToBoolean(cursor.getInt(8)));
            savedFile.setAllowedToSeeTitle(IntToBoolean(cursor.getInt(9)));
            savedFile.setIsUnlocked(IntToBoolean(cursor.getInt(10)));
            savedFilesList.add(savedFile);
        }
        cursor.close();

        return savedFilesList;
    }

    public int getDBRecord(int key) {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DBRecord WHERE _key=" + key, null);
        cursor.moveToNext();
        int record = cursor.getInt(1);
        cursor.close();
        return record;
    }

    public void updateDBRecord(int key, int value) {
        db = getWritableDatabase();
        db.execSQL("UPDATE " + DB_RECORD_TABLE + " SET value=" + value + " WHERE _key=" + key);
    }

    private boolean IntToBoolean(int i) {
        return i != 0;
    }

    public void updateFileDateAndTime(String newDateAndTime, int id, String table) {
        db = getWritableDatabase();
        db.execSQL("UPDATE " + table + " SET " + UNLOCK_DATE_TIME + " = `" + newDateAndTime + "` WHERE id=" + id);
    }

    public void updateFileUnlocked(int id, String table){
        db = getWritableDatabase();
        db.execSQL("UPDATE " + table + " SET " + IS_FILE_UNLOCKED + "=1 WHERE id=" + id);
    }

    public void deleteFileFromDB(String table, int id) {
        db = getWritableDatabase();
        db.delete(table, "id=" + id, null);
    }

    public void deleteFolder(SavedFolder folder, int FILES_TYPE){
        db = getWritableDatabase();
        String folderTable = getFolderTableName(FILES_TYPE);

        ArrayList<String> filesPathsList = new ArrayList<>();
        ArrayList<Integer> idsList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + "id, " + PATH + " FROM " + folder.getFilesTable(), null);

        while (cursor.moveToNext()){
            idsList.add(cursor.getInt(0));
            filesPathsList.add(cursor.getString(1));
        }
        cursor.close();

        for (int i=0; i<filesPathsList.size(); i++){
            // Deleting file from database
            int id = idsList.get(i);
            File file = new File(filesPathsList.get(i));
            this.deleteFileFromDB(folder.getFilesTable(), id);
            // Deleting file from storage
            file.delete();
        }

        db.execSQL("DROP TABLE " + folder.getFilesTable());

        db.delete(folderTable,"id="+folder.getId(), null);
        dbChangeListener.onFolderChange();
    }

    private String getFolderTableName(int FileType) {
        String tableName = "";
        switch (FileType) {
            case VIDEO_TYPE:
                tableName = VIDEO_FOLDERS_TABLE;
                break;
            case PHOTO_TYPE:
                tableName = PHOTO_FOLDERS_TABLE;
                break;
            case OTHER_TYPE:
                tableName = OTHER_FOLDERS_TABLE;
        }
        return tableName;
    }

    /**
     * This function find the table name in which folder files are stored
     * There are three tables in which folders are stored. In Video Folder Table Videos Folders are stored.
     * This function find the Max(id). If previously there were 3 folders then max id will be 3 and then increment it to 4
     * For video table name would be like this v4. For Photos it would be p4 etc.
     * @param tableName : name of the table where folders are stored
     * @return table name where files will be stored.
     */
    private String generateFolderFilesTableName(String tableName) {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select MAX(id) from " + tableName, null);
        cursor.moveToNext();
        int maxId = cursor.getInt(0) + 1;
        cursor.close();
        return tableName.charAt(0) + "_" + maxId;

    }


    // Database change listener

    // Due to the FATAL EXCEPTION: pool-1-thread-1. This function create folder first time.
    private void createFolderFirstTime(SQLiteDatabase db, int FILE_TYPE, String folderName) {
        String tableName = getFolderTableName(FILE_TYPE);
        String folderFilesTableName = tableName.charAt(0) + "_" + "0";

        ContentValues values = new ContentValues(2);
        values.put(NAME, folderName);
        values.put(FOLDER_FILES_TABLE_NAME, folderFilesTableName);
        db.insert(tableName, null, values);

        // creating folder for saving folder files
        db.execSQL("CREATE TABLE " + folderFilesTableName + CREATE_TABLE_QUERY);
    }

    public void setDBChangeListener(DBChangeListener listener) {
        dbChangeListener = listener;
    }

    public interface DBChangeListener {
        void onFolderChange();
    }
}
