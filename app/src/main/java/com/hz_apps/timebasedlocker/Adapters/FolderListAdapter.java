package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.selectfolder.Folder;
import com.hz_apps.timebasedlocker.ui.selectfolder.selectfiles.SelectFilesActivity;

import java.util.ArrayList;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.myViewHolder> {

    private final Context context;
    private final ArrayList<Folder> foldersList;
    AlertDialog.Builder alertDialog;

    public FolderListAdapter(Context context, ArrayList<Folder> foldersList) {
        this.context = context;
        this.foldersList = foldersList;
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
        Glide.with(context).load(foldersList.get(position).getFirstImage()).into(holder.imageView);
        holder.folder_name.setText(folder.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SelectFilesActivity.class);
            intent.putExtra("folder", folder);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertDialog = new AlertDialog.Builder(context).setMessage(folder.getAbsolutePath())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.create().show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return foldersList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imageView;
        private TextView folder_name;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.folder_image_view);
            folder_name = itemView.findViewById(R.id.folder_name_text_view);
        }
    }
}
