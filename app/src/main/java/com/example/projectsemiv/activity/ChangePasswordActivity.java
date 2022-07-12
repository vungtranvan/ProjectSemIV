package com.example.projectsemiv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.entity.ChangPasswordVm;
import com.example.projectsemiv.helper.CommonData;
import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextInputLayout txtPasswordOld, txtPasswordNew, txtConfirmPasswordNew;
    private ValidateHelper validateHelper;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.change_pass_nav);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initConfig();
        eventValidateInput();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateHelper.isPassword(txtPasswordOld, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD) |
                        !validateHelper.isPassword(txtPasswordNew, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD) |
                        !validateHelper.isConfirmPasswordNew(txtConfirmPasswordNew, CommonData.MIN_LENGTH_PASSWORD,
                                CommonData.MAX_LENGTH_PASSWORD, txtPasswordNew.getEditText().getText().toString())
                ) {
                    return;
                }

                if (txtPasswordOld.getEditText().getText().toString().equals(txtPasswordNew.getEditText().getText().toString())) {
                    txtPasswordNew.setError(getResources().getString(R.string.password_new_must_be_other_password_old));
                    return;
                } else {
                    txtPasswordNew.setError(null);
                }

                checkPasswordValid();
            }
        });
    }

    private void initConfig() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        sessionManager = new SessionManager(this);
        validateHelper = new ValidateHelper(this);

        txtPasswordOld = findViewById(R.id.txtPasswordOld);
        txtPasswordNew = findViewById(R.id.txtPasswordNew);
        txtConfirmPasswordNew = findViewById(R.id.txtConfirmPasswordNew);
    }

    private void eventValidateInput() {
        txtPasswordOld.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.isPassword(txtPasswordOld, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtPasswordNew.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validateHelper.isPassword(txtPasswordNew, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD)) {
                    if (txtPasswordOld.getEditText().getText().toString().length() != 0) {
                        if (txtPasswordOld.getEditText().getText().toString().equals(txtPasswordNew.getEditText().getText().toString())) {
                            txtPasswordNew.setError(getResources().getString(R.string.password_new_must_be_other_password_old));
                            return;
                        } else {
                            txtPasswordNew.setError(null);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtConfirmPasswordNew.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.isConfirmPasswordNew(txtConfirmPasswordNew, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD,
                        txtPasswordNew.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void checkPasswordValid() {
        mProgressDialog.show();
        ApiService.apiService.getAccountById(sessionManager.getUserIdInSession()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account == null || !account.getPassword().equals(txtPasswordOld.getEditText().getText().toString())) {
                    mProgressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.password_old_not_correct), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    callApi();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApi() {
        ChangPasswordVm entity = new ChangPasswordVm();
        entity.setUserId(sessionManager.getUserIdInSession());
        entity.setPassword(txtPasswordNew.getEditText().getText().toString());
        entity.setConfirmPassword(txtConfirmPasswordNew.getEditText().getText().toString());

        ApiService.apiService.updatePasswordByUserId(entity).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    finish();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}