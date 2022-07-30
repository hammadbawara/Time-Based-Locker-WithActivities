package com.hz_apps.timebasedlocker.ui.selectfolder.selectfiles;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.FilesListAdapter;
import com.hz_apps.timebasedlocker.databinding.ActivitySelectFilesBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.FilesExtensions;
import com.hz_apps.timebasedlocker.ui.selectfolder.Folder;

import java.io.File;

public class SelectFilesActivity extends AppCompatActivity {

    private ActivitySelectFilesBinding binding;
    private SelectFilesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(SelectFilesViewModel.class);

        if (mViewModel.getFilesList().size() == 0) getFiles();



        FilesListAdapter adapter = new FilesListAdapter(this, mViewModel.getFilesList());
        binding.selectFilesRecyclerView.setAdapter(adapter);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/100;
        binding.selectFilesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));


    }

    private void getFiles(){
        Folder folder = (Folder) getIntent().getSerializableExtra("folder");
        String[] extensions = FilesExtensions.imagesExtensions;
        File[] filesList = folder.listFiles();
        for (File file : filesList){
            for (String extension : extensions){
                if (file.getName().endsWith(extension)){
                    mViewModel.addFileInFilesList(file);
                }
            }
        }
    }
}