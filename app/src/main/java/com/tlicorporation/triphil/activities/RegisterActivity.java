package com.tlicorporation.triphil.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tlicorporation.triphil.R;
//import com.tlicorporation.triphil.helpers.InputValidation;
import com.tlicorporation.triphil.model.User;
import com.tlicorporation.triphil.sql.DatabaseHelper;


public class RegisterActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputEditText txtName;
    private TextInputEditText txtEmail;
    private TextInputEditText txtPassword;
    private TextInputEditText txtConfirmPassword;
    private AppCompatButton btnRegister;
    private AppCompatTextView txtVwLoginLink;

    //private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
        txtConfirmPassword.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        postDataToSQLite();

                    default:
                        break;
                }
            }
            return false;
        });
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        tilName = findViewById(R.id.textInputLayoutName);
        tilEmail = findViewById(R.id.textInputLayoutEmail);
        tilPassword = findViewById(R.id.textInputLayoutPassword);
        tilConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        txtName = findViewById(R.id.textInputEditName);
        txtEmail = findViewById(R.id.textInputEditTextEmail);
        txtPassword = findViewById(R.id.textInputEditTextPassword);
        txtConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);

        btnRegister = findViewById(R.id.appCompatButtonRegister);

        txtVwLoginLink = findViewById(R.id.appCompatTextViewLoginLink);

    }

    private void initListeners() {
        btnRegister.setOnClickListener(this);
        txtVwLoginLink.setOnClickListener(this);

    }

    private void initObjects() {
        //inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.appCompatButtonRegister){
            postDataToSQLite();
        }else if(v.getId() == R.id.appCompatTextViewLoginLink){
            finish();
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {

        String value = txtName.getText().toString().trim();
        if (value.isEmpty()) {
            tilName.setError("Empty Name");
            hideKeyboardFrom(txtName);
            return;
        } else {
            tilName.setErrorEnabled(false);
        }

        value = txtPassword.getText().toString().trim();

        if (value.isEmpty()) {
            tilEmail.setError("Empty Email");
            hideKeyboardFrom(txtEmail);
            return;
        } else {
            tilEmail.setErrorEnabled(false);
        }

        value = txtPassword.getText().toString().trim();

        if (value.isEmpty()) {
            tilPassword.setError("Empty Password");
            hideKeyboardFrom(txtPassword);
            return;
        } else {
            tilPassword.setErrorEnabled(false);
        }

        String pw = txtPassword.getText().toString().trim();
        String con_pw = txtConfirmPassword.getText().toString().trim();
        if (!pw.contentEquals(con_pw)) {
            tilConfirmPassword.setError("Password Not Match!");
            hideKeyboardFrom(txtConfirmPassword);
            return ;
        } else {
            tilConfirmPassword.setErrorEnabled(false);
        }

        if (!databaseHelper.checkUser(txtEmail.getText().toString().trim())) {

            user.setName(txtName.getText().toString().trim());
            user.setEmail(txtEmail.getText().toString().trim());
            user.setPassword(txtPassword.getText().toString().trim());
            databaseHelper.addUser(user);
            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        txtName.setText(null);
        txtEmail.setText(null);
        txtPassword.setText(null);
        txtConfirmPassword.setText(null);
    }

    private void hideKeyboardFrom(View view) {
        Context context;
        context = this.activity;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}