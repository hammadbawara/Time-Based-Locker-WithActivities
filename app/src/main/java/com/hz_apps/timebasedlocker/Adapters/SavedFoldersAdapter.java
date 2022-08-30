package com.hz_apps.timebasedlocker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.hz_apps.timebasedlocker.Datebase.SavedFolder;
import com.hz_apps.timebasedlocker.Dialogs.BSDialogFragment;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;

import java.util.List;

public class SavedFoldersAdapter extends RecyclerView.Adapter<SavedFoldersAdapter.MyViewHolder> {
    private final Context context;
    private final List<SavedFolder> savedFolderList;
    private final int FILES_TYPE;

    public SavedFoldersAdapter(Context context, List<SavedFolder> savedFolderList, int FILES_TYPE) {
        this.context = context;
        this.savedFolderList = savedFolderList;
        this.FILES_TYPE = FILES_TYPE;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_view, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SavedFolder folder = savedFolderList.get(position);
        holder.folder_name.setText(folder.getName());

        holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_folder));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SavedFilesActivity.class);
            intent.putExtra("saved_folder", folder);
            intent.putExtra("FILES_TYPE", FILES_TYPE);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("folder", folder);
                bundle.putSerializable("FILES_TYPE", FILES_TYPE);
                Navigation.findNavController(v).navigate(R.id.BSDialog, bundle);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedFolderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView imageView;
        private final TextView folder_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.folder_image_view);
            folder_name = itemView.findViewById(R.id.folder_name_text_view);
        }
    }
}
