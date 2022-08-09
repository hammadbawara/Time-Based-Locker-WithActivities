package com.hz_apps.timebasedlocker.ui.photos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hz_apps.timebasedlocker.databinding.FragmentPhotosBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

public class PhotosFragment extends Fragment {

    private FragmentPhotosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PhotosViewModel photosViewModel =
                new ViewModelProvider(this).get(PhotosViewModel.class);

        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.button.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), SelectFolderActivity.class);
            intent.putExtra("Type_Of_Files", 1);
            startActivity(intent);
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}