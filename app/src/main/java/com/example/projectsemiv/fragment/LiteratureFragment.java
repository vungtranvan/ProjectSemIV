package com.example.projectsemiv.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsemiv.MainActivity;
import com.example.projectsemiv.R;
import com.example.projectsemiv.adapter.SubjectAdapter;
import com.example.projectsemiv.entity.HistoryVm;
import com.example.projectsemiv.entity.QuestionHistoryVm;
import com.example.projectsemiv.helper.CommonData;
import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.services.ApiService;
import com.example.projectsemiv.slide.ScreenSlideActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiteratureFragment extends Fragment {

    private List<HistoryVm> mListHistory;
    private ProgressDialog mProgressDialog;
    private TextView noData;
    private SessionManager sessionManager;
    private int userId;
    GridView gvExamMath;

    public LiteratureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.literature_nav);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_literature, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        sessionManager = new SessionManager(getContext());
        userId = sessionManager.getUserIdInSession();
        noData = getActivity().findViewById(R.id.noDataLiterature);
        gvExamMath = (GridView) getActivity().findViewById(R.id.gvExamLiterature);
        getListHistory();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenu = item.getItemId();
        switch (idMenu) {
            case R.id.menuAddHistoryUser:
                addNewHistory();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewHistory() {
        mProgressDialog.show();
        ApiService.apiService.addHistory(userId, CommonData.ID_LITERATURE).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(getContext(), getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    getListHistory();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getListHistory() {
        mProgressDialog.show();
        ApiService.apiService.getAllHistoryByMember(userId, CommonData.ID_LITERATURE).enqueue(new Callback<List<HistoryVm>>() {
            @Override
            public void onResponse(Call<List<HistoryVm>> call, Response<List<HistoryVm>> response) {
                mListHistory = response.body();

                if (mListHistory == null || mListHistory.size() == 0) {
                    mListHistory = new ArrayList<>();
                    noData.setVisibility(View.VISIBLE);
                } else {
                    noData.setVisibility(View.GONE);
                }

                SubjectAdapter adapter = new SubjectAdapter(getContext(), mListHistory);
                gvExamMath.setAdapter(adapter);
                gvExamMath.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        HistoryVm historyVm = mListHistory.get(position);
                        loadDataHistoryDetail(historyVm.getId(), historyVm.isStatus());
                    }
                });
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<HistoryVm>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataHistoryDetail(int id, boolean status) {
        ApiService.apiService.getHistoryDetail(id).enqueue(new Callback<List<QuestionHistoryVm>>() {
            @Override
            public void onResponse(Call<List<QuestionHistoryVm>> call, Response<List<QuestionHistoryVm>> response) {
                List<QuestionHistoryVm> data = (ArrayList<QuestionHistoryVm>) response.body();

                if (data == null) {
                    data = new ArrayList<>();
                }

                Intent intent = new Intent(getActivity(), ScreenSlideActivity.class);
                intent.putExtra("_idH", id);
                intent.putExtra("test", status);
                intent.putExtra("arr_Ques", (ArrayList) data);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<QuestionHistoryVm>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getListHistory();
    }
}