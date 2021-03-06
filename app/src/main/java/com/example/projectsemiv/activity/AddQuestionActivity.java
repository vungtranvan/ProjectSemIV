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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.CategoryExam;
import com.example.projectsemiv.helper.CommonData;
import com.example.projectsemiv.helper.ValidateHelper;
import com.example.projectsemiv.services.ApiService;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQuestionActivity extends AppCompatActivity {

    private List<CategoryExam> lstCategory;
    private ValidateHelper validateHelper;
    private ProgressDialog mProgressDialog;

    private TextInputLayout txtName, txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD;
    private Spinner spAnswerCorrect, spCategoryExam;

    private String imagePath = null;
    private CircleImageView imageView;
    private static int IMAGE_REQ = 1;
    private Uri imageUri;

    private int categoryExamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.add_question);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        initConfig();
        setDataSpinner();

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
                if (!validateHelper.notEmpty(txtName, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE) |
                        !validateHelper.notEmpty(txtAnswerA, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE) |
                        !validateHelper.notEmpty(txtAnswerB, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE) |
                        !validateHelper.notEmpty(txtAnswerC, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE) |
                        !validateHelper.notEmpty(txtAnswerD, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE)
                ) {
                    return;
                }
                mProgressDialog.show();
                if (imageUri != null) {
                    uploadImage();
                } else {
                    callApi();
                }
            }
        });

        imageView = findViewById(R.id.imgQuestion);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
            }
        });
    }

    private void initConfig() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));

        validateHelper = new ValidateHelper(AddQuestionActivity.this);

        txtName = findViewById(R.id.txtName);
        txtAnswerA = findViewById(R.id.txtAnswerA);
        txtAnswerB = findViewById(R.id.txtAnswerB);
        txtAnswerC = findViewById(R.id.txtAnswerC);
        txtAnswerD = findViewById(R.id.txtAnswerD);
        spAnswerCorrect = findViewById(R.id.spAnswerCorrect);
        spCategoryExam = findViewById(R.id.spCategoryExam);

        try {
            MediaManager.init(this);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void setDataSpinner() {

        ApiService.apiService.getAllCategoryExam().enqueue(new Callback<List<CategoryExam>>() {
            @Override
            public void onResponse(Call<List<CategoryExam>> call, Response<List<CategoryExam>> response) {
                lstCategory = response.body();

                if (lstCategory == null || lstCategory.size() == 0) {
                    lstCategory = new ArrayList<>();
                }

                ArrayAdapter categoryAdapter = new ArrayAdapter(AddQuestionActivity.this, R.layout.spinner, lstCategory);
                spCategoryExam.setAdapter(categoryAdapter);
                spCategoryExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CategoryExam cate = (CategoryExam) parent.getSelectedItem();
                        categoryExamId = cate.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<CategoryExam>> call, Throwable t) {
                Toast.makeText(AddQuestionActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(AddQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddQuestionActivity.this, new String[]{
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
            Glide.with(AddQuestionActivity.this).load(imageUri).into(imageView);
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
                callApi();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                mProgressDialog.dismiss();
                Toast.makeText(AddQuestionActivity.this, getResources().getString(R.string.upload_image_false), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                mProgressDialog.dismiss();
                Toast.makeText(AddQuestionActivity.this, getResources().getString(R.string.upload_image_false), Toast.LENGTH_SHORT).show();
            }
        }).dispatch();
    }

    private void callApi() {
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("name", txtName.getEditText().getText().toString());
            paramObject.put("categoryExamId", categoryExamId);
            paramObject.put("answerA", txtAnswerA.getEditText().getText().toString());
            paramObject.put("answerB", txtAnswerB.getEditText().getText().toString());
            paramObject.put("answerC", txtAnswerC.getEditText().getText().toString());
            paramObject.put("answerD", txtAnswerD.getEditText().getText().toString());
            paramObject.put("answerCorrect", spAnswerCorrect.getSelectedItem().toString());
            paramObject.put("image", imagePath);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService.apiService.addQuestion(paramObject.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean bl = Boolean.parseBoolean(response.body());
                if (bl) {
                    Toast.makeText(AddQuestionActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    finish();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddQuestionActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AddQuestionActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
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
                validateHelper.notEmpty(txtName, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtAnswerA.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.notEmpty(txtAnswerA, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtAnswerB.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.notEmpty(txtAnswerB, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtAnswerC.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.notEmpty(txtAnswerC, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtAnswerD.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateHelper.notEmpty(txtAnswerD, CommonData.MIN_LENGTH_DEFAULT, Integer.MAX_VALUE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}