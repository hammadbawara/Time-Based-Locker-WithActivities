package com.hz_apps.timebasedlocker.ui.videos;

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
import com.hz_apps.timebasedlocker.Database.DBHelper;
import com.hz_apps.timebasedlocker.Dialogs.GetInputDialogFragment;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.FragmentVideosBinding;

import java.util.concurrent.Executors;

public class VideosFragment extends Fragment implements DBHelper.DBChangeListener {

    DBHelper db;
    VideosViewModel viewModel;
    SavedFoldersAdapter adapter;
    private FragmentVideosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(VideosViewModel.class);

        binding = FragmentVideosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav_main_activity);
        if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);

        DBHelper.getINSTANCE().setDBChangeListener(this);

        if (viewModel.getSavedFolderList().size() == 0) {
            fetchDataFromDB();
        } else {
            setDataInRV();
        }

        binding.addFolderVideosFragment.setOnClickListener(view -> {

        });

        binding.swipeRefreshVideosFragment.setOnRefreshListener(this::fetchDataFromDB);

        binding.addFolderVideosFragment.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("FILES_TYPE", DBHelper.VIDEO_TYPE);
            GetInputDialogFragment dialogFragment = new GetInputDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getChildFragmentManager(), "GET");
        });

        return root;
    }

    private void fetchDataFromDB() {

        binding.progressBarVideosFragment.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            db = DBHelper.getINSTANCE();
            viewModel.setSavedFolderList(db.getSavedFolders(DBHelper.VIDEO_TYPE));

            requireActivity().runOnUiThread(this::setDataInRV);
        });

    }

    // This function set data in Recycler View
    private void setDataInRV() {
        adapter = new SavedFoldersAdapter(requireContext(), viewModel.getSavedFolderList(), DBHelper.VIDEO_TYPE);
        binding.recyclerviewSavedVideos.setAdapter(adapter);
        // Items show in one row
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int numberOfImagesInOneRow = (int) (displayMetrics.widthPixels / displayMetrics.density) / 155;
        binding.recyclerviewSavedVideos.setLayoutManager(new GridLayoutManager(getContext(), numberOfImagesInOneRow));
        binding.recyclerviewSavedVideos.setAdapter(adapter);

        binding.progressBarVideosFragment.setVisibility(View.GONE);

        if (binding.swipeRefreshVideosFragment.isRefreshing())
            binding.swipeRefreshVideosFragment.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onFolderChange() {
        fetchDataFromDB();
        setDataInRV();
    }
}