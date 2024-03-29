package com.hz_apps.timebasedlocker.ui.ShowSavedFiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hz_apps.timebasedlocker.Adapters.SavedFilesAdapter;
import com.hz_apps.timebasedlocker.Database.DBHelper;
import com.hz_apps.timebasedlocker.Database.SavedFile;
import com.hz_apps.timebasedlocker.Database.SavedFolder;
import com.hz_apps.timebasedlocker.TimeUpdate.DateTimeManager;
import com.hz_apps.timebasedlocker.databinding.ActivitySavedFilesBinding;
import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class SavedFilesActivity extends AppCompatActivity implements DateTimeManager.UpdateTimeListener {
    public static int FILES_TYPE;
    public static String FILES_TABLE_NAME;
    ActivitySavedFilesBinding binding;
    SavedFilesViewModel viewModel;
    SavedFolder savedFolder;
    SavedFilesAdapter adapter;
    // if user delete item in MediaViewerActivity then that deleted file will be removed from recyclerview by using index
    public static int deletedFileIndex = -1;
    DateAndTime dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarSavedFiles);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarSavedFiles.setNavigationOnClickListener((arrow) -> onBackPressed());

        viewModel = new ViewModelProvider(this).get(SavedFilesViewModel.class);

        // Intent
        savedFolder = (SavedFolder) getIntent().getSerializableExtra("saved_folder");
        FILES_TYPE = getIntent().getIntExtra("FILES_TYPE", -1);
        FILES_TABLE_NAME = savedFolder.getFilesTable();

        // Getting last time saved in storage
        dateAndTime = DateTimeManager.getDateAndTime(this);

        // Setting time in textView
        binding.timeViewSavedFiles.setText(dateAndTime.toString());

        getSupportActionBar().setTitle(savedFolder.getName());

        DateTimeManager.update(this, this);

        main();


        binding.addFilesSavedFiles.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectFolderActivity.class);
            startActivity(intent);
        });

        binding.swipeRefreshSavedFiles.setOnRefreshListener(this::main);


    }

    private void main() {
        binding.progressBarSavedFiles.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(() -> {
            fetchDataFromDB();
            runOnUiThread(this::setFilesInRecyclerView);
        });
    }

    private void fetchDataFromDB() {
        DBHelper db = DBHelper.getINSTANCE();
        List<SavedFile> savedFileList = db.getSavedFiles(savedFolder.getFilesTable());
        viewModel.setSavedFilesList(savedFileList);
        adapter = new SavedFilesAdapter(this, viewModel.getSavedFilesList(), binding.toolbarSavedFiles, binding.addFilesSavedFiles);
        adapter.setDateAndTime(dateAndTime);
    }

    private void setFilesInRecyclerView() {
        RecyclerView recyclerView = binding.recyclerviewSavedFiles;
        recyclerView.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels / displayMetrics.density) / 120;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));
        binding.progressBarSavedFiles.setVisibility(View.GONE);
        binding.swipeRefreshSavedFiles.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        if (DBHelper.isAnyChangeInFiles) {
            main();
            DBHelper.isAnyChangeInFiles = false;
        }
        if (deletedFileIndex != -1){
            viewModel.getSavedFilesList().remove(deletedFileIndex);
            adapter.notifyItemRemoved(deletedFileIndex);
            deletedFileIndex = -1;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onUpdateTime(DateAndTime dateAndTime) {
        runOnUiThread(() ->{
            // updating Date And Time in textView
            binding.timeViewSavedFiles.setText(dateAndTime.toString());
            // updating date and time in adapter
            adapter.setDateAndTime(dateAndTime);
        });
    }
}