package com.hz_apps.timebasedlocker.ui.LockFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.hz_apps.timebasedlocker.Adapters.LockFileAdapter;
import com.hz_apps.timebasedlocker.databinding.ActivityLockFilesBinding;

import java.io.File;
import java.util.ArrayList;

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






    }
}