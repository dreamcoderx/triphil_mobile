package com.tlicorporation.triphil.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.R;
import java.sql.Connection;
import java.sql.Statement;

public class ScanAllSelectActivity
        extends AppCompatActivity
        implements View.OnClickListener{

    private Integer scanID, rowNo;
    String strID, strRowNo, strBarcode, strRefNo;

    private EditText txtBarcode, txtRowNo;
    private Button btnDelete;

    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_all_select);

        setTitle("Select Record");
        txtRowNo = findViewById(R.id.et_sas_rowno);
        txtBarcode = findViewById(R.id.et_sas_barcode);
        btnDelete = findViewById(R.id.btn_sas_delete);

        Intent intent = getIntent();
        strID = intent.getStringExtra("id");
        strRowNo = intent.getStringExtra("rowno");
        strBarcode = intent.getStringExtra("barcode");
        strRefNo = intent.getStringExtra("refno");

        scanID = Integer.parseInt(strID);
        rowNo =Integer.parseInt(strRowNo);

        txtRowNo.setText(strRowNo);
        txtBarcode.setText(strBarcode);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sas_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("DELETE?")
                    .setTitle("DELETE RECORD").setPositiveButton("Yes", dialogClickListener).show();
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    deleteLine(scanID);
                    deleterefrowno(strRefNo, rowNo);

                    String message="hello ";
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE",message);
                    setResult(0,intent);
                    finish();//finishing activity
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // No button clicked
                    // do nothing
                    //Toast.makeText(ScanAllSelectActivity.this, "No Clicked",
                    //        Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    void deleteLine(Integer selid) {
        try {
            Connection con = connectionClass.CONN(this);
            String query = "DELETE FROM [tblbarcode] WHERE id = "  + selid ;

            Statement stmt = con.createStatement();
            stmt.executeQuery(query);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void deleterefrowno(String refno, Integer rowno) {
        try {
            Connection con = connectionClass.CONN(this);
            String query = "DELETE FROM [tblbarcode] WHERE ref_no = '"  + refno + "' and row_no > " + rowno ;
            Statement stmt = con.createStatement();
            stmt.executeQuery(query);
        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }

}