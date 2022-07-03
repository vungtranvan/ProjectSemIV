package com.example.projectsemiv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projectsemiv.R;

public class AddQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.add_question);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
    }
}