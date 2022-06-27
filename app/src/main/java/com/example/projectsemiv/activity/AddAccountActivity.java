package com.example.projectsemiv.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.projectsemiv.MainActivity;
import com.example.projectsemiv.R;
import com.example.projectsemiv.RegisterActivity;
import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.fragment.ManagerAccountFragment;
import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAccountActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextInputLayout txtUserName, txtName, txtEmail, txtAddress, txtPassword, txtConfirmPassword;
    private RadioButton rdMale, rdFemale, rdTypeAdmin, rdTypeMember;
    private ValidateHelper validateHelper;
    private ProgressDialog mProgressDialog;

    private String imagePath = null;
    private CircleImageView imageView;
    private static int IMAGE_REQ = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.add_account);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        initConfig();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        sessionManager = new SessionManager(this);
        validateHelper = new ValidateHelper(AddAccountActivity.this);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        rdMale = findViewById(R.id.rdMale);
        rdTypeAdmin = findViewById(R.id.rdTypeAdmin);

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
                if (!validateHelper.notEmpty(txtUserName, validateHelper.MIN_LENGTH_DEFAULT, validateHelper.MAX_LENGTH_DEFAULT) |
                        !validateHelper.isPassword(txtPassword, 4, 12) |
                        !validateHelper.notEmpty(txtName, validateHelper.MIN_LENGTH_DEFAULT, 50) |
                        !validateHelper.isEmail(txtEmail) |
                        !validateHelper.notEmpty(txtAddress, validateHelper.MIN_LENGTH_DEFAULT, 250) |
                        !validateHelper.isConfirmPassword(txtConfirmPassword, 4, 12, txtPassword.getEditText().getText().toString())
                ) {
                    return;
                }
                mProgressDialog.show();
                checkDuplicateUserName();
            }
        });

        imageView = findViewById(R.id.imgAccount);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
            }
        });
    }

    private void initConfig() {
//        Map config = new HashMap();
//        config.put("cloud_name", "dbxeyrb6x");
//        config.put("api_key", "961662384742667");
//        config.put("api_secret", "Voe5pAgeYMhyIU9sukfvuEoj_fQ");
//        config.put("secure", true);

        try {
            MediaManager.init(this);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(AddAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddAccountActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, IMAGE_REQ);
        }
        selectImage();
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQ && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(AddAccountActivity.this).load(imageUri).into(imageView);
        }
    }

    private void uploadImage() {
        MediaManager.get().upload(imageUri).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {

            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                imagePath = resultData.get("secure_url").toString();
                CallApi();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                mProgressDialog.dismiss();
                Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.upload_image_false), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                mProgressDialog.dismiss();
                Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.upload_image_false), Toast.LENGTH_SHORT).show();
            }
        }).dispatch();
    }

    private void checkDuplicateUserName() {
        ApiService.apiService.getAccountByUserName(txtUserName.getEditText().getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account != null) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.username_exist), Toast.LENGTH_SHORT).show();
                } else {
                    checkDuplicateEmail();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkDuplicateEmail() {
        ApiService.apiService.getAccountByEmail(txtEmail.getEditText().getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account != null) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
                } else {
                    if (imageUri != null) {
                        uploadImage();
                    } else {
                        CallApi();
                    }
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallApi() {

        Account account = new Account(txtUserName.getEditText().getText().toString(),
                txtPassword.getEditText().getText().toString(), txtName.getEditText().getText().toString(),
                txtEmail.getEditText().getText().toString(), imagePath,
                txtAddress.getEditText().getText().toString(), rdMale.isChecked(), rdTypeAdmin.isChecked());
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("userName", account.getUserName());
            paramObject.put("password", account.getPassword());
            paramObject.put("name", account.getName());
            paramObject.put("email", account.getEmail());
            paramObject.put("image", account.getImage());
            paramObject.put("address", account.getAddress());
            paramObject.put("sex", account.isSex());
            paramObject.put("isAdmin", account.isIsAdmin());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService.apiService.addAccount(paramObject.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    finish();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddAccountActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
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