package com.example.projectsemiv.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.helper.SessionManager;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccountActivity extends AppCompatActivity {

    private int idAcc;
    private String userName;
    private String passwordUser;
    private String imgUser;
    private boolean typeUser;
    private boolean typeUpdate;

    private SessionManager sessionManager;
    private TextInputLayout txtName, txtEmail, txtAddress, txtPassword;
    private RadioButton rdMale, rdFemale, rdTypeAdmin, rdTypeMember;
    private RadioGroup rdTypeAccount;
    private ValidateHelper validateHelper;
    private ProgressDialog mProgressDialog;

    private String imagePath = null;
    private CircleImageView imageView;
    private static int IMAGE_REQ = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.edit_account);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        initConfig();

        loadDetailData();

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
                if (!validateHelper.notEmpty(txtName, validateHelper.MIN_LENGTH_DEFAULT, 50) |
                        !validateHelper.isEmail(txtEmail) |
                        !validateHelper.notEmpty(txtAddress, validateHelper.MIN_LENGTH_DEFAULT, 250)
                ) {
                    return;
                }

                if (txtPassword.getEditText().getText().toString().trim().length() != 0 && !validateHelper.isPassword(txtPassword, 4, 12)) {
                    return;
                }

                mProgressDialog.show();
                checkDuplicateEmail();
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
        idAcc = getIntent().getExtras().getInt("idAcc");
        typeUpdate = getIntent().getExtras().getBoolean("typeUpdate");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        sessionManager = new SessionManager(this);
        validateHelper = new ValidateHelper(this);

        txtPassword = findViewById(R.id.txtPassword);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        rdTypeAdmin = findViewById(R.id.rdTypeAdmin);
        rdTypeMember = findViewById(R.id.rdTypeMember);
        rdTypeAccount = findViewById(R.id.rdTypeAccount);

        if (idAcc == 1 || typeUpdate) {
            rdTypeAccount.setVisibility(View.GONE);
            if (typeUpdate) {
                txtPassword.setVisibility(View.GONE);
            }
        }

        try {
            MediaManager.init(this);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void loadDetailData() {
        mProgressDialog.show();
        ApiService.apiService.getAccountById(idAcc).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account != null) {
                    userName = account.getUserName();
                    passwordUser = account.getPassword();
                    imgUser = account.getImage();
                    typeUser = account.isIsAdmin();

                    if (imgUser != null) {
                        Glide.with(UpdateAccountActivity.this).load(imgUser).into(imageView);
                    }
                    // set data
                    txtName.getEditText().setText(account.getName());
                    txtEmail.getEditText().setText(account.getEmail());
                    txtAddress.getEditText().setText(account.getAddress());
                    if (account.isSex()) {
                        rdMale.setChecked(true);
                    } else {
                        rdFemale.setChecked(true);
                    }

                    if (account.isIsAdmin()) {
                        rdTypeAdmin.setChecked(true);
                    } else {
                        rdTypeMember.setChecked(true);
                    }

                    mProgressDialog.dismiss();
                } else {
                    mProgressDialog.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(UpdateAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UpdateAccountActivity.this, new String[]{
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
            Glide.with(UpdateAccountActivity.this).load(imageUri).into(imageView);
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
                Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.upload_image_false), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                mProgressDialog.dismiss();
                Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.upload_image_false), Toast.LENGTH_SHORT).show();
            }
        }).dispatch();
    }

    private void checkDuplicateEmail() {
        ApiService.apiService.getAccountByEmail(txtEmail.getEditText().getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account != null && account.getId() != idAcc) {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallApi() {
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("_id", idAcc);
            paramObject.put("userName", userName);
            if (txtPassword.getEditText().getText().toString().trim().length() == 0 || typeUpdate) {
                paramObject.put("password", passwordUser);
            } else {
                paramObject.put("password", txtPassword.getEditText().getText().toString().trim());
            }

            paramObject.put("name", txtName.getEditText().getText().toString().trim());
            paramObject.put("email", txtEmail.getEditText().getText().toString().trim());
            if (imagePath == null) {
                paramObject.put("image", imgUser);
            } else {
                paramObject.put("image", imagePath);
            }

            paramObject.put("address", txtAddress.getEditText().getText().toString().trim());
            paramObject.put("sex", rdMale.isChecked());

            if (idAcc == 1 || typeUpdate) {
                paramObject.put("isAdmin", typeUser);
            } else {
                paramObject.put("isAdmin", rdTypeAdmin.isChecked());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService.apiService.editAccount(paramObject.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    finish();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eventValidateInput() {

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
                if (txtPassword.getEditText().getText().toString().trim().length() == 0) {
                    txtPassword.setError(null);
                } else {
                    validateHelper.isPassword(txtPassword, 4, 12);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
}