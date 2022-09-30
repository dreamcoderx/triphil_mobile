package com.tlicorporation.triphil.activities;

import static java.lang.String.valueOf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.custom.MessageBox;
import com.tlicorporation.triphil.helpers.singleToneClass;
import com.tlicorporation.triphil.sql.DBQuery;
import com.tlicorporation.triphil.sqldatabase.UserLog;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView scrollView;
    private TextInputEditText etName;
    private TextInputEditText etPassword;
    private AppCompatButton btnLogIn;
    private ProgressBar progBar;
    private AppCompatTextView lnkRegister;
    private AppCompatTextView textViewSettings;
    private static BarcodeReader barcodeReader;
    private AidcManager manager;
    private TextView tvBuildDate;

    private void hideKeyboardFrom(View view) {
        Context context;
        context = this.activity;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        DBQuery dbq;
        dbq = new DBQuery(this);
        dbq.createDatabase();
        dbq.close();
        initViews();
        initListeners();
        hideKeyboardFrom(etName);
        try{
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            int version = pInfo.versionCode;
            //int verCode = pInfo.versionCode;
            String v = valueOf(version);
            tvBuildDate.setText("TPLS Version: " + v);
        }catch(Exception e){
            tvBuildDate.setText("");
        }

        AidcManager.create(this, aidcManager -> {
            manager = aidcManager;
            try {
                barcodeReader = manager.createBarcodeReader();
            } catch (InvalidScannerNameException e) {
                Toast.makeText(LoginActivity.this, "Invalid Scanner Name Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        etName.setOnKeyListener((v, keyCode, event) -> {
            return false;
        });
        etPassword.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        //verifyFromSQLite();
                    default:
                        break;
                }
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (barcodeReader != null) {
            barcodeReader.close();
            barcodeReader = null;
        }
        if (manager != null) {
            manager.close();
        }
    }

    private void initViews() {
        scrollView = findViewById(R.id.nestedScrollView);
        etName = findViewById(R.id.textInputEditName);
        etPassword = findViewById(R.id.textInputEditTextPassword);
        btnLogIn = findViewById(R.id.appCompatButtonLogin);
        lnkRegister = findViewById(R.id.textViewLinkRegister);
        textViewSettings = findViewById(R.id.textViewSettings);
        progBar = findViewById(R.id.progressBar);
        progBar.setVisibility(View.INVISIBLE);
        tvBuildDate = findViewById(R.id.tvBuildDate);
    }

    private void initListeners() {
        btnLogIn.setOnClickListener(this);
        lnkRegister.setOnClickListener(this);
        textViewSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.appCompatButtonLogin) {
            verifyUser();

        } else if (v.getId() == R.id.textViewLinkRegister) {

            if (isSuperAdmin(etName.getText().toString(), etPassword.getText().toString())
                    || noSuperAdmin()) {
                Intent intentRegister = new Intent(getApplicationContext(), signup.class);
                startActivity(intentRegister);
            } else {
                Snackbar.make(scrollView, "Only superadmin can access this!", Snackbar.LENGTH_LONG).show();
            }


        } else if (v.getId() == R.id.textViewSettings) {
            Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intentSettings);
        }
    }

    public void verifyUser() {
        String userid = etName.getText().toString();
        String password = etPassword.getText().toString();
        UserLog user = new UserLog(this);
        user.setUserName(userid);
        user.setPassword(password);
        MessageBox mb = new MessageBox(this);
        if (userid.trim().equals("") || password.trim().equals("")) {
            mb.show("login", "Please type Username and Password");
            return;
        }

        if (!user.isUserValid()) {
            mb.show("login", user.getMsg());
            return;
        }

        singleToneClass sc = com.tlicorporation.triphil.helpers.singleToneClass.getInstance();
        sc.setUser_role(user.getUserRole());
        sc.setUser_id(user.getUserID());
        Intent menuIntent = new Intent(activity, ReferenceNumberActivity.class);
        etName.setText("");
        etPassword.setText("");
        startActivity(menuIntent);
    }

    private boolean noSuperAdmin() {
        //default yes
        boolean rv = true;
        try {
            Connection con = ConnectionClass.CONN(this);
            if (!(con == null)) {
                String query = "SELECT count(*) cnt FROM [tblusers] where user_role = 'superadmin'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    if (!(rs.getInt("cnt") == 0)) {
                        rv = false;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
        return rv;
    }

    private boolean isSuperAdmin(String usrname, String pw) {
        //default false
        boolean rv = false;
        try {
            Connection con = ConnectionClass.CONN(this);
            if (!(con == null)) {
                String query = "select * from tblusers where username = '" + usrname + "' and password = '" + pw + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    if (rs.getString("user_role").equals("superadmin")) {
                        rv = true;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
        return rv;
    }

    static BarcodeReader getBarcodeObject() {
        return barcodeReader;
    }
}

