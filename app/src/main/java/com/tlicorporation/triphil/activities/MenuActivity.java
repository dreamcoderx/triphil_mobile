package com.tlicorporation.triphil.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.tlicorporation.triphil.R;

public class MenuActivity
    extends AppCompatActivity
    implements View.OnClickListener{
    private Button appCompatButtonScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        appCompatButtonScan =  findViewById(R.id.btnScan);
        appCompatButtonScan.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnScan:
                Intent intent = new Intent(getApplicationContext(), ReferenceNumberActivity.class);
                startActivity(intent);
                break;
        }
    }
}