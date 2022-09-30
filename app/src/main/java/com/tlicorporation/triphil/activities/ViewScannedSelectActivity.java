package com.tlicorporation.triphil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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


public class ViewScannedSelectActivity extends AppCompatActivity implements View.OnClickListener{
    //ConnectionClass connectionClass;
    Integer _id, _rowno, _scanNo, _containerno, _carton_no;
    String _deliveryDate;
    String _refno;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scanned_select);
        setTitle("Select Record");
        EditText etCarton = findViewById(R.id.etCarton);
        EditText etModel = findViewById(R.id.etModel);
        EditText etQty = findViewById(R.id.etQty);
        context = this;

        Button deleteBtn = findViewById(R.id.btn_vss_delete);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ScanID");
        String rowno = intent.getStringExtra("RowNo");
        String scan_no = intent.getStringExtra("ScanNo");
        String container_no = intent.getStringExtra("ContainerNo");

        etCarton.setText(intent.getStringExtra("CartonNo"));
        etModel.setText(intent.getStringExtra("ModelNo"));
        etQty.setText(intent.getStringExtra("Qty"));

        _refno = intent.getStringExtra("RefNo");
        _id = Integer.parseInt(id);
        _rowno = Integer.parseInt(rowno);
        _scanNo = Integer.parseInt(scan_no);
        _deliveryDate = intent.getStringExtra("DeliveryDate");
        _containerno = Integer.parseInt(container_no);
        _carton_no = Integer.parseInt(intent.getStringExtra("CartonNo"));



        deleteBtn.setOnClickListener(ViewScannedSelectActivity.this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_vss_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("DELETE?")
                    .setTitle("DELETE RECORD").setPositiveButton("Yes", dialogClickListener).show();
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            Intent intent=new Intent();
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(context, "deleting", Toast.LENGTH_SHORT).show();
                    deleteline(_deliveryDate, _containerno, _carton_no, _refno);
                    deleterowgreater(_deliveryDate,_containerno, _rowno);
                    setResult(Activity.RESULT_OK,intent);
                    finish();//finishing activity
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    setResult(Activity.RESULT_CANCELED,intent);
                    break;
            }
        }
    };

    void deleteline(String deldate, Integer containerno, Integer carton_no, String ref_no) {
        try {
            Connection con = ConnectionClass.CONN(this);

            String qryIns = "INSERT INTO [mob].[tblContainerScannedCartonDelete]" +
                    " ([ScanID],[ScanNo],[RowNo],[DeliveryDate],[ContainerNo]" +
                    " ,[RefNo],[CartonNo],[ModelNo],[Qty]" +
                    " ,[ScannedBy],[ScannedDT],[DeletedBy],[DeletedDT])" +
                    " SELECT" +
                    " [ScanID],[ScanNo],[RowNo],[DeliveryDate],[ContainerNo]" +
                    " ,[RefNo],[CartonNo],[ModelNo],[Qty]" +
                    " ,[ScannedBy],[ScannedDT], ? ,? " +
                    " FROM [mob].[tblContainerScannedCarton]" +
                    " where RefNo = ?" +
                    " and DeliveryDate = ?" +
                    " and ContainerNo = ? and CartonNo = ?";

            assert con != null;
            PreparedStatement ps = con.prepareStatement(qryIns);
            ps.setString(1, "user");
            ps.setString(2, "01-01-01");
            ps.setString(3, ref_no);
            ps.setString(4, deldate);
            ps.setInt(5, containerno);
            ps.setInt(6, carton_no);
            ps.executeQuery();

            String query = "DELETE FROM [mob].[tblContainerScannedCarton]  " +
                    " where RefNo = ?" +
                    " and DeliveryDate = ?" +
                    " and ContainerNo = ? and CartonNo = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, ref_no);
            stmt.setString(2, deldate);
            stmt.setInt(3, containerno);
            stmt.setInt(4, carton_no);
            Log.e("sql", query);
            stmt.executeQuery();


        }
        catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }

    void deleterowgreater(String deldate, Integer containerno, Integer RowNo) {
        try {
            Connection con = ConnectionClass.CONN(this);

            String qryIns = "INSERT INTO [mob].[tblContainerScannedCartonDelete]" +
                    " ([ScanID],[ScanNo],[RowNo],[DeliveryDate],[ContainerNo]" +
                    " ,[RefNo],[CartonNo],[ModelNo],[Qty]" +
                    " ,[ScannedBy],[ScannedDT],[DeletedBy],[DeletedDT])" +
                    " SELECT" +
                    " [ScanID],[ScanNo],[RowNo],[DeliveryDate],[ContainerNo]" +
                    " ,[RefNo],[CartonNo],[ModelNo],[Qty]" +
                    " ,[ScannedBy],[ScannedDT], ? ,? " +
                    "FROM [mob].[tblContainerScannedCarton]  " +
                    " where RowNo > ?" +
                    " and DeliveryDate = ?" +
                    " and ContainerNo = ?";

            assert con != null;
            PreparedStatement ps = con.prepareStatement(qryIns);
            ps.setString(1, "user");
            ps.setString(2, "01-01-01");
            ps.setInt(3,RowNo);
            ps.setString(4, deldate);
            ps.setInt(5, containerno);
            ps.executeQuery();
            ps.executeQuery();

            String query = "DELETE " +
                    "FROM [mob].[tblContainerScannedCarton]  " +
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