package com.example.projectsemiv;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.helper.ValidateHelper;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends FragmentActivity {

    private SessionManager sessionManager;
    private TextInputLayout txtUserName, txtPassword;
    private ValidateHelper validateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        if (sessionManager.checkLogin()) {
            redirectMainActivity();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        validateHelper = new ValidateHelper(LoginActivity.this);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        eventValidateInput();

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateHelper.notEmpty(txtUserName, validateHelper.MIN_LENGTH_DEFAULT, validateHelper.MAX_LENGTH_DEFAULT) |
                        !validateHelper.isPassword(txtPassword, 4, 12)
                ) {
                    return;
                }

                if (txtUserName.getEditText().getText().toString().equals("Admin") && txtPassword.getEditText().getText().toString().equals("Admin@123")) {
                    sessionManager.createLoginSession(txtUserName.getEditText().getText().toString(), "Trần Văn Vững", "image_user_default", "Admin");
                    redirectMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.username_or_password_not_correct), Toast.LENGTH_SHORT).show();
                }
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
                validateHelper.notEmpty(txtUserName, validateHelper.MIN_LENGTH_DEFAULT, validateHelper.MAX_LENGTH_DEFAULT);
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
                validateHelper.isPassword(txtPassword, 4, 12);
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