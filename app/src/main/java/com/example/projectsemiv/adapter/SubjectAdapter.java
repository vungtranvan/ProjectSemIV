package com.example.projectsemiv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.HistoryVm;

import java.util.List;

public class SubjectAdapter extends ArrayAdapter<HistoryVm> {
    private List<HistoryVm> mListHistory;

    public SubjectAdapter(Context context, List<HistoryVm> objects) {
        super(context, R.layout.item_history_list, objects);
        mListHistory = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
        }
        HistoryVm historyVm = mListHistory.get(position);

        TextView tvNumExam = convertView.findViewById(R.id.tvNumExam);
        TextView tvTotalMarkHistory = convertView.findViewById(R.id.tvTotalMarkHistory);
        ImageView imgIcon = convertView.findViewById(R.id.imgIcon);

        tvNumExam.setText(getContext().getResources().getString(R.string.exam_no) + " " + (position + 1));
        imgIcon.setImageResource(R.drawable.subject);
        if (!historyVm.isStatus()) {
            tvNumExam.setPadding(10, 28, 0, 0);
            tvTotalMarkHistory.setVisibility(View.GONE);
        } else {
            tvNumExam.setPadding(10, 0, 0, 0);
            tvTotalMarkHistory.setVisibility(View.VISIBLE);
            tvTotalMarkHistory.setText(historyVm.getCorrectMark() + "/" + historyVm.getTotalMark());
        }
        return convertView;
    }
}
