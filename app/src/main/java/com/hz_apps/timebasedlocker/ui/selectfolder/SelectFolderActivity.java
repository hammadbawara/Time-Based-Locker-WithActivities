package com.hz_apps.timebasedlocker.ui.selectfolder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.FolderListAdapter;
import com.hz_apps.timebasedlocker.databinding.ActivitySelectFolderBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectFolderActivity extends AppCompatActivity {

    ActivitySelectFolderBinding binding;
    private SelectFolderViewModel mViewModel;
    private int TypesOfFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(SelectFolderViewModel.class);

        if (requestPermission()){
            // showing progressbar until task completes
            binding.progressBarSelectFolder.setVisibility(View.VISIBLE);
            // Using executor for running this function in background thread so Ui respond
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(()->{
                getFoldersFromStorage();
                this.runOnUiThread(this::showItemsInRecyclerView);
            });


        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }


    }
    private void getFoldersFromStorage(){
        if (mViewModel.getFoldersList().size() == 0){
            String path = "/storage/emulated/0/";

            // extension tells which types of files we are looking
            String[] extensions;
            TypesOfFiles = getIntent().getIntExtra("Type_Of_Files", -1);
            switch (TypesOfFiles){
                case 0:
                    extensions = FilesExtensions.videosExtensions;
                    break;
                case 1:
                    extensions = FilesExtensions.imagesExtensions;
                    break;
                default:
                    extensions = new String[]{""};
            }

            // Getting folders list containing images
            FindSpecificFilesFolders findSpecificFilesFolders = new FindSpecificFilesFolders(path, extensions,
                    new String[] {"Android"});
            mViewModel.setFoldersList(findSpecificFilesFolders.getFoldersList());
        }
    }

    private void showItemsInRecyclerView(){
        // check if folderList is zero then set message
        if (mViewModel.getFoldersList().size() == 0){
            binding.filesNotFoundTextView.setVisibility(View.VISIBLE);
            // hiding progressbar when all tasks completes
            binding.progressBarSelectFolder.setVisibility(View.GONE);
            return;
        }
        // Setting items in recyclerView
        FolderListAdapter adapter = new FolderListAdapter(this, mViewModel.getFoldersList(), TypesOfFiles);
        binding.selectFolderRecyclerView.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/155;
        binding.selectFolderRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));

        // hiding progressbar when all tasks completes
        binding.progressBarSelectFolder.setVisibility(View.GONE);
    }

    private boolean requestPermission(){
        boolean isStoragePermissionGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!isStoragePermissionGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        isStoragePermissionGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return isStoragePermissionGranted;
    }
}