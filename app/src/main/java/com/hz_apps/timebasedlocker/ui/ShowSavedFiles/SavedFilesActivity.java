package com.hz_apps.timebasedlocker.ui.ShowSavedFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.hz_apps.timebasedlocker.Adapters.SavedFilesAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.Datebase.SavedFolder;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.ActivitySavedFilesBinding;

import java.util.List;
import java.util.concurrent.Executors;

public class SavedFilesActivity extends AppCompatActivity {
    ActivitySavedFilesBinding binding;
    SavedFilesViewModel viewModel;
    SavedFolder savedFolder;
    SavedFilesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SavedFilesViewModel.class);

        savedFolder = (SavedFolder) getIntent().getSerializableExtra("saved_folder");

        Executors.newSingleThreadExecutor().execute(() -> {
            fetchDataFromDB();
            runOnUiThread(this::setFilesInRecyclerView);
        });



    }

    private void fetchDataFromDB(){
        binding.progressBarSavedFiles.setVisibility(View.VISIBLE);
        DBHelper db = DBHelper.getINSTANCE();
        List<SavedFile> savedFileList = db.getSavedFiles(savedFolder.getFilesTable());
        viewModel.setSavedFilesList(savedFileList);
        adapter = new SavedFilesAdapter(this, viewModel.getSavedFilesList(), binding.toolbarSavedFiles);
    }

    private void setFilesInRecyclerView(){
        RecyclerView recyclerView = binding.recyclerviewSavedFiles;
        recyclerView.setAdapter(adapter);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/120;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));
        binding.progressBarSavedFiles.setVisibility(View.GONE);
    }
}