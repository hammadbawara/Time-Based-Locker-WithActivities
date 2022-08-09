package com.hz_apps.timebasedlocker.ui.LockFiles;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.LockFileAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBRecord;
import com.hz_apps.timebasedlocker.Datebase.DBRepository;
import com.hz_apps.timebasedlocker.Datebase.SavedPhoto;
import com.hz_apps.timebasedlocker.Datebase.SavedVideo;
import com.hz_apps.timebasedlocker.MainActivity;
import com.hz_apps.timebasedlocker.databinding.ActivityLockFilesBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class LockFilesActivity extends AppCompatActivity {

    ActivityLockFilesBinding binding;
    LockFileAdapter adapter;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int YEAR, MONTH, DAY_OF_MONTH, HOUR, MINUTE;
    Calendar calendar;
    ArrayList<File> selectedFiles;
    int TYPES_OF_FILES;
    private DBRepository db;
    private String destinationFolder;
    int last_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateTime();

        selectedFiles = (ArrayList<File>) getIntent().getSerializableExtra("selected_files");
        TYPES_OF_FILES = getIntent().getIntExtra("TYPES_OF_FILES", -1);

        adapter = new LockFileAdapter(this, selectedFiles);


        binding.lockItemsRecyclerview.setAdapter(adapter);
        binding.lockItemsRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        SetDateAndTimeOnAllItems();


        binding.nextBtnActivityLockFiles.setOnClickListener(v -> {
            DateAndTime[] dateAndTimeList = adapter.getDateAndTimeList();

            boolean datesChecked = checkAllDatesAreSet(dateAndTimeList);

            if (datesChecked) {
                moveFilesIntoSafe(dateAndTimeList);
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                this.finish();
            } else{
                Toast.makeText(this, "Please select unlock date for all files", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void moveFilesIntoSafe(DateAndTime[] dateAndTimeList) {
        db = new DBRepository(getApplication());

        updateTime();
        updateValuesAccordingToFile();

        DateAndTime lockDateAndTime = new DateAndTime(LocalDate.of(YEAR, MONTH, DAY_OF_MONTH),
                LocalTime.of(HOUR, MINUTE));

        for (int i=0; i<selectedFiles.size(); i++){

            File source = selectedFiles.get(i);
            moveFile(source, new File(destinationFolder + last_id) );
            last_id += 1;

            switch (TYPES_OF_FILES){
                case 0:
                    SavedVideo video = new SavedVideo(source.getPath(),
                            source.getName(), true,
                            true, true,
                            dateAndTimeList[i], lockDateAndTime, 0);
                    new Thread(() -> db.insertVideo(video)).start();
                    break;
                case 1:
                    SavedPhoto photo = new SavedPhoto(source.getPath(), source.getName(),
                            true, true, true,
                            dateAndTimeList[i], lockDateAndTime, 0);
                    new Thread(() -> db.insertPhoto(photo)).start();
                    break;
            }
            updateLastIdInDatabase();
        }
    }

    /*
     * This function is used to move file from one directory to other.
     * I tried to use source.renameTo(dest) but android does not allow to move file from
     * internal storage to app directory so I am copying files from internal storage and then delete that file
     */
    boolean moveFile(File source, File destination) {
        if (!destination.getParentFile().isDirectory()){
            File parent = destination.getParentFile();
            parent.mkdirs();
        }

        // Copying file
        boolean isFileCopied = false;
        byte[] buffer = new byte[1024];
        int length;
        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            while ((length = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileInputStream.close();
            fileOutputStream.close();
            isFileCopied = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Destination Path " + destination.getAbsolutePath());

        System.out.println("File Copied Status: " + isFileCopied);
        boolean isFileDeleted = false;

        // Deleting file
        if (isFileCopied){
            isFileDeleted = source.delete();
        }

        System.out.println("File Deleted Status: " + isFileDeleted);

        return isFileCopied && isFileDeleted;
    }

    /*
     *
     */
    private boolean checkAllDatesAreSet(DateAndTime[] dateAndTimeList) {
        adapter.setDateNotSetWarning(true);
        boolean datesChecked = true;
        for (int i=0; i<dateAndTimeList.length; i++){
            if (dateAndTimeList[i].getDate() == null){
                adapter.notifyItemChanged(i);
                datesChecked = false;
            }
        }
        return datesChecked;
    }

    private void SetDateAndTimeOnAllItems() {
        // set Date on all items
        binding.setDateLockActivity.setOnClickListener(v -> {

            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    adapter.setDateForAllItems(LocalDate.of(year, month, dayOfMonth));
                    adapter.notifyDataSetChanged();
                }
            }, YEAR, MONTH, DAY_OF_MONTH);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();

        });

        // set Time on all items
        binding.setTimeLockActivity.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                adapter.setTimeForAllItems(LocalTime.of(hourOfDay, minute));
                adapter.notifyDataSetChanged();

            }, 0, 0, true);
            timePickerDialog.show();
        });
    }

    private void updateTime(){
        calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        HOUR = calendar.get(Calendar.HOUR);
        MINUTE = calendar.get(Calendar.MINUTE);
    }

    /*
     * This function updates necessary values
     */
    private void updateValuesAccordingToFile(){
        switch (TYPES_OF_FILES){
            case 0:
                try {last_id = db.getDBRecord(DBRecord.LAST_SAVED_VIDEO_KEY).getValue();}
                catch (Exception ignored){};
                destinationFolder = "/data/data/" + this.getPackageName() + "/files/videos/";
            case 1:
                try {last_id = db.getDBRecord(DBRecord.LAST_SAVED_PHOTO_KEY).getValue();}
                catch (Exception ignored){};
                destinationFolder = "/data/data/" + this.getPackageName() + "/files/photos/";
        }
    }

    private void updateLastIdInDatabase(){
        switch (TYPES_OF_FILES){
            case 0:
                db.updateDBRecord(new DBRecord(DBRecord.LAST_SAVED_VIDEO_KEY, last_id));
                break;
            case 1:
                db.updateDBRecord(new DBRecord(DBRecord.LAST_SAVED_PHOTO_KEY, last_id));
                break;
        }
    }
}