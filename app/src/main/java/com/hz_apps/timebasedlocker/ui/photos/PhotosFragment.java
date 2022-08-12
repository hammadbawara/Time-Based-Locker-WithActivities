package com.hz_apps.timebasedlocker.ui.photos;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.SavedPhotosAdapter;
import com.hz_apps.timebasedlocker.Datebase.DatabaseHelper;
import com.hz_apps.timebasedlocker.databinding.FragmentPhotosBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

import java.util.concurrent.Executors;

public class PhotosFragment extends Fragment {

    private FragmentPhotosBinding binding;
    DatabaseHelper db;
    PhotosViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fetchDataFromDB();


        binding.floatingActionButton.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), SelectFolderActivity.class);
            intent.putExtra("Type_Of_Files", 1);
            startActivity(intent);
        });

        binding.swipeRefreshPhotosFragment.setOnRefreshListener(this::fetchDataFromDB);

        return root;
    }

    // This method fetch data from database and also set that data in recycler view
    private void fetchDataFromDB(){
        // Showing progressBar before fetching data from Database and setting into recycler view
        binding.progressBarPhotosFragment.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {

            db = DatabaseHelper.getINSTANCE(requireActivity().getApplication());
            viewModel.setSavedFileList(db.getSavedFiles(DatabaseHelper.SAVED_PHOTO_TABLE));

            requireActivity().runOnUiThread(this::setDataInRecyclerView);
        });
    }

    private void setDataInRecyclerView(){
        SavedPhotosAdapter adapter = new SavedPhotosAdapter(getContext());
        binding.recyclerviewPhotoFragment.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/130;
        binding.recyclerviewPhotoFragment.setLayoutManager(new GridLayoutManager(getContext(), numberOfImagesInOneRow));
        adapter.setSavedPhotoList(viewModel.getSavedFileList());
        binding.recyclerviewPhotoFragment.setAdapter(adapter);

        // Hiding Progress Bar after showing data in recycler view
        binding.progressBarPhotosFragment.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        fetchDataFromDB();
        super.onResume();
    }
}