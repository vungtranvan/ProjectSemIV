package com.example.projectsemiv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projectsemiv.LoginActivity;
import com.example.projectsemiv.R;
import com.example.projectsemiv.RegisterActivity;
import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.helper.EmailHelper;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendEmailActivity extends AppCompatActivity {

    private TextInputLayout txtEmail;
    private ValidateHelper validateHelper;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        validateHelper = new ValidateHelper(SendEmailActivity.this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        txtEmail = findViewById(R.id.txtEmail);
        eventValidateInput();

        Button btnSendEmail = findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateHelper.isEmail(txtEmail)) {
                    return;
                }
                checkExitEmail();
            }
        });
    }

    private void checkExitEmail() {
        ApiService.apiService.getAccountByEmail(txtEmail.getEditText().getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account == null) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SendEmailActivity.this, getResources().getString(R.string.email_not_exit), Toast.LENGTH_SHORT).show();
                } else {
                    String code = randomCode();
                    //sendEmail(account.getEmail(), code);
                    new EmailHelper(SendEmailActivity.this).sendEmail(account.getEmail(), code);
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(SendEmailActivity.this, OtpVerificationActivity.class);
                    intent.putExtra("code", code);
                    intent.putExtra("idU", account.getId());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SendEmailActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void sendEmail(String toEmail, String code) {
//
//        try {
//            String stringSenderEmail = "vunghihi321@gmail.com";
//            String stringReceiverEmail = toEmail;
//            String stringPasswordSenderEmail = "yxnutdvgljofrjro";
//
//            String stringHost = "smtp.gmail.com";
//
//            Properties properties = System.getProperties();
//
//            properties.put("mail.smtp.host", stringHost);
//            properties.put("mail.smtp.port", "465");
//            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.auth", "true");
//
//            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
//                }
//            });
//
//            MimeMessage mimeMessage = new MimeMessage(session);
//            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
//
//            mimeMessage.setSubject(getResources().getString(R.string.app_name));
//            mimeMessage.setText(getResources().getString(R.string.opt_code) + " " + code);
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Transport.send(mimeMessage);
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();
//
//        } catch (AddressException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }

    private void eventValidateInput() {
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
    }

    private String randomCode() {
        int max = 999999;
        int min = 100000;
        return String.valueOf((int) Math.floor(Math.random() * (max - min + 1) + min));
    }
}