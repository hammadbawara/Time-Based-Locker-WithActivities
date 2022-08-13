package com.hz_apps.timebasedlocker.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.databinding.FragmentBSDialogBinding;

public class BSDialogFragment extends BottomSheetDialogFragment {
    FragmentBSDialogBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBSDialogBinding.inflate(getLayoutInflater());

        SavedFile file = (SavedFile) getArguments().getSerializable("file");
        return binding.getRoot();
    }
}