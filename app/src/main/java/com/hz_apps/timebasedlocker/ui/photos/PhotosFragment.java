package com.hz_apps.timebasedlocker.ui.photos;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hz_apps.timebasedlocker.Adapters.SavedFoldersAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.FragmentPhotosBinding;

import java.util.concurrent.Executors;

public class PhotosFragment extends Fragment {

    DBHelper db;
    PhotosViewModel viewModel;
    private FragmentPhotosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav_main_activity);
        if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);

        if (viewModel.getSavedFolderList().size() == 0) {
            fetchDataFromDB();
        } else {
            setDataInRV();
        }


        binding.addFolderPhotosFragment.setOnClickListener((v) -> {

        });

        binding.swipeRefreshPhotosFragment.setOnRefreshListener(this::fetchDataFromDB);

        return root;
    }

    // This method fetch data from database and also set that data in recycler view
    private void fetchDataFromDB() {
        // Showing progressBar before fetching data from Database and setting into recycler view
        binding.progressBarPhotosFragment.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {

            db = DBHelper.getINSTANCE();
            viewModel.setSavedFolderList(db.getSavedFolders(DBHelper.PHOTO_TYPE));

            requireActivity().runOnUiThread(this::setDataInRV);
        });
    }

    // This function set data in Recycler View
    private void setDataInRV() {
        SavedFoldersAdapter adapter = new SavedFoldersAdapter(requireContext(), viewModel.getSavedFolderList(), DBHelper.PHOTO_TYPE);
        binding.recyclerviewPhotoFragment.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels / displayMetrics.density) / 155;
        binding.recyclerviewPhotoFragment.setLayoutManager(new GridLayoutManager(getContext(), numberOfImagesInOneRow));
        binding.recyclerviewPhotoFragment.setAdapter(adapter);

        // Hiding Progress Bar after showing data in recycler view
        binding.progressBarPhotosFragment.setVisibility(View.GONE);

        if (binding.swipeRefreshPhotosFragment.isRefreshing())
            binding.swipeRefreshPhotosFragment.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        if (DBHelper.isAnyChangeInFolders) {
            fetchDataFromDB();
            DBHelper.isAnyChangeInFolders = false;
        }
        super.onResume();
    }
}