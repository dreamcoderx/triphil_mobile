package com.tlicorporation.triphil.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.adapters.ScanViewAdapter;
import com.tlicorporation.triphil.model.ScanView;
import com.tlicorporation.triphil.model.Search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CheckboxSelectActivity extends AppCompatActivity
        implements View.OnClickListener  {
    ConnectionClass connectionClass;
    RecyclerView rcv;
    String reference_no;
    String strDelDate;
    Integer intContainerNo;
    SearchView srcVw;
    private ScanViewAdapter mAdapter;
    private List<ScanView> scanList = new ArrayList<>();
    Button delScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox_select);

        rcv = findViewById(R.id.rcvCheckBox);
        srcVw = findViewById(R.id.srcViewScanned);
        delScan = findViewById(R.id.btnDeleteScan);
        delScan.setOnClickListener(this);


        Intent intent = getIntent();
        reference_no = intent.getStringExtra("refno");
        intContainerNo = intent.getIntExtra("containerNo", 1);
        strDelDate = intent.getStringExtra("deliveryDate");

        Search search = new Search();
        mAdapter = new ScanViewAdapter(scanList, this, search );
        rcv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv.setLayoutManager(mLayoutManager);
        rcv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcv.setItemAnimator(new DefaultItemAnimator());
        rcv.setAdapter(mAdapter);

        load_data();

    }

    void load_data(){
        Toast.makeText(CheckboxSelectActivity.this, "loading data...", Toast.LENGTH_SHORT).show();
        try {
            Connection con = connectionClass.CONN(this);
            if (con == null) {
                Toast.makeText(this, "error sql", Toast.LENGTH_LONG).show();
                return;
            }

            String qry = "";
            qry += "select ScanID, RefNo, RowNo, CartonNo, ModelNo, ";
            qry += " Qty, ContainerNo, ScanNo, DeliveryDate";
            qry += " from mob.tblContainerScannedCarton";

            PreparedStatement stmt = con.prepareStatement(qry);
            ResultSet rs = stmt.executeQuery();
            scanList.clear();
            while (rs.next()) {
                ScanView scanVw = new ScanView(rs.getInt("ScanID")
                        , rs.getInt("ContainerNo")
                        , rs.getString("RefNo")
                        , rs.getInt("RowNo")
                        , rs.getString("CartonNo")
                        , rs.getString("ModelNo")
                        , rs.getInt("Qty")
                        , rs.getInt("ScanNo")
                        , rs.getString("DeliveryDate"));
                scanList.add(scanVw);
            }
            mAdapter.notifyDataSetChanged();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDeleteScan) {
            String strSelected = "";
            String strScanID;
            for (int i = 0; i < mAdapter.getItemCount(); i++){
                if (mAdapter.getList().get(i).getSelected()){
                    strScanID =Integer.toString(mAdapter.getList().get(i).getScanID());
                    strSelected = strSelected + " " + strScanID;
                }
            }

            Toast.makeText(this, strSelected, Toast.LENGTH_SHORT).show();
        }
    }
}