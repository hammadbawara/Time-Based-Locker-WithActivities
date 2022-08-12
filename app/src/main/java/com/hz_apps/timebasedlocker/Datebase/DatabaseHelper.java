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

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table values
    private final String PATH = "path";
    private final String TITLE = "title";
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

    // Tables names
    public static final String SAVED_VIDEO_TABLE = "saved_videos";
    public static final String SAVED_PHOTO_TABLE = "saved_photos";
    private static final String DB_RECORD_TABLE = "DBRecord";

    // Tracking database
    public boolean isAnyFileInserted = true;

    private static DatabaseHelper dbHelper;

    public static DatabaseHelper getINSTANCE(Application application){
        if (dbHelper != null){
            return dbHelper;
        }
        return new DatabaseHelper(application.getApplicationContext(), "filesDB.db");
    }

    private final String CREATE_TABLE_QUERY = "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            PATH + " TEXT, " +
            TITLE + " TEXT, " +
            UNLOCK_DATE_TIME+" TEXT, " +
            LOCK_DATE_TIME + " TEXT, " +
            IS_FILE + " INTEGER, " +
            FILE_TYPE + "INTEGER, " +
            IS_ALLOWED_TO_EXTEND_TIME +" INTEGER, " +
            IS_ALLOWED_TO_SEE_PHOTO + " INTEGER, " +
            IS_ALLOWED_TO_SEE_TITLE + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SAVED_VIDEO_TABLE + CREATE_TABLE_QUERY);
        db.execSQL("CREATE TABLE " + SAVED_PHOTO_TABLE + CREATE_TABLE_QUERY);
        db.execSQL("CREATE TABLE " + DB_RECORD_TABLE + "(_key INTEGER PRIMARY KEY, value INTEGER)");
        db.execSQL("INSERT INTO "  + DB_RECORD_TABLE + "(_key, value) VALUES(1, 1)");
        db.execSQL("INSERT INTO "  + DB_RECORD_TABLE + "(_key, value) VALUES(2, 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert_file(SavedFile file, String table){
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PATH, file.getPath());
        values.put(TITLE, file.getTitle());
        values.put(UNLOCK_DATE_TIME, file.getUnlockDateTime().toString());
        values.put(LOCK_DATE_TIME, file.getLockDateTime().toString());
        values.put(IS_FILE, file.isFile());
        values.put(IS_ALLOWED_TO_EXTEND_TIME, file.isAllowedToExtendDateTime());
        values.put(IS_ALLOWED_TO_SEE_TITLE, file.isAllowedToSeeTitle());
        values.put(IS_ALLOWED_TO_SEE_PHOTO, file.isAllowedToSeePhoto());
        long result = db.insert(table, null, values);
        isAnyFileInserted = true;
        return result != -1;
    }

    public List<SavedFile> getSavedFiles(String table){
        db = getReadableDatabase();
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM  " + table,null );

        List<SavedFile> savedFilesList = new ArrayList<>();

        while (cursor.moveToNext()){
            SavedFile savedFile = new SavedFile();
            savedFile.setId(cursor.getInt(0));
            savedFile.setPath(cursor.getString(1));
            savedFile.setTitle(cursor.getString(2));
            savedFile.setUnlockDateTime(DateAndTime.parse(cursor.getString(3)));
            savedFile.setLockDateTime(DateAndTime.parse(cursor.getString(4)));
            savedFile.setFile(IntToBoolean(cursor.getInt(5)));
            savedFile.setAllowedToExtendDateTime(IntToBoolean(cursor.getInt(6)));
            savedFile.setAllowedToSeePhoto(IntToBoolean(cursor.getInt(7)));
            savedFile.setAllowedToSeeTitle(IntToBoolean(cursor.getInt(8)));
            savedFilesList.add(savedFile);
        }
        cursor.close();

        return savedFilesList;
    }

    public int createFolder(String name, int Type){
        return 1;
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
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(String.valueOf(key), value);
        db = getWritableDatabase();
        db.execSQL("UPDATE " + DB_RECORD_TABLE + " SET value=" + value + " WHERE _key=" +key );
    }

    private boolean IntToBoolean(int i){
        return i != 0;
    }


}
