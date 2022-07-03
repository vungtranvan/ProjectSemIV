package com.example.projectsemiv.services;

import com.example.projectsemiv.entity.ChangPasswordVm;
import com.example.projectsemiv.entity.QuestionVm;
import com.example.projectsemiv.entity.Account;
import com.example.projectsemiv.entity.CategoryExam;
import com.example.projectsemiv.entity.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/WebApiExamTest/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);

    //Category Exam
    @GET("categoryExam")
    Call<List<CategoryExam>> getAllCategoryExam();

    @GET("categoryExam/getById/{id}")
    Call<CategoryExam> getCategoryExamById(@Path("id") int id);

    // Account
    @GET("account")
    Call<List<Account>> getAllAccount(@Query("keySearch") String keySearch);

    @GET("account/getById/{id}")
    Call<Account> getAccountById(@Path("id") int id);

    @GET("account/getByUserName/{userName}")
    Call<Account> getAccountByUserName(@Path("userName") String userName);

    @GET("account/getByEmail/{email}")
    Call<Account> getAccountByEmail(@Path("email") String email);

    @Headers("Content-Type: application/json")
    @POST("account/addAccount")
    Call<String> addAccount(@Body String strData);

    @Headers("Content-Type: application/json")
    @POST("account/registerAccount")
    Call<String> registerAccount(@Body String strData);

    @Headers("Content-Type: application/json")
    @POST("account/updatePasswordByUsername")
    Call<String> updatePasswordByUsername(@Body ChangPasswordVm entity);

    @Headers("Content-Type: application/json")
    @PUT("account/editAccount")
    Call<String> editAccount(@Body String strData);

    @DELETE("account/deleteAccount/{id}")
    Call<String> deleteAccountById(@Path("id") int id);

    //Question
    @GET("question")
    Call<List<QuestionVm>> getAllQuestion(@Query("keySearch") String keySearch);

    @GET("question/getById/{id}")
    Call<Question> getQuestionById(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @POST("question/addQuestion")
    Call<String> addQuestion(@Body Question entity);

    @Headers("Content-Type: application/json")
    @PUT("question/editQuestion")
    Call<String> editQuestion(@Body Question entity);

    @DELETE("question/deleteQuestion/{id}")
    Call<String> deleteQuestionById(@Path("id") int id);
}
