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
import com.example.projectsemiv.entity.ChangPasswordVm;
import com.example.projectsemiv.helper.CommonData;
import com.example.projectsemiv.helper.CommonHelper;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout txtPasswordNew, txtConfirmPasswordNew;
    private ValidateHelper validateHelper;
    private ProgressDialog mProgressDialog;
    private int idU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.change_pass_nav);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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
                if (!validateHelper.isPassword(txtPasswordNew, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD) |
                        !validateHelper.isConfirmPasswordNew(txtConfirmPasswordNew, CommonData.MIN_LENGTH_PASSWORD,
                                CommonData.MAX_LENGTH_PASSWORD, txtPasswordNew.getEditText().getText().toString())
                ) {
                    return;
                }
                callApi();
            }
        });
    }

    private void initConfig() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        validateHelper = new ValidateHelper(this);

        idU = getIntent().getExtras().getInt("idU");

        txtPasswordNew = findViewById(R.id.txtPasswordNew);
        txtConfirmPasswordNew = findViewById(R.id.txtConfirmPasswordNew);
    }

    private void eventValidateInput() {

        txtPasswordNew.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.isPassword(txtPasswordNew, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD);
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


    private void callApi() {
        mProgressDialog.show();
        ChangPasswordVm entity = new ChangPasswordVm();
        entity.setUserId(idU);
        entity.setPassword(CommonHelper.toMd5(txtPasswordNew.getEditText().getText().toString().trim()));
        entity.setConfirmPassword(CommonHelper.toMd5(txtConfirmPasswordNew.getEditText().getText().toString().trim()));

        ApiService.apiService.updatePasswordByUserId(entity).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    finish();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}