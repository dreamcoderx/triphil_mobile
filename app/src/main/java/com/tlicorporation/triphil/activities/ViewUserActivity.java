package com.tlicorporation.triphil.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.adapters.UserViewAdapter;
import com.tlicorporation.triphil.model.User;
import com.tlicorporation.triphil.model.UserView;
import com.tlicorporation.triphil.sql.DatabaseHelper;

import java.util.ArrayList;

public class ViewUserActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    DatabaseHelper database_helper;
    ArrayList<UserView> allUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        recyclerView =  findViewById(R.id.recycler_view);
        database_helper = new DatabaseHelper(this);
        displayUser();
        actionButton =  findViewById(R.id.add);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog();
            }
        });
    }
    public void displayUser() {
        allUser = new ArrayList<>(database_helper.getAllUserArray());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        UserViewAdapter adapter = new UserViewAdapter(getApplicationContext(), this, allUser);
        recyclerView.setAdapter(adapter);
    }
    public void showDialog() {
        final EditText name, email, password, confirmpassword;
        AppCompatButton register;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.activity_register);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        name = dialog.findViewById(R.id.textInputEditName);
        email = dialog.findViewById(R.id.textInputEditTextEmail);
        password =dialog.findViewById(R.id.textInputEditTextPassword);
        confirmpassword = dialog.findViewById(R.id.textInputEditTextConfirmPassword);
        register = dialog.findViewById(R.id.appCompatButtonRegister);
        register.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Please Enter Username");
                }else if(email.getText().toString().isEmpty()) {
                    email.setError("Please Enter User Email");
                }else if(password.getText().toString().isEmpty()) {
                    password.setError("Please Enter Password!");
                }else if(!password.getText().toString().equals(confirmpassword.getText().toString())) {
                    confirmpassword.setError("Password is not equal!");
                }else {
                    User usr = new User();
                    usr.setName(name.getText().toString());
                    usr.setEmail(email.getText().toString());
                    usr.setPassword(password.getText().toString());
                    database_helper.addUser(usr);
                    dialog.cancel();
                    displayUser();
                }
            }
        });
    }
}