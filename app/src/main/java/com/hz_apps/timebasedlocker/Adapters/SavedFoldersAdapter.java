package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.hz_apps.timebasedlocker.Datebase.SavedFolder;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;

import java.util.List;

public class SavedFoldersAdapter extends RecyclerView.Adapter<SavedFoldersAdapter.MyViewHolder> {
    private final Context context;
    private final List<SavedFolder> savedFolderList;

    public SavedFoldersAdapter(Context context, List<SavedFolder> savedFolderList) {
        this.context = context;
        this.savedFolderList = savedFolderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SavedFolder folder = savedFolderList.get(position);
        holder.folder_name.setText(folder.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SavedFilesActivity.class);
            intent.putExtra("saved_folder", folder);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return savedFolderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imageView;
        private TextView folder_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.folder_image_view);
            folder_name = itemView.findViewById(R.id.folder_name_text_view);
        }
    }
}
