package com.hz_apps.timebasedlocker.ui.others;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.FragmentOthersBinding;

public class OthersFragment extends Fragment {

    private FragmentOthersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OthersViewModel othersViewModel =
                new ViewModelProvider(this).get(OthersViewModel.class);

        binding = FragmentOthersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav_main_activity);
        if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);

        final TextView textView = binding.textNotifications;


        othersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}