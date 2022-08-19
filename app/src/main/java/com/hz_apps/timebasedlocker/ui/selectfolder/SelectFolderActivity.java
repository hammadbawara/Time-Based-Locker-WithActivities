package com.hz_apps.timebasedlocker.ui.selectfolder;

import android.Manifest;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.FolderListAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.databinding.ActivitySelectFolderBinding;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectFolderActivity extends AppCompatActivity {

    ActivitySelectFolderBinding binding;
    private SelectFolderViewModel mViewModel;
    private int FILES_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel = new ViewModelProvider(this).get(SelectFolderViewModel.class);

        requestPermission();

    }

    private void main(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            getFoldersFromStorage();
            this.runOnUiThread(this::showItemsInRecyclerView);
        });
    }
    private void getFoldersFromStorage(){
        if (mViewModel.getFoldersList().size() == 0){
            String path = System.getenv("EXTERNAL_STORAGE");

            // extension tells which types of files we are looking
            String[] extensions;
            FILES_TYPE = SavedFilesActivity.FILES_TYPE;
            switch (FILES_TYPE){
                case DBHelper.VIDEO_TYPE:
                    extensions = FilesExtensions.videosExtensions;
                    break;
                case DBHelper.PHOTO_TYPE:
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
        FolderListAdapter adapter = new FolderListAdapter(this, mViewModel.getFoldersList());
        binding.selectFolderRecyclerView.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/155;
        binding.selectFolderRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));

        // hiding progressbar when all tasks completes
        binding.progressBarSelectFolder.setVisibility(View.GONE);
    }

    private void showDetailOfPermission(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Storage Permission is necessary for getting files.");
        dialog.setPositiveButton("Allow", (dialog1, which) -> requestPermission());
        dialog.setNegativeButton("Go Back", (dialog1, which) -> SelectFolderActivity.this.finish());
        dialog.create().show();
    }

    private void requestPermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        main();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        showDetailOfPermission();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}