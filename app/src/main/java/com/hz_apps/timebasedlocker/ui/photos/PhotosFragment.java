package com.hz_apps.timebasedlocker.ui.photos;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hz_apps.timebasedlocker.Adapters.SavedPhotosAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBRepository;
import com.hz_apps.timebasedlocker.Datebase.SavedPhoto;
import com.hz_apps.timebasedlocker.databinding.FragmentPhotosBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

import java.util.List;

public class PhotosFragment extends Fragment {

    private FragmentPhotosBinding binding;
    DBRepository db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PhotosViewModel viewModel =
                new ViewModelProvider(this).get(PhotosViewModel.class);

        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBRepository(requireActivity().getApplication());

        viewModel.setSavedPhotosList(db.getAllSavedPhotos(0));



        SavedPhotosAdapter adapter = new SavedPhotosAdapter(getContext());
        binding.recyclerviewPhotoFragment.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/130;
        binding.recyclerviewPhotoFragment.setLayoutManager(new GridLayoutManager(getContext(), numberOfImagesInOneRow));



        binding.floatingActionButton.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), SelectFolderActivity.class);
            intent.putExtra("Type_Of_Files", 1);
            startActivity(intent);
        });

        viewModel.getSavedPhotosList().observe(getViewLifecycleOwner(), new Observer<List<SavedPhoto>>() {
            @Override
            public void onChanged(List<SavedPhoto> savedPhotosList) {
                System.out.println("Set Saved Photos in RecyclerView " + savedPhotosList.size() );
                adapter.setSavedPhotoList(savedPhotosList);
                binding.recyclerviewPhotoFragment.setAdapter(adapter);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}