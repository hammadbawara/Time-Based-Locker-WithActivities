package com.hz_apps.timebasedlocker.ui.videos;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.hz_apps.timebasedlocker.Adapters.SavedFilesAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.MainActivity;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.ActivityMainBinding;
import com.hz_apps.timebasedlocker.databinding.FragmentVideosBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

import java.util.concurrent.Executors;

public class VideosFragment extends Fragment {

    private FragmentVideosBinding binding;
    DBHelper db;
    VideosViewModel viewModel;
    SavedFilesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(VideosViewModel.class);

        binding = FragmentVideosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // creating database instance for all over application
        DBHelper.createInstanceForOverApplication(requireActivity().getApplication());

        if (viewModel.getSavedVideosList().size() == 0){
            fetchDataFromDB();
        }else{
            setDataInRV();
        }

        binding.addVideosBtn.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), SelectFolderActivity.class);
            intent.putExtra("Type_Of_Files", 0);
            startActivity(intent);
        });

        binding.swipeRefreshVideosFragment.setOnRefreshListener(this::fetchDataFromDB);

        return root;
    }

    private void fetchDataFromDB(){

        binding.progressBarVideosFragment.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            System.out.println("Fetched");
            db = DBHelper.getINSTANCE();
            viewModel.setSavedVideosList(db.getSavedFiles(DBHelper.SAVED_VIDEO_TABLE));

            requireActivity().runOnUiThread(this::setDataInRV);
        });

    }
    // This function set data in Recycler View
    private void setDataInRV(){
        adapter = new SavedFilesAdapter(requireContext(), viewModel.getSavedVideosList(), binding.toolbarVideosFragment);
        binding.recyclerviewSavedVideos.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/130;
        binding.recyclerviewSavedVideos.setLayoutManager(new GridLayoutManager(getContext(), numberOfImagesInOneRow));
        binding.recyclerviewSavedVideos.setAdapter(adapter);

        binding.progressBarVideosFragment.setVisibility(View.GONE);

        if (binding.swipeRefreshVideosFragment.isRefreshing()) binding.swipeRefreshVideosFragment.setRefreshing(false);
    }

    @Override
    public void onResume() {
        if (DBHelper.isAnyFileInserted){
            fetchDataFromDB();
            DBHelper.isAnyFileInserted = false;
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}