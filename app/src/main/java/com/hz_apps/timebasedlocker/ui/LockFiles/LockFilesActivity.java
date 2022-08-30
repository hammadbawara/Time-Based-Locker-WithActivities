package com.hz_apps.timebasedlocker.ui.LockFiles;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.LockFileAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.Dialogs.ConfirmationLockFileDialog;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.ActivityLockFilesBinding;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LockFilesActivity extends AppCompatActivity implements ConfirmationLockFileDialog.ConfirmationLockFileDialogData {

    ActivityLockFilesBinding binding;
    LockFileAdapter adapter;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int YEAR, MONTH, DAY_OF_MONTH, HOUR, MINUTE;
    Calendar calendar;
    ArrayList<File> selectedFiles;
    int FILES_TYPES;
    int last_id = 1;
    ProgressDialog progress;
    private DBHelper db;
    private String destinationFolder;
    private ExecutorService executor;
    private boolean allowedExtendDate, allowSeeTile, allowSeeImage;
    private DateAndTime[] dateAndTimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarLockFiles);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarLockFiles.setNavigationOnClickListener((arrow) -> onBackPressed());
        updateTime();
        getSupportActionBar().setTitle("Select Unlock Date and Time");

        selectedFiles = (ArrayList<File>) getIntent().getSerializableExtra("selected_files");
        FILES_TYPES = SavedFilesActivity.FILES_TYPE;

        // Initializing Database
        db = DBHelper.getINSTANCE();

        adapter = new LockFileAdapter(this, selectedFiles);


        binding.lockItemsRecyclerview.setAdapter(adapter);
        binding.lockItemsRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        SetDateAndTimeOnAllItems();


        binding.nextBtnActivityLockFiles.setOnClickListener(v -> {
            dateAndTimeList = adapter.getDateAndTimeList();

            boolean datesChecked = checkAllDatesAreSet(dateAndTimeList);

            if (datesChecked) {
                ConfirmationLockFileDialog dialogFragment = new ConfirmationLockFileDialog();
                ConfirmationLockFileDialog.listener = this;
                dialogFragment.show(getSupportFragmentManager(), "GET");
            } else {
                Toast.makeText(this, "Please select unlock date for all files", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void moveFilesIntoSafe() {

        executor = Executors.newSingleThreadExecutor();

        progress = new ProgressDialog(this);
        progress.setTitle("File(s) Saving in Lock");

        progress.setIndeterminate(false);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", (dialog, which) -> {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
                dialog.dismiss();
            }
        });

        executor.execute(() -> {

            // updating time for time when file is locked
            updateTime();
            // date when files lock
            // In local datetime Month 1 = January
            // but in DatePickerDialog Month 0 = January
            DateAndTime lockDateAndTime = new DateAndTime(LocalDate.of(YEAR, MONTH + 1, DAY_OF_MONTH),
                    LocalTime.of(HOUR, MINUTE));
            // update path where files store and also last id
            updateValuesAccordingToFile();
            System.out.println("Destination Folder " + destinationFolder);
            System.out.println("FILE TYPE  " + FILES_TYPES);

            int total_files = selectedFiles.size();

            // Create folder if not exists
            File destination = new File(destinationFolder);
            if (!destination.isDirectory()) {
                destination.mkdirs();
            }

            for (int i = 0; i < total_files; i++) {

                File source = selectedFiles.get(i);

                progress.setProgressNumberFormat((i + 1) + "/" + total_files);
                progress.setProgress(0);
                runOnUiThread(() -> {
                    progress.setMessage(source.getName());
                    progress.show();
                });

                // moving file into app directory
                moveFile(source, new File(destinationFolder + last_id));

                if (executor.isShutdown()) {
                    break;
                }

                // saving file information in database

                SavedFile file = new SavedFile();
                file.setId(last_id);
                file.setOriginalPath(source.getPath());
                file.setName(source.getName());
                file.setUnlockDateTime(dateAndTimeList[i]);
                file.setLockDateTime(lockDateAndTime);
                file.setAllowedToExtendDateTime(allowedExtendDate);
                file.setAllowedToSeePhoto(allowSeeImage);
                file.setAllowedToSeeTitle(allowSeeTile);
                file.setPath(destinationFolder + last_id);
                file.setFileType(FILES_TYPES);

                db.insert_file(file, SavedFilesActivity.FILES_TABLE_NAME);

                last_id += 1;
                updateLastIdInDatabase();
            }
            progress.dismiss();
            Intent intent = new Intent(this, SavedFilesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            this.finish();
        });


    }

    /*
     * This function is used to move file from one directory to other.
     * I tried to use source.renameTo(dest) but android does not allow to move file from
     * internal storage to app directory so I am copying files from internal storage and then delete that file
     */
    boolean moveFile(File source, File destination) {

        long total_size_of_file = source.length();
        long file_copied = 0;
        // Copying file
        boolean isFileCopied = false;
        byte[] buffer = new byte[1024000];
        int length;
        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            while ((length = fileInputStream.read(buffer)) > 0) {
                if (executor.isShutdown()) {
                    return true;
                }
                fileOutputStream.write(buffer, 0, length);
                file_copied += length;
                progress.setProgress((int) ((file_copied * 100L) / total_size_of_file));
            }
            fileInputStream.close();
            fileOutputStream.close();
            isFileCopied = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean isFileDeleted = false;

        // Deleting file
        if (isFileCopied) {
            //isFileDeleted = source.delete();
        }

        return isFileCopied && isFileDeleted;
    }

    /*
     *
     */
    private boolean checkAllDatesAreSet(DateAndTime[] dateAndTimeList) {
        boolean datesChecked = true;
        for (DateAndTime dateAndTime : dateAndTimeList) {
            if (dateAndTime.getDate() == null) {
                datesChecked = false;
                break;
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
                    adapter.setDateOnAllItems(LocalDate.of(year, month+1, dayOfMonth));
                }
            }, YEAR, MONTH, DAY_OF_MONTH);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();

        });

        // set Time on all items
        binding.setTimeLockActivity.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                adapter.setTimeOnAllItems(LocalTime.of(hourOfDay, minute));

            }, 0, 0, true);
            timePickerDialog.show();
        });
    }

    private void updateTime() {
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
    private void updateValuesAccordingToFile() {
        switch (FILES_TYPES) {
            case DBHelper.VIDEO_TYPE:
                last_id = db.getDBRecord(DBHelper.LAST_SAVED_VIDEO_KEY);
                destinationFolder = getString(R.string.saved_videos_path);
                break;
            case DBHelper.PHOTO_TYPE:
                last_id = db.getDBRecord(DBHelper.LAST_SAVED_PHOTO_KEY);
                destinationFolder = getString(R.string.saved_photos_path);
                break;
        }
    }

    private void updateLastIdInDatabase() {
        switch (FILES_TYPES) {
            case DBHelper.VIDEO_TYPE:
                db.updateDBRecord(DBHelper.LAST_SAVED_VIDEO_KEY, last_id);
                break;
            case DBHelper.PHOTO_TYPE:
                db.updateDBRecord(DBHelper.LAST_SAVED_PHOTO_KEY, last_id);
                break;
        }
    }
    public void lockFiles(boolean allowExtendDate, boolean allowSeeTitle, boolean allowSeeImage) {
        this.allowedExtendDate = allowExtendDate;
        this.allowSeeTile = allowSeeTitle;
        this.allowSeeImage = allowSeeImage;

        moveFilesIntoSafe();
    }
}