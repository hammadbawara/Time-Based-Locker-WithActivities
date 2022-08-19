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
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.Datebase.SavedFolder;
import com.hz_apps.timebasedlocker.databinding.ActivitySavedFilesBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class SavedFilesActivity extends AppCompatActivity {
    ActivitySavedFilesBinding binding;
    SavedFilesViewModel viewModel;
    SavedFolder savedFolder;
    SavedFilesAdapter adapter;
    public static int FILES_TYPE;
    public static String FILES_TABLE_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SavedFilesViewModel.class);

        savedFolder = (SavedFolder) getIntent().getSerializableExtra("saved_folder");
        FILES_TYPE = getIntent().getIntExtra("FILES_TYPE", -1);
        FILES_TABLE_NAME = savedFolder.getFilesTable();

        main();


        binding.addFilesSavedFiles.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectFolderActivity.class);
            startActivity(intent);
        });

        binding.swipeRefreshSavedFiles.setOnRefreshListener(this::main);



    }

    private void main(){
        binding.progressBarSavedFiles.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(() -> {
            fetchDataFromDB();
            runOnUiThread(this::setFilesInRecyclerView);
        });
    }

    private void fetchDataFromDB(){
        DBHelper db = DBHelper.getINSTANCE();
        List<SavedFile> savedFileList = db.getSavedFiles(savedFolder.getFilesTable());
        viewModel.setSavedFilesList(savedFileList);
        adapter = new SavedFilesAdapter(this, viewModel.getSavedFilesList(), binding.toolbarSavedFiles);
    }

    private void setFilesInRecyclerView(){
        RecyclerView recyclerView = binding.recyclerviewSavedFiles;
        recyclerView.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/120;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));
        binding.progressBarSavedFiles.setVisibility(View.GONE);
        binding.swipeRefreshSavedFiles.setRefreshing(false);
    }
}