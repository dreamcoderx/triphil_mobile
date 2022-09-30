package com.tlicorporation.triphil.activities;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.mssql.ContainerScan;

public class SetMaxRowActivity extends AppCompatActivity {

    TextView tvRowNo;
    EditText etMaxQty;
    Button btnOk, btnCancel;
    String shipDate;
    int containerNo =1;
    int rowNo = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        //context = this;
        setContentView(R.layout.activity_set_max_row);
        this.setFinishOnTouchOutside(true);
        tvRowNo = findViewById(R.id.tvRowNumber);
        etMaxQty = findViewById(R.id.etMaxQty);
        Bundle extras = getIntent().getExtras();
        shipDate = extras.getString("deliveryDate");
        containerNo = extras.getInt("containerNo");
        rowNo = extras.getInt("rowNo");
        String rn = valueOf(rowNo);
        tvRowNo.setText(rn);
        etMaxQty.setText("35");

        btnOk = findViewById(R.id.btnOK);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(v -> {
        try {

            ContainerScan conScan = new ContainerScan(this);
            conScan.setDeliveryDate(shipDate);
            conScan.setContainerNo(containerNo);
            conScan.setCurrentRow(rowNo);
            conScan.setRowMaxQty(Integer.parseInt(etMaxQty.getText().toString()));
            Toast.makeText(this, "Set Max Qty Successful!", Toast.LENGTH_SHORT).show();
            Intent data = new Intent();
            data.putExtra("rowno", rowNo);
            data.putExtra("maxqty",Integer.parseInt(etMaxQty.getText().toString()));
            setResult(Activity.RESULT_OK,data);
            finish();
            return;



        }catch (Exception ex){
            Toast.makeText(this, "btnOk: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        });
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED); // some error ...
            finish();
        });
    }
}