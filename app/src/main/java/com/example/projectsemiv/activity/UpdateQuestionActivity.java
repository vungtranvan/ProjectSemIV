package com.example.projectsemiv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projectsemiv.R;

public class UpdateQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.edit_question);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_question);
    }
}