package com.hz_apps.timebasedlocker.ui.ViewMedia;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.databinding.ActivityMediaViewerBinding;

public class MediaViewerActivity extends AppCompatActivity {

    ActivityMediaViewerBinding binding;
    private SavedFile savedFile;
    private boolean isOptionLayoutVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMediaViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        savedFile = (SavedFile) getIntent().getSerializableExtra("saved_file");

        switch (savedFile.getFileType()){
            case DBHelper.VIDEO_TYPE:
                showVideo();
                break;
            case DBHelper.PHOTO_TYPE:
                showPhoto();
                break;

        }
    }

    View.OnClickListener mediaClickListener = v -> {
      if (isOptionLayoutVisible){
          binding.optionsLayoutMediaViewer.setVisibility(View.INVISIBLE);
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
}