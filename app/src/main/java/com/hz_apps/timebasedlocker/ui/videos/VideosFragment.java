package com.hz_apps.timebasedlocker.ui.videos;

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

import com.hz_apps.timebasedlocker.Adapters.SavedVideosAdapter;
import com.hz_apps.timebasedlocker.Datebase.DBRepository;
import com.hz_apps.timebasedlocker.Datebase.SavedVideo;
import com.hz_apps.timebasedlocker.databinding.FragmentVideosBinding;
import com.hz_apps.timebasedlocker.ui.selectfolder.SelectFolderActivity;

import java.util.List;

public class VideosFragment extends Fragment {

    private FragmentVideosBinding binding;

    DBRepository db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VideosViewModel viewModel =
                new ViewModelProvider(this).get(VideosViewModel.class);

        binding = FragmentVideosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBRepository(requireActivity().getApplication());

        viewModel.setSavedVideoList(db.getAllSavedVideos(0));



        SavedVideosAdapter adapter = new SavedVideosAdapter(getContext());
        binding.recyclerviewSavedVideos.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels/displayMetrics.density)/130;
        binding.recyclerviewSavedVideos.setLayoutManager(new GridLayoutManager(getContext(), numberOfImagesInOneRow));



        viewModel.getSavedVideoList().observe(getViewLifecycleOwner(), new Observer<List<SavedVideo>>() {
            @Override
            public void onChanged(List<SavedVideo> savedVideos) {
                adapter.setSavedVideoList(savedVideos);
                binding.recyclerviewSavedVideos.setAdapter(adapter);
            }
        });


        binding.addVideosBtn.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), SelectFolderActivity.class);
            intent.putExtra("Type_Of_Files", 0);
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