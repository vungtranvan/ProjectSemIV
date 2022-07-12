package com.example.projectsemiv.helper;

import android.content.Context;
import android.util.Patterns;

import com.example.projectsemiv.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class ValidateHelper {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    private Context mContext;

    public ValidateHelper(Context context) {
        mContext = context;
    }

    public boolean notEmpty(TextInputLayout input, int minLength, int maxLength) {
        String nameInput = input.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            input.setError(mContext.getResources().getString(R.string.field_can_not_empty));
            return false;
        } else if (nameInput.length() < minLength) {
            input.setError(mContext.getResources().getString(R.string.field_min_length) + ": " + minLength);
            return false;
        } else if (nameInput.length() > maxLength) {
            input.setError(mContext.getResources().getString(R.string.field_max_length) + ": " + maxLength);
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

    public boolean isEmail(TextInputLayout input) {
        String emailInput = input.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            input.setError(mContext.getResources().getString(R.string.field_can_not_empty));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            input.setError(mContext.getResources().getString(R.string.email_not_valid));
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

    public boolean isPassword(TextInputLayout input, int minLength, int maxLength) {
        String passwordInput = input.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            input.setError(mContext.getResources().getString(R.string.field_can_not_empty));
            return false;
        } else if (passwordInput.length() < minLength) {
            input.setError(mContext.getResources().getString(R.string.field_min_length) + ": " + minLength);
            return false;
        } else if (passwordInput.length() > maxLength) {
            input.setError(mContext.getResources().getString(R.string.field_max_length) + ": " + maxLength);
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            input.setError(mContext.getResources().getString(R.string.password_too_weak));
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

    public boolean isConfirmPassword(TextInputLayout input, int minLength, int maxLength, String value) {
        String passwordInput = input.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            input.setError(mContext.getResources().getString(R.string.field_can_not_empty));
            return false;
        } else if (passwordInput.length() < minLength) {
            input.setError(mContext.getResources().getString(R.string.field_min_length) + ": " + minLength);
            return false;
        } else if (passwordInput.length() > maxLength) {
            input.setError(mContext.getResources().getString(R.string.field_max_length) + ": " + maxLength);
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            input.setError(mContext.getResources().getString(R.string.password_too_weak));
            return false;
        } else if (!passwordInput.equals(value)) {
            input.setError(mContext.getResources().getString(R.string.confirm_password_is_correct));
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

    public boolean isConfirmPasswordNew(TextInputLayout input, int minLength, int maxLength, String value) {
        String passwordInput = input.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            input.setError(mContext.getResources().getString(R.string.field_can_not_empty));
            return false;
        } else if (passwordInput.length() < minLength) {
            input.setError(mContext.getResources().getString(R.string.field_min_length) + ": " + minLength);
            return false;
        } else if (passwordInput.length() > maxLength) {
            input.setError(mContext.getResources().getString(R.string.field_max_length) + ": " + maxLength);
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            input.setError(mContext.getResources().getString(R.string.password_too_weak));
            return false;
        } else if (!passwordInput.equals(value)) {
            input.setError(mContext.getResources().getString(R.string.confirm_password_new_is_correct));
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }
}
