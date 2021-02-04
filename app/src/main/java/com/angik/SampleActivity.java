package com.angik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.angik.dianahost.R;
import com.angik.dianahost.databinding.ActivitySampleBinding;

public class SampleActivity extends AppCompatActivity {

    private ActivitySampleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySampleBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }
}