package com.hz_apps.timebasedlocker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hz_apps.timebasedlocker.databinding.DialogConfirmationLockFileBinding;

public class ConfirmationLockFileDialog extends DialogFragment {
    DialogConfirmationLockFileBinding binding;
    public static ConfirmationLockFileDialogData listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogConfirmationLockFileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setView(view);

        dialog.setPositiveButton("Yes", (dialog1, which) -> {
            boolean allowExtendDate = binding.allowExtendDateChB.isChecked();
            boolean allowSeeTitle = binding.allowSeeTitleChB.isChecked();
            boolean allowSeeImage = binding.allowSeeImageChB.isChecked();
            listener.lockFiles(allowExtendDate, allowSeeTitle, allowSeeImage);
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return dialog.create();
    }

    public interface ConfirmationLockFileDialogData{
        void lockFiles(boolean allowExtendDate, boolean allowSeeTitle, boolean allowSeeImage);
    }
}