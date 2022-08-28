package com.hz_apps.timebasedlocker.ui.ViewMedia;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.databinding.ActivityMediaViewerBinding;
import com.hz_apps.timebasedlocker.databinding.CustomAlertDialogBinding;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;

import java.io.File;

public class MediaViewerActivity extends AppCompatActivity {

    ActivityMediaViewerBinding binding;
    private SavedFile savedFile;
    private boolean isOptionLayoutVisible = false;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMediaViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        savedFile = (SavedFile) getIntent().getSerializableExtra("saved_file");
        index = getIntent().getIntExtra("file_index", -1);

        switch (savedFile.getFileType()){
            case DBHelper.VIDEO_TYPE:
                showVideo();
                break;
            case DBHelper.PHOTO_TYPE:
                showPhoto();
                break;

        }

        binding.deleteFileMediaViewer.setOnClickListener(v -> showDeleteDialog());
    }

    View.OnClickListener mediaClickListener = v -> {
      if (isOptionLayoutVisible){
          binding.optionsLayoutMediaViewer.setVisibility(View.INVISIBLE);
          // Setting in full screen
          getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                  WindowManager.LayoutParams.FLAG_FULLSCREEN);
          isOptionLayoutVisible = false;
      }else{
          binding.optionsLayoutMediaViewer.setVisibility(View.VISIBLE);
          isOptionLayoutVisible = true;
      }
    };

    private void showVideo(){
        VideoView videoView = binding.videoViewMediaViewer;
        videoView.setVisibility(View.VISIBLE);
        videoView.setOnClickListener(mediaClickListener);

        videoView.setVideoPath(savedFile.getPath());

        videoView.start();

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        mediaController.setAnchorView(videoView);
    }

    private void showPhoto(){
        PhotoView photoView = binding.photoViewMediaViewer;
        photoView.setVisibility(View.VISIBLE);
        photoView.setOnClickListener(mediaClickListener);

        photoView.setImageURI(Uri.parse(savedFile.getPath()));
    }

    private void showDeleteDialog(){
        CustomAlertDialogBinding binding = CustomAlertDialogBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(binding.getRoot());


        binding.fileNameTextView.setText(savedFile.getName());

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {

        });
        dialog.setPositiveButton("Ok", (dialog12, which) -> {
            DBHelper db = DBHelper.getINSTANCE();
            db.deleteFileFromDB(SavedFilesActivity.FILES_TABLE_NAME, savedFile.getId());
            boolean isFileDeleted = new File(savedFile.getPath()).delete();
            if (isFileDeleted){
                Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show();
                SavedFilesActivity.deletedFileIndex = index;
                this.finish();
            }
            else{
                Toast.makeText(this, "File not deleted. Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.create().show();
    }
}