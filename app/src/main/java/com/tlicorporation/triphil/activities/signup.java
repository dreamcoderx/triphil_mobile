package com.tlicorporation.triphil.activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.R;
public class signup extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtConfirmPassword;
    Spinner spinnerUserRole;
    Button btnSignUp;
    ProgressBar progressBar;
    LinearLayout lvparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        spinnerUserRole = findViewById(R.id.spnrUserRole);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.pbScan);
        lvparent = findViewById(R.id.lvparent);
        this.setTitle("User SignUp");

        String[] items = new String[]{"user","admin","superadmin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinnerUserRole.setAdapter(adapter);

        //getSupportFragmentManager().beginTransaction().show(this);
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);


        btnSignUp.setOnClickListener(v -> {

            if (isEmpty(edtUsername.getText().toString()) ||
                    isEmpty(edtPassword.getText().toString()) ||
                    isEmpty(edtConfirmPassword.getText().toString()))
                ShowSnackBar("Please enter all fields");
            else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString()))
                ShowSnackBar("Password does not match");
            else {
                AddUsers addUsers = new AddUsers();
                addUsers.execute("");
            }

        });
    }

    public void ShowSnackBar(String message) {
        Snackbar.make(lvparent, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", view -> {

                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public Boolean isEmpty(String strValue) {
        if (strValue == null || strValue.trim().equals((""))) {
            return true;
        }else {
            return false;
        }
    }

    public class AddUsers extends AsyncTask<String, Void, String> {
        String username, password;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = edtUsername.getText().toString();
            password = edtPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Context context;
                context = getApplicationContext();
                Connection connection = ConnectionClass.CONN(context);
                String queryStmt = "Insert into tblusers " +
                        " (username, password, user_role) values "
                        + "('"
                        + username
                        + "','"
                        + password
                        + "','" + spinnerUserRole.getSelectedItem().toString() + "')";
                PreparedStatement preparedStatement = connection.prepareStatement(queryStmt);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                return "Added successfully";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                return "Exception. Please check your code and database.";
            }
        }
        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(signup.this, result, Toast.LENGTH_SHORT).show();
            ShowSnackBar(result);
            progressBar.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.VISIBLE);
            //if (result.equals("Added successfully")) {
                // Clear();
            //}

        }


    }

}