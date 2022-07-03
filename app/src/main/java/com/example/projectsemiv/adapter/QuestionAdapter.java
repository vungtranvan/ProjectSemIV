package com.example.projectsemiv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.QuestionVm;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<QuestionVm> {
    private List<QuestionVm> mListQuestion;
    private Context mCtx;

    public QuestionAdapter(Context context, List<QuestionVm> objects) {
        super(context, R.layout.item_question_list, objects);
        mCtx = context;
        mListQuestion = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.mCtx).inflate(R.layout.item_question_list, null);
        }

        QuestionVm questionVm = mListQuestion.get(position);
        TextView tvNameQuestion = v.findViewById(R.id.tvNameQuestion);
        TextView tvCategoryExamName = v.findViewById(R.id.tvCategoryExamName);

        tvNameQuestion.setText(questionVm.getName());
        tvCategoryExamName.setText(questionVm.getCategoryExamName());
        return v;
    }
}
