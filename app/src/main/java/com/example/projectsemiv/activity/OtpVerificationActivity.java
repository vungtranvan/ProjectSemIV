package com.example.projectsemiv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectsemiv.R;
import com.example.projectsemiv.helper.CommonData;

public class OtpVerificationActivity extends AppCompatActivity {

    private String code;
    private int idU;
    private Button btnSubmit;
    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private int countEnterCodeError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        code = getIntent().getExtras().getString("code");
        idU = getIntent().getExtras().getInt("idU");

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);


        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);

        setupOtpInputs();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeInput = inputCode1.getText().toString().trim() + inputCode2.getText().toString().trim() +
                        inputCode3.getText().toString().trim() + inputCode4.getText().toString().trim() +
                        inputCode5.getText().toString().trim() + inputCode6.getText().toString().trim();
                if (!codeInput.equals(code)) {
                    countEnterCodeError += 1;

                    if (countEnterCodeError > CommonData.NUMBER_MAX_ENTER_FAIL_OTP) {
                        Toast.makeText(OtpVerificationActivity.this, getResources().getString(R.string.verification_enter_max_fail) + " " +
                                String.valueOf(CommonData.NUMBER_MAX_ENTER_FAIL_OTP) + " " + getResources().getString(R.string.times), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(OtpVerificationActivity.this, getResources().getString(R.string.verification_fail), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Intent intent = new Intent(OtpVerificationActivity.this, ForgotPasswordActivity.class);
                    intent.putExtra("idU", idU);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setupOtpInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
                checkValidateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
                checkValidateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
                checkValidateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
                checkValidateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }
                checkValidateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                checkValidateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkValidateInput() {
        if (!inputCode1.getText().toString().trim().isEmpty() &&
                !inputCode2.getText().toString().trim().isEmpty() &&
                !inputCode3.getText().toString().trim().isEmpty() &&
                !inputCode4.getText().toString().trim().isEmpty() &&
                !inputCode5.getText().toString().trim().isEmpty() &&
                !inputCode6.getText().toString().trim().isEmpty()
        ) {
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnSubmit.setVisibility(View.GONE);
        }
    }
}