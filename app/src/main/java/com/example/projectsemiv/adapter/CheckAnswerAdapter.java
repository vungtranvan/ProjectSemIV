package com.example.projectsemiv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.QuestionHistoryVm;

import java.util.List;

public class CheckAnswerAdapter extends BaseAdapter {
    List lsData;
    LayoutInflater inflater;
    Context mContext;

    public CheckAnswerAdapter(List lsData, Context context) {
        this.lsData = lsData;
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lsData.size();
    }

    @Override
    public Object getItem(int position) {
        return lsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionHistoryVm data = (QuestionHistoryVm) getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_gridview_list_answer, null);
            holder.tvNumAns = (TextView) convertView.findViewById(R.id.tvNumAns);
            holder.tvYourAns = (TextView) convertView.findViewById(R.id.tvAnswer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int i = position + 1;

        holder.tvNumAns.setText(mContext.getResources().getString(R.string.question_no) + " " + i + ": ");
        holder.tvYourAns.setText(data.getAnswerChoice());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvNumAns, tvYourAns;
    }
}
