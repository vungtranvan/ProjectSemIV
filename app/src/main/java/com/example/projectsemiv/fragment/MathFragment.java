package com.example.projectsemiv.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.projectsemiv.MainActivity;
import com.example.projectsemiv.R;
import com.example.projectsemiv.adapter.CategoryExamAdapter;
import com.example.projectsemiv.entity.CategoryExam;
import com.example.projectsemiv.entity.FakeData;
import com.example.projectsemiv.slide.ScreenSlideActivity;

import java.util.ArrayList;
import java.util.List;


public class MathFragment extends Fragment {

    CategoryExamAdapter examAdapter;
    GridView gvExam;
    List<CategoryExam> arr_exam = new ArrayList<CategoryExam>();

    public MathFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Toán Học");
        return inflater.inflate(R.layout.fragment_math, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gvExam = (GridView) getActivity().findViewById(R.id.gvExam);

        // fake data
        arr_exam = new FakeData().getExamFake();

        examAdapter = new CategoryExamAdapter(getActivity(), arr_exam);
        gvExam.setAdapter(examAdapter);
        gvExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ScreenSlideActivity.class);
                intent.putExtra("categoryExamId", i + 1);
                intent.putExtra("subject", "math");
                intent.putExtra("test", "yes");
                startActivity(intent);
            }
        });

    }
}