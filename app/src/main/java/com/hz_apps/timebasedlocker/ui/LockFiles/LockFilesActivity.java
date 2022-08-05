package com.hz_apps.timebasedlocker.ui.LockFiles;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.LockFileAdapter;
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
    int YEAR, MONTH, DAY_OF_MONTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<File> selectedFiles = (ArrayList<File>) getIntent().getSerializableExtra("selected_files");

        adapter = new LockFileAdapter(this, selectedFiles);


        binding.lockItemsRecyclerview.setAdapter(adapter);
        binding.lockItemsRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);

        SetDateAndTimeOnAllItems();


        binding.nextBtnActivityLockFiles.setOnClickListener(v -> {
            DateAndTime[] localDateTimes = adapter.getDateAndTimeList();
            adapter.setDateNotSetWarning(true);
            for (int i = 0; i < localDateTimes.length; i++) {
                if (localDateTimes[i] == null) {
                    adapter.notifyItemChanged(i);
                }
            }
            Intent intent = new Intent(this, MainActivity.class);
            // clearing all previous activities
            intent.putExtra("DatesList", localDateTimes);
            intent.putExtra("selected_files", selectedFiles);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        });

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
}