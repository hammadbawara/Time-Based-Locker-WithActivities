package com.hz_apps.timebasedlocker.ui.LockFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.hz_apps.timebasedlocker.Adapters.LockFileAdapter;
import com.hz_apps.timebasedlocker.databinding.ActivityLockFilesBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class LockFilesActivity extends AppCompatActivity {

    ActivityLockFilesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<File> selectedFiles = (ArrayList<File>) getIntent().getSerializableExtra("selected_files");

        LockFileAdapter adapter = new LockFileAdapter(this, selectedFiles);

        System.out.println("Adapter size " + adapter.getItemCount());


        binding.lockItemsRecyclerview.setAdapter(adapter);
        binding.lockItemsRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);

        binding.setDateAllLockFiles.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                adapter.setDateForAllItems(year + " " + (month+1) + " " + dayOfMonth);
                binding.setDateAllLockFiles.setText(year + " " + (month+1) + " " + dayOfMonth);
                adapter.notifyDataSetChanged();
            }, YEAR, MONTH, DAY_OF_MONTH);
            datePickerDialog.show();
        });

    }
}