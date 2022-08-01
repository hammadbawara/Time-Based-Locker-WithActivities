package com.hz_apps.timebasedlocker.ui.selectfolder.selectfiles;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

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

public class SelectFilesActivity extends AppCompatActivity {

    private ActivitySelectFilesBinding binding;
    private SelectFilesViewModel mViewModel;
    private RecyclerView recyclerView;
    FilesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(SelectFilesViewModel.class);

        if (mViewModel.getFilesList().size() == 0) getFiles();

        recyclerView = binding.selectFilesRecyclerView;
        adapter = new FilesListAdapter(this, mViewModel.getFilesList(), binding.nextBtn);
        recyclerView.setAdapter(adapter);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/100;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfImagesInOneRow));

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
            System.out.println("Selected");
            adapter.setAllItemsSelected(true);
            adapter.notifyDataSetChanged();
        }else if (item.getTitle() == "Unselect All"){
            System.out.println("Unselected");
        }
        return super.onOptionsItemSelected(item);
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