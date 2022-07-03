package com.example.projectsemiv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectsemiv.MainActivity;
import com.example.projectsemiv.R;


public class MathFragment extends Fragment {

    public MathFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.manager_question_nav);
        return inflater.inflate(R.layout.fragment_math, container, false);
    }


}