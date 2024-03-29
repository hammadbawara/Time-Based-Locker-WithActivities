package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.selectfolder.Folder;

import java.util.ArrayList;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.myViewHolder> {

    private final Context context;
    private final ArrayList<Folder> foldersList;
    private final FoldersListListener listeners;

    public FolderListAdapter(Context context, ArrayList<Folder> foldersList, FoldersListListener listeners) {
        this.context = context;
        this.foldersList = foldersList;
        this.listeners = listeners;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Folder folder = foldersList.get(position);
        Glide.with(context).load(folder.getFirstImage())
                .into(holder.imageView);
        holder.folder_name.setText(folder.getName());
    }

    @Override
    public int getItemCount() {
        return foldersList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ShapeableImageView imageView;
        private final TextView folder_name;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.folder_image_view);
            folder_name = itemView.findViewById(R.id.folder_name_text_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listeners.onClick(getBindingAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listeners.onLongClick(getBindingAdapterPosition());
            return true;
        }
    }

    public interface FoldersListListener {
        void onClick(int position);
        void onLongClick(int position);
    }
}
