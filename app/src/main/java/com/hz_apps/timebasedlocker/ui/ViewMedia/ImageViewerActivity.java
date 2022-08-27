package com.hz_apps.timebasedlocker.ui.ViewMedia;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hz_apps.timebasedlocker.databinding.ActivityImageViewerBinding;

public class ImageViewerActivity extends AppCompatActivity {
    ActivityImageViewerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String path = getIntent().getStringExtra("image_path");

        binding.photoView.setImageURI(Uri.parse(path));
    }
}