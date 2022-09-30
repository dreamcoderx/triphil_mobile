package com.tlicorporation.triphil.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.tlicorporation.triphil.R;

public class TestActivity123 extends AppCompatActivity {
    int ctr = 1;
    int max = 10;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test123);
        btn = findViewById(R.id.btnTest);
        btn.setOnClickListener(v -> {
            ctr++;
            Toast.makeText(this,String.valueOf(ctr), Toast.LENGTH_SHORT).show();
            if (ctr >= max){
                if (SetMaxRowActivityForResult()){
                    Toast.makeText(this, "finish...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean SetMaxRowActivityForResult() {
        try {
            Intent it=new Intent(this,setmaxtest.class);
            it.putExtra("rowNo", 1);
            it.putExtra("containerNo",2);
            it.putExtra("deliveryDate", 3);
            someActivityResultLauncher.launch(it);
            return true;
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    //Intent data = result.getData();
                    //assert data != null;
                    //CURRENT_ROW_NO = data.getIntExtra("rowno",0);
                    //conSql.setRowMax(data.getIntExtra("maxqty",0));
                    //container.setRowNo(CURRENT_ROW_NO);
                    //conSql.updateRowMax(container);
                    //LoadCounters();
                    //isPauseScanner = false;
                }
            });
}