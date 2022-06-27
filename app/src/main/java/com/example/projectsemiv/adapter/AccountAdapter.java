package com.example.projectsemiv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectsemiv.R;
import com.example.projectsemiv.activity.AddAccountActivity;
import com.example.projectsemiv.entity.Account;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountAdapter extends ArrayAdapter<Account> {

    private List<Account> mListAccount;
    private Context mCtx;

    public AccountAdapter(Context context, List<Account> objects) {
        super(context, R.layout.item_account_list, objects);
        mCtx = context;
        mListAccount = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.mCtx).inflate(R.layout.item_account_list, null);
        }

        Account account = mListAccount.get(position);
        TextView tvNameAcc = v.findViewById(R.id.tvNameAcc);
        TextView tvUserNameAcc = v.findViewById(R.id.tvUserNameAcc);
        TextView tvTypeAcc = v.findViewById(R.id.tvTypeAcc);
        TextView tvEmailAcc = v.findViewById(R.id.tvEmailAcc);
        TextView tvSexAcc = v.findViewById(R.id.tvSexAcc);
        TextView tvAddressAcc = v.findViewById(R.id.tvAddressAcc);

        CircleImageView imgAccountView = v.findViewById(R.id.imgAccountView);
        if (account.getImage() != null) {
            Glide.with(mCtx).load(account.getImage()).into(imgAccountView);
        } else {
            imgAccountView.setImageResource(R.mipmap.ic_launcher);
        }

        tvNameAcc.setText(account.getName());
        tvUserNameAcc.setText(account.getUserName());
        tvEmailAcc.setText(account.getEmail());
        tvAddressAcc.setText(account.getAddress());
        tvSexAcc.setText(account.isSex() ? mCtx.getResources().getString(R.string.male) : mCtx.getResources().getString(R.string.female));
        tvTypeAcc.setText(account.isIsAdmin() ? mCtx.getResources().getString(R.string.acc_type_admin) : mCtx.getResources().getString(R.string.acc_type_member));
        return v;
    }
}
