package com.hz_apps.timebasedlocker.Dialogs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.databinding.FragmentBSDialogBinding;

import java.io.File;

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


//        binding..setOnClickListener(v -> {
//            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//            Uri uri = FileProvider.getUriForFile(requireActivity(), requireActivity().getPackageName() + ".fileprovider", file);
//            intentShareFile.setDataAndType(uri, "videos/*");
//            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
//            startActivity(Intent.createChooser(intentShareFile, "Share File"));
//            dismiss();
//        });
//
//        binding.extendDateBtnSavedFile.setOnClickListener(v -> {
//            DateAndTime dateTime = savedFile.getUnlockDateTime();
//            LocalDate date = dateTime.getDate();
//
//            datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
//                // changing date in database for that file
//                DBHelper dbHelper = DBHelper.getINSTANCE();
//                // creating date time object for saving date
//                DateAndTime dateAndTime = new DateAndTime(LocalDate.of(year, month+1, dayOfMonth), dateTime.getTime());
//                // TODO: Set the right way to get table name
//                dbHelper.updateFileDateAndTime(dateAndTime.toString(),savedFile.getId(), DBHelper.SAVED_VIDEO_TABLE);
//
//            },date.getYear(), date.getMonthValue()-1, date.getDayOfMonth());
//
//            datePickerDialog.getDatePicker().setMinDate(TimeUnit.DAYS.toMillis(date.toEpochDay()));
//            datePickerDialog.show();
//            dismiss();
//        });

//        binding.deleteBtnSavedFolder.setOnClickListener(v -> {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
//            CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(getLayoutInflater());
//            dialog.setView(dialogBinding.getRoot());
//
//            dialogBinding.fileNameTextView.setText(savedFile.getName());
//
//            dialog.setPositiveButton("Ok", (dialog1, which) -> {
//                boolean isFileDeleted = file.delete();
//                if (isFileDeleted) {
//                    // TODO: HERE TABLE NAME IS ALSO NOT SET
//                    DBHelper.getINSTANCE().deleteFileFromDB(DBHelper.SAVED_VIDEO_TABLE, savedFile.getId());
//                    Toast.makeText(binding.getRoot().getContext(), "File Deleted Successfully", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(binding.getRoot().getContext(), "File not deleted. Something went wrong.", Toast.LENGTH_SHORT).show();
//                }
//            });
//            dialog.setNegativeButton("Cancel", (dialog12, which) -> {
//
//            });
//            dialog.show();
//
//            dismiss();
//        });

        return binding.getRoot();


    }
}