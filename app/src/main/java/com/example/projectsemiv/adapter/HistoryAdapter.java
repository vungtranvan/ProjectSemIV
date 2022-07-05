package com.example.projectsemiv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.HistoryVm;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryVm> {
    private List<HistoryVm> mListHistory;
    private Context mCtx;

    public HistoryAdapter(Context context, List<HistoryVm> objects) {
        super(context, R.layout.item_history_list, objects);
        mCtx = context;
        mListHistory = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.mCtx).inflate(R.layout.item_history_list, null);
        }

        HistoryVm historyVm = mListHistory.get(position);
        TextView tvCodeHistory = v.findViewById(R.id.tvCodeHistory);
        TextView tvCategoryExamNameHistory = v.findViewById(R.id.tvCategoryExamNameHistory);
        TextView tvNameAccCreateHistory = v.findViewById(R.id.tvNameAccCreateHistory);
        TextView tvStatusHistory = v.findViewById(R.id.tvStatusHistory);
        TextView tvMarkHistory = v.findViewById(R.id.tvMarkHistory);
        TextView title_tvMarkHistory = v.findViewById(R.id.title_tvMarkHistory);


        tvCodeHistory.setText(String.valueOf(historyVm.getId()));
        tvCategoryExamNameHistory.setText(historyVm.getCategoryExamName());
        tvNameAccCreateHistory.setText(historyVm.getAccountName());
        tvStatusHistory.setText(historyVm.isStatus() ? mCtx.getResources().getString(R.string.status_tested) : mCtx.getResources().getString(R.string.status_not_tested));
        if (historyVm.isStatus()) {
            tvMarkHistory.setText(historyVm.getCorrectMark() + " / " + historyVm.getTotalMark());
            title_tvMarkHistory.setVisibility(View.VISIBLE);
        } else {
            tvMarkHistory.setVisibility(View.GONE);
            title_tvMarkHistory.setVisibility(View.GONE);
        }

        return v;
    }
}
