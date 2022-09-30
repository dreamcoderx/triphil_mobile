package com.tlicorporation.triphil.activities;

import android.app.Activity;
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
import java.sql.PreparedStatement;

public class ScanSelectActivity extends Activity implements View.OnClickListener {

    ConnectionClass connectionClass;

    int scanID, rowNo;
    //private String refNo;
    //String _del_date;
    // Integer _scanNo;

    private EditText etCarton;
    private EditText etModel;
    private EditText etQty;

    private Button deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_select);

        setTitle("Select Record");
        etCarton = findViewById(R.id.et_ss_carton);
        etModel = findViewById(R.id.et_ss_model);
        etQty = findViewById(R.id.et_ss_qty);

        deleteBtn = findViewById(R.id.btn_delete_line);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        String carton = intent.getStringExtra("carton");
        String model = intent.getStringExtra("model");
        String qty = intent.getStringExtra("qty");

        String rowno =intent.getStringExtra("rowno");
        //String refno =intent.getStringExtra("refno");

        scanID = Integer.parseInt(id);
        rowNo =Integer.parseInt(rowno);
        //refNo = refno;

        etCarton.setText(carton);
        etModel.setText(model);
        etQty.setText(qty);
        deleteBtn.setOnClickListener( this);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_delete_line){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("DELETE?").setTitle("DELETE RECORD").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Yes button clicked
                    Toast.makeText(ScanSelectActivity.this, "Yes Clicked",
                            Toast.LENGTH_LONG).show();
                    //deleteline(_)
                    //deleterowgreater(_refno, _row);

                    String message="hello ";
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE",message);
                    setResult(0,intent);
                    finish();//finishing activity

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // No button clicked
                    // do nothing
                    //Toast.makeText(ScanSelectActivity.this, "No Clicked",
                    //        Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    void deleteline(String deldate, Integer containerno, Integer scanNo) {
        try {
            Connection con = connectionClass.CONN(this);

            String query = "DELETE FRO[mob].[tblContainerScannedCarton]  " +
                        " where ScanNo = ?" +
                        " and DeliveryDate = ?" +
                        " and ContainerNo = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1,scanNo);
            stmt.setString(2, deldate);
            stmt.setInt(3, containerno);
            stmt.executeQuery();

        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }

    void deleterowgreater(String deldate, Integer containerno, Integer RowNo) {
        try {
            Connection con = connectionClass.CONN(this);
            //String query = "DELETE FROM [tblbarcode] WHERE ref_no = '"  + refno + "' and row_no > " + rowno   ;
            String query = "DELETE FRO[mob].[tblContainerScannedCarton]  " +
                    " where RowNo > ?" +
                    " and DeliveryDate = ?" +
                    " and ContainerNo = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1,RowNo);
            stmt.setString(2, deldate);
            stmt.setInt(3, containerno);
            stmt.executeQuery();
        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }


}