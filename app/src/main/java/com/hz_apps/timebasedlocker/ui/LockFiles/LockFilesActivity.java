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
import com.hz_apps.timebasedlocker.Datebase.SavedVideo;
import com.hz_apps.timebasedlocker.MainActivity;
import com.hz_apps.timebasedlocker.databinding.ActivityLockFilesBinding;

import java.io.File;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateTime();
        selectedFiles = (ArrayList<File>) getIntent().getSerializableExtra("selected_files");

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
        DBRepository db = new DBRepository(getApplication());
        int last_video_key = 0;
        try {
            last_video_key = db.getDBRecord(DBRecord.LAST_VIDEO_ID).getValue();
        }catch (Exception ignored){};

        updateTime();
        DateAndTime lockDateAndTime = new DateAndTime(LocalDate.of(YEAR, MONTH, DAY_OF_MONTH),
                LocalTime.of(HOUR, MINUTE));

        for (int i=0; i<selectedFiles.size(); i++){
            File source = selectedFiles.get(i);
            //moveFile(source, new File("data/data/" + this.getPackageName() + "/files/videos/" + last_video_key) );
            //moveFile(source, new File("/storage/emulated/0/" + last_video_key + ".jpg") );
            boolean result = source.renameTo(new File("data/data/" + this.getPackageName() + "/files/videos/" + last_video_key));
            System.out.println(result);
            last_video_key += 1;
            SavedVideo video = new SavedVideo(source.getPath(),
                    source.getName(), true,
                    true, true,
                    dateAndTimeList[i], lockDateAndTime, 0);
            new Thread(() ->{
                db.insertVideo(video);
            }).start();
        }
    }

    /*
     * This function is used to move file from one directory to other.
     * Technique for moving files is simple just rename files path to desired directory.
     */
    private boolean moveFile(File source, File destination) {
        boolean isDirectoryMade = false;

        if (!destination.getParentFile().isDirectory()){
            File parent = destination.getParentFile();
            System.out.println("Parent: " + parent.getAbsolutePath());
            isDirectoryMade = parent.mkdirs();
            System.out.println("isDirectoryMade: " + isDirectoryMade);
        }

        boolean isFileRenamed = source.renameTo(destination);


        System.out.println("destination: " + destination.getAbsolutePath());

        System.out.println("isFileRenamed: " + isFileRenamed);

        return isDirectoryMade && isFileRenamed;
    }

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
}