package com.example.projectsemiv.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectsemiv.MainActivity;
import com.example.projectsemiv.R;
import com.example.projectsemiv.activity.AddQuestionActivity;
import com.example.projectsemiv.adapter.HistoryAdapter;
import com.example.projectsemiv.entity.HistoryVm;
import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private List<HistoryVm> mListHistory;
    private ProgressDialog mProgressDialog;
    private TextView noData;
    private SessionManager sessionManager;

    public HistoryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.history_nav);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        sessionManager = new SessionManager(getContext());
        noData = getActivity().findViewById(R.id.noDataHistory);
        getListHistory(null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_of_history, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search_history).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getListHistory(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getListHistory(null);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getListHistory(String keyword) {
        mProgressDialog.show();
        ApiService.apiService.getAllHistoryByAdmin(keyword).enqueue(new Callback<List<HistoryVm>>() {
            @Override
            public void onResponse(Call<List<HistoryVm>> call, Response<List<HistoryVm>> response) {
                mListHistory = response.body();

                if (mListHistory == null || mListHistory.size() == 0) {
                    mListHistory = new ArrayList<>();
                    noData.setVisibility(View.VISIBLE);
                } else {
                    noData.setVisibility(View.GONE);
                }

                ListView listView = getActivity().findViewById(R.id.listViewHistory);
                HistoryAdapter adapter = new HistoryAdapter(getContext(), mListHistory);
                listView.setAdapter(adapter);
                registerForContextMenu(listView);
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<HistoryVm>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getListHistory(null);
    }
}
