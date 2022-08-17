package com.hz_apps.timebasedlocker.Dialogs;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.databinding.FragmentBSDialogBinding;
import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.io.File;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class BSDialogFragment extends BottomSheetDialogFragment {
    FragmentBSDialogBinding binding;
    DatePickerDialog datePickerDialog;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBSDialogBinding.inflate(getLayoutInflater());

        assert getArguments() != null;
        SavedFile savedFile = (SavedFile) getArguments().getSerializable("file");
        File file = new File(savedFile.getPath());


        binding.shareBtnSavedFile.setOnClickListener(v -> {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(requireActivity(), requireActivity().getPackageName() + ".fileprovider", file);
            intentShareFile.setDataAndType(uri, "videos/*");
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
            dismiss();
        });

        binding.extendDateBtnSavedFile.setOnClickListener(v -> {

            DateAndTime dateTime = savedFile.getUnlockDateTime();

            LocalDate date = dateTime.getDate();

            datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {

            },date.getYear(), date.getMonthValue()-1, date.getDayOfMonth());
            datePickerDialog.getDatePicker().setMinDate(TimeUnit.DAYS.toMillis(date.toEpochDay()));
            datePickerDialog.show();
            dismiss();
        });

        return binding.getRoot();


    }
}