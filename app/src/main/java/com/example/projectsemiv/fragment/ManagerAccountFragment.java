package com.example.projectsemiv.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projectsemiv.LoginActivity;
import com.example.projectsemiv.MainActivity;
import com.example.projectsemiv.R;
import com.example.projectsemiv.RegisterActivity;
import com.example.projectsemiv.activity.AddAccountActivity;
import com.example.projectsemiv.adapter.AccountAdapter;
import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerAccountFragment extends Fragment {

    private List<Account> mListAccount;
    private ProgressDialog mProgressDialog;

    public ManagerAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.manager_account_nav);
        return inflater.inflate(R.layout.fragment_manager_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        getListAccount(null);

        Button btnAddNewAccount = getActivity().findViewById(R.id.btnAddNewAccount);
        btnAddNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.sub_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Account account = mListAccount.get(info.position);
        switch (item.getItemId()) {
            case R.id.menuUpdate:
                Toast.makeText(getContext(), "Edit " + account.getName(), Toast.LENGTH_LONG).show();
                break;
            case R.id.menuDelete:
                dialogConfirm(account.getId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void dialogConfirm(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.notification);
        builder.setMessage(R.string.notification_confirm_delete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount(id);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void deleteAccount(int id) {
        if (id == 1) {
            Toast.makeText(getContext(), getResources().getString(R.string.delete_acc_admin_error), Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog.show();
        ApiService.apiService.deleteAccountById(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(getContext(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    getListAccount(null);
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListAccount(String keyword) {
        mProgressDialog.show();
        ApiService.apiService.getAllAccount(keyword).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                mListAccount = response.body();

                if (mListAccount == null) {
                    mListAccount = new ArrayList<>();
                }
                ListView listView = getActivity().findViewById(R.id.listViewAccount);
                AccountAdapter adapter = new AccountAdapter(getContext(), mListAccount);
                listView.setAdapter(adapter);
                registerForContextMenu(listView);
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getListAccount(null);
    }
}