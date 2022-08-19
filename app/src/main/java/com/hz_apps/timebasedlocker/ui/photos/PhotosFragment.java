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

import com.hz_apps.timebasedlocker.Adapters.SavedFilesAdapter;
import com.hz_apps.timebasedlocker.Adapters.SavedFoldersAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.databinding.FragmentPhotosBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

import java.util.concurrent.Executors;

public class PhotosFragment extends Fragment {

    private FragmentPhotosBinding binding;
    DBHelper db;
    PhotosViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (viewModel.getSavedFolderList().size() == 0){
            fetchDataFromDB();
        }else{
            setDataInRV();
        }


        binding.floatingActionButton.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), SelectFolderActivity.class);
            intent.putExtra("Type_Of_Files", DBHelper.TYPE_PHOTO);
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

            db = DBHelper.getINSTANCE();
            viewModel.setSavedFolderList(db.getSavedFolders(DBHelper.TYPE_PHOTO));

            requireActivity().runOnUiThread(this::setDataInRV);
        });
    }
    // This function set data in Recycler View
    private void setDataInRV(){
        SavedFoldersAdapter adapter = new SavedFoldersAdapter(requireContext(), viewModel.getSavedFolderList());
        binding.recyclerviewPhotoFragment.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/130;
        binding.recyclerviewPhotoFragment.setLayoutManager(new GridLayoutManager(getContext(), numberOfImagesInOneRow));
        binding.recyclerviewPhotoFragment.setAdapter(adapter);

        // Hiding Progress Bar after showing data in recycler view
        binding.progressBarPhotosFragment.setVisibility(View.GONE);

        if (binding.swipeRefreshPhotosFragment.isRefreshing()) binding.swipeRefreshPhotosFragment.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        if (DBHelper.isAnyChangeInFolders){
            fetchDataFromDB();
            DBHelper.isAnyChangeInFolders = false;
        }
        super.onResume();
    }
}