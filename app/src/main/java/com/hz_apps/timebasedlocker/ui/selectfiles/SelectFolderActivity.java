package com.hz_apps.timebasedlocker.ui.selectfiles;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.FolderListAdapter;
import com.hz_apps.timebasedlocker.databinding.ActivitySelectFolderBinding;

import java.util.ArrayList;

public class SelectFolderActivity extends AppCompatActivity {

    ActivitySelectFolderBinding binding;
    private ArrayList<Folder> foldersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (requestPermission()){

        }else{
            Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
        }
        showItemsInRecyclerView();



    }

    private void showItemsInRecyclerView(){
        String path = "/storage/emulated/0/";

        // extension tells which types of files we are looking
        String[] extensions = new String[] {".jpg", ".png", ".gif"};

        // Getting folders list containing images
        FindSpecificFilesFolders findSpecificFilesFolders = new FindSpecificFilesFolders(path, extensions);
        findSpecificFilesFolders.setIgnoreFoldersList(new String[] {"Android"});
        foldersList = findSpecificFilesFolders.getFoldersList();

        // Setting items in recyclerView
        FolderListAdapter adapter = new FolderListAdapter(this, foldersList);
        binding.selectFolderRecyclerView.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/155;
        binding.selectFolderRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));
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