package com.hz_apps.timebasedlocker.ui.selectfolder.selectfiles;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hz_apps.timebasedlocker.Adapters.FilesListAdapter;
import com.hz_apps.timebasedlocker.databinding.ActivitySelectFilesBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.FilesExtensions;
import com.hz_apps.timebasedlocker.ui.selectfolder.Folder;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectFilesActivity extends AppCompatActivity {

    private ActivitySelectFilesBinding binding;
    private SelectFilesViewModel mViewModel;
    FilesListAdapter adapter;
    public static int TYPES_OF_FILES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        TYPES_OF_FILES = getIntent().getIntExtra("TypesOfFiles", -1);

        mViewModel = new ViewModelProvider(this).get(SelectFilesViewModel.class);
        runBackground();

    }

    private void runBackground(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // only fetch files when filesList size is zero. This make sure the activity won't fetch
            // files when files already been fetched.
            if (mViewModel.getFilesList().size() == 0) getFiles();
            // setting recyclerview in background thread
            this.runOnUiThread(this::setRecyclerView);
        });
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = binding.selectFilesRecyclerView;
        adapter = new FilesListAdapter(this, mViewModel.getFilesList(), binding.nextBtnActivitySelectFiles);
        recyclerView.setAdapter(adapter);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/120;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));

        // hiding progressbar after doing all tasks
        binding.progressBarSelectFiles.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.add("Select All");
        menu.add("Unselect All");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == "Select All"){
            if (!adapter.isAllItemsSelected()){
                adapter.setAllItemsUnSelectedFlag(false);
                adapter.setAllItemsSelected(true);
                for (int i=0; i<adapter.getFilesSelectedState().length; i++){
                    if (!adapter.getFilesSelectedState()[i]){
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        }else{
            if (!adapter.isAllItemsUnselected()){
                adapter.setAllItemsSelected(false);
                adapter.setAllItemsUnSelectedFlag(true);
                for (int i=0; i<adapter.getFilesSelectedState().length; i++){
                    if (adapter.getFilesSelectedState()[i]){
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFiles(){
        Folder folder = (Folder) getIntent().getSerializableExtra("folder");
        String[] extensions = new String[] {""};
        switch (TYPES_OF_FILES){
            case 0:
                extensions = FilesExtensions.videosExtensions;
                break;
            case 1:
                extensions = FilesExtensions.imagesExtensions;
                break;

        }

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