package com.hz_apps.timebasedlocker.Dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.Datebase.SavedFolder;
import com.hz_apps.timebasedlocker.databinding.FragmentBSDialogBinding;

import java.io.File;

public class BSDialogFragment extends BottomSheetDialogFragment {
    FragmentBSDialogBinding binding;
    int FILES_TYPE;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBSDialogBinding.inflate(getLayoutInflater());

        assert getArguments() != null;
        SavedFolder savedFolder = (SavedFolder) getArguments().getSerializable("folder");
        FILES_TYPE = getArguments().getInt("FILES_TYPE", -1);

        binding.deleteBtnSavedFolder.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("This folder and files will be deleted permanently.");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DBHelper.getINSTANCE().deleteFolder(savedFolder, FILES_TYPE);
                    dismiss();
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
            dialog.show();
        });


        return binding.getRoot();


    }
}