package com.hz_apps.timebasedlocker.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.databinding.DialogGetInputBinding;

public class GetInputDialogFragment extends DialogFragment {
    DialogGetInputBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogGetInputBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        assert getArguments() != null;
        int FILES_TYPE = getArguments().getInt("FILES_TYPE");

        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setView(view);
        TextView textView = binding.getInputEditText;
        textView.setHint("Folder Name");

        dialog.setPositiveButton("Create", (dialog1, which) -> {

            String text = binding.getInputEditText.getText().toString();

            if (text.equals("")){
                binding.textView2.setText("Folder name is not valid");
            }else{
                DBHelper.getINSTANCE().create_folder(text, FILES_TYPE);
            }


        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });


        return dialog.create();
    }
}
