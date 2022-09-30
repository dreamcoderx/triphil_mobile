package com.tlicorporation.triphil.activities;

import static java.lang.String.valueOf;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.tlicorporation.triphil.R;
public class setmaxtest extends AppCompatActivity {
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
            setResult(Activity.RESULT_OK);
            finish();
        });
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }
}