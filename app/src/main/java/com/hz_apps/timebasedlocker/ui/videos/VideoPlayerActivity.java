package com.hz_apps.timebasedlocker.ui.videos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.ActivityVideoPlayerBinding;

import java.io.File;

public class VideoPlayerActivity extends AppCompatActivity {

    ActivityVideoPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        String path = getIntent().getStringExtra("video_path");

        VideoView videoView = binding.videoView;

        videoView.setVideoPath(path);

        videoView.start();

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        mediaController.setAnchorView(videoView);
    }
}