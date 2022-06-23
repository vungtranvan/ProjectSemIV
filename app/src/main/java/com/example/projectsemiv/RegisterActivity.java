package com.example.projectsemiv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextInputLayout txtUserName, txtName, txtEmail, txtAddress, txtPassword, txtConfirmPassword;
    private RadioButton rdMaleOfResgiter, rdFemaleOfResgiter;
    private ValidateHelper validateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);
        validateHelper = new ValidateHelper(RegisterActivity.this);

        txtUserName = findViewById(R.id.txtUserNameOfResgiter);
        txtPassword = findViewById(R.id.txtPasswordOfResgiter);
        txtConfirmPassword = findViewById(R.id.txtConfirmPasswordOfResgiter);
        txtName = findViewById(R.id.txtNameOfResgiter);
        txtEmail = findViewById(R.id.txtEmailOfResgiter);
        txtAddress = findViewById(R.id.txtAddressOfResgiter);
        rdMaleOfResgiter = findViewById(R.id.rdMaleOfResgiter);
        rdFemaleOfResgiter = findViewById(R.id.rdFemaleOfResgiter);

        eventValidateInput();

        Button btnRegister = findViewById(R.id.btnResgiter);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateHelper.notEmpty(txtUserName, validateHelper.MIN_LENGTH_DEFAULT, validateHelper.MAX_LENGTH_DEFAULT) |
                        !validateHelper.isPassword(txtPassword, 4, 12) |
                        !validateHelper.notEmpty(txtName, validateHelper.MIN_LENGTH_DEFAULT, 50) |
                        !validateHelper.isEmail(txtEmail) |
                        !validateHelper.notEmpty(txtAddress, validateHelper.MIN_LENGTH_DEFAULT, 250) |
                        !validateHelper.isConfirmPassword(txtConfirmPassword, 4, 12, txtPassword.getEditText().getText().toString())
                ) {
                    return;
                }

                checkDuplicateUserName();
            }
        });

        TextView tvSignInNow = findViewById(R.id.tvSignInNow);
        tvSignInNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectLoginActivity();
            }
        });
    }

    private void checkDuplicateUserName() {
        ApiService.apiService.getAccountByUserName(txtUserName.getEditText().getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account != null) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.username_exist), Toast.LENGTH_SHORT).show();
                } else {
                    checkDuplicateEmail();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void checkDuplicateEmail() {
        ApiService.apiService.getAccountByEmail(txtEmail.getEditText().getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account != null) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
                }else{
                    CallApi();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallApi() {
        Account account = new Account(txtUserName.getEditText().getText().toString(),
                txtPassword.getEditText().getText().toString(), txtName.getEditText().getText().toString(),
                txtEmail.getEditText().getText().toString(), "img_default",
                txtAddress.getEditText().getText().toString(), rdMaleOfResgiter.isChecked(), false);
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("userName", account.getUserName());
            paramObject.put("password", account.getPassword());
            paramObject.put("name", account.getName());
            paramObject.put("email", account.getEmail());
            paramObject.put("image", account.getImage());
            paramObject.put("address", account.getAddress());
            paramObject.put("sex", account.isSex());
            paramObject.put("isAdmin", account.isAdmin());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService.apiService.addAccount(paramObject.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(account.getUserName(), account.getName(), account.getImage(), account.isAdmin() ? "Admin" : "Member");
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    redirectMainActivity();
                } else {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void redirectLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

        txtName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.notEmpty(txtName, validateHelper.MIN_LENGTH_DEFAULT, 50);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.isEmail(txtEmail);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtAddress.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.notEmpty(txtAddress, validateHelper.MIN_LENGTH_DEFAULT, 250);
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

        txtConfirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.isConfirmPassword(txtConfirmPassword, 4, 12, txtPassword.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}