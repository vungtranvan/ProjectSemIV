package com.example.projectsemiv;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsemiv.activity.SendEmailActivity;
import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.helper.CommonData;
import com.example.projectsemiv.helper.CommonHelper;
import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends FragmentActivity {

    private SessionManager sessionManager;
    private TextInputLayout txtUserName, txtPassword;
    private ValidateHelper validateHelper;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        if (sessionManager.checkLogin()) {
            redirectMainActivity();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        validateHelper = new ValidateHelper(LoginActivity.this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        eventValidateInput();

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateHelper.notEmpty(txtUserName, CommonData.MIN_LENGTH_DEFAULT, CommonData.MAX_LENGTH_DEFAULT) |
                        !validateHelper.isPassword(txtPassword, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD)
                ) {
                    return;
                }
                mProgressDialog.show();
                ApiService.apiService.getAccountByUserName(txtUserName.getEditText().getText().toString()).enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        Account account = response.body();
                        if (account == null) {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.username_not_exist), Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            return;
                        }
                        if (!account.getPassword().equals(CommonHelper.toMd5(txtPassword.getEditText().getText().toString()))) {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.password_not_correct), Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            return;
                        }
                        sessionManager.createLoginSession(String.valueOf(account.getId()), account.getName(), account.getImage(), String.valueOf(account.isIsAdmin()));
                        mProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        redirectMainActivity();
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        mProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        TextView btnRegister = findViewById(R.id.tvSignUp);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SendEmailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventValidateInput() {
        txtUserName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.notEmpty(txtUserName, CommonData.MIN_LENGTH_DEFAULT, CommonData.MAX_LENGTH_DEFAULT);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.isPassword(txtPassword, CommonData.MIN_LENGTH_PASSWORD, CommonData.MAX_LENGTH_PASSWORD);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.checkLogin()) {
            redirectMainActivity();
        }
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}