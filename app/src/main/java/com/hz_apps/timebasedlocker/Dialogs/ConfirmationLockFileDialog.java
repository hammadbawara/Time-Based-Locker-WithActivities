package com.hz_apps.timebasedlocker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.DialogConfirmationLockFileBinding;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;

public class ConfirmationLockFileDialog extends DialogFragment {
    DialogConfirmationLockFileBinding binding;
    public static ConfirmationLockFileDialogData listener;
    boolean isShowAdvancedOptionShowing = false;
    int FILES_TYPE;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogConfirmationLockFileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        TextView showAdvancedOptions = binding.showAdvancedOptionsDiloagLockFile;
        ConstraintLayout advancedOptionsLayout = binding.advancedOptionsLayoutDialogLockFile;

        FILES_TYPE = SavedFilesActivity.FILES_TYPE;

        if (FILES_TYPE == DBHelper.VIDEO_TYPE){
            binding.allowSeeImageChB.setVisibility(View.VISIBLE);
        }

        showAdvancedOptions.setOnClickListener(v -> {
            if (isShowAdvancedOptionShowing){
                advancedOptionsLayout.setVisibility(View.GONE);
                showAdvancedOptions.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_expand_less), null, null, null);
                isShowAdvancedOptionShowing = false;
            }else{
                advancedOptionsLayout.setVisibility(View.VISIBLE);
                showAdvancedOptions.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_expand_more), null, null, null);
                isShowAdvancedOptionShowing = true;
            }
        });

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