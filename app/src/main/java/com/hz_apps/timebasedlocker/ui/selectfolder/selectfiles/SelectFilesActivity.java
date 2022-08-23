package com.hz_apps.timebasedlocker.ui.selectfolder.selectfiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hz_apps.timebasedlocker.Adapters.FilesListAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.ActivitySelectFilesBinding;
import com.hz_apps.timebasedlocker.ui.LockFiles.LockFilesActivity;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;
import com.hz_apps.timebasedlocker.ui.selectfolder.FilesExtensions;
import com.hz_apps.timebasedlocker.ui.selectfolder.Folder;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectFilesActivity extends AppCompatActivity {

    public static int FILES_TYPE;
    FilesListAdapter adapter;
    private ActivitySelectFilesBinding binding;
    private SelectFilesViewModel mViewModel;
    Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarSelectFiles);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarSelectFiles.setNavigationOnClickListener((arrow) -> onBackPressed());
        setOptionMenu();
        ImageButton nextBtn = binding.nextBtnActivitySelectFiles;

        folder =  (Folder) getIntent().getSerializableExtra("folder");

        nextBtn.setOnClickListener(v -> {
            if (adapter.numberOfItemsSelected == 0) {
                Toast.makeText(SelectFilesActivity.this, "You have not selected any item", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SelectFilesActivity.this, LockFilesActivity.class);
                intent.putExtra("selected_files", adapter.getAllSelectedFiles());
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle(folder.getName());


        FILES_TYPE = SavedFilesActivity.FILES_TYPE;

        mViewModel = new ViewModelProvider(this).get(SelectFilesViewModel.class);
        runBackground();

    }

    private void runBackground() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // only fetch files when filesList size is zero. This make sure the activity won't fetch
            // files when files already been fetched.
            if (mViewModel.getFilesList().size() == 0) getFiles();
            // setting recyclerview in background thread
            adapter = new FilesListAdapter(this, mViewModel.getFilesList(), binding.nextBtnActivitySelectFiles);
            runOnUiThread(this::setRecyclerView);
        });
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = binding.selectFilesRecyclerView;
        recyclerView.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels / displayMetrics.density) / 120;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));

        // hiding progressbar after doing all tasks
        binding.progressBarSelectFiles.setVisibility(View.GONE);
    }

    private void setOptionMenu(){

        binding.toolbarSelectFiles.inflateMenu(R.menu.action_mode_saved_file);

        binding.toolbarSelectFiles.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.select_all_saved_file) {
                if (adapter.isAllItemsSelected()) {
                    adapter.setAllItemsUnselected();
                } else {
                    adapter.setAllItemsSelected();
                }
            }
            return false;
        });
    }

    private void getFiles() {
        String[] extensions = new String[]{""};
        switch (FILES_TYPE) {
            case DBHelper.VIDEO_TYPE:
                extensions = FilesExtensions.videosExtensions;
                break;
            case DBHelper.PHOTO_TYPE:
                extensions = FilesExtensions.imagesExtensions;
                break;

        }

        File[] filesList = folder.listFiles();
        for (File file : filesList) {
            for (String extension : extensions) {
                if (file.getName().endsWith(extension)) {
                    mViewModel.addFileInFilesList(file);
                }
            }
        }
    }
}