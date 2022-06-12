package com.example.projectsemiv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectsemiv.helper.SessionManager;

public class LoginActivity extends FragmentActivity {

    private SessionManager sessionManager;
    private EditText txtUserName, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);

        if (sessionManager.checkLogin()) {
            redirectMainActivity();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUserName.getText().toString().equals("Admin") && txtPassword.getText().toString().equals("123")) {
                    sessionManager.createLoginSession(txtUserName.getText().toString(), "Admin");
                    redirectMainActivity();
                }
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
    }
}