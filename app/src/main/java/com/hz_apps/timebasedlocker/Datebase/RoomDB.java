package com.hz_apps.timebasedlocker.Datebase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {SavedVideo.class, DBRecord.class, SavedPhoto.class}, version = 4)
public abstract class RoomDB extends RoomDatabase {

    abstract DBDao dbDao();

    private static volatile RoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RoomDB getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (RoomDatabase.class){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RoomDB.class, "locked_files.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallBack)
                            .allowMainThreadQueries()
                            .build();
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            executorService.execute(() ->{
                DBDao dbDao = INSTANCE.dbDao();
            });
        }
    };

}
