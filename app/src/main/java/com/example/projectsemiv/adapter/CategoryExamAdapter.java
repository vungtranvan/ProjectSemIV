package com.example.projectsemiv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.CategoryExam;

import java.util.List;

public class CategoryExamAdapter extends ArrayAdapter<CategoryExam> {
    public CategoryExamAdapter(Context context, List<CategoryExam> exam) {
        super(context, 0, exam);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvNumExam);
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);

        CategoryExam c = getItem(position);
        if (c != null) {
            tvName.setText("" + c.getName());
            imgIcon.setImageResource(R.drawable.subject);
        }

        return convertView;
    }
}
