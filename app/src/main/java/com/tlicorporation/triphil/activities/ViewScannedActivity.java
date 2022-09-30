package com.tlicorporation.triphil.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.R;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewScannedActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    ListView lstBarcode;
    TextView tvwRefNo;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scanned);
        //lstBarcode = findViewById(R.id.listScannedAll);
        String strRefNo ;
        tvwRefNo = findViewById(R.id.tvwReferenceNo);
        intent = getIntent();
        strRefNo = intent.getStringExtra("refno");
        tvwRefNo.setText(strRefNo);
        load_data(strRefNo);

        lstBarcode.setOnItemClickListener((adapterView, view, i, l) -> {
            //HashMap<String, Object> member = (HashMap<String, Object>) lstBarcode.getItemAtPosition(i);
            TextView scanIDTextView = (TextView) view.findViewById(R.id.tv_rsa_scanid);
            TextView tvBarcode = (TextView) view.findViewById(R.id.tv_rsa_barcode);
            TextView rownoTextView = (TextView) view.findViewById(R.id.tv_rsa_rowno);

            String id = scanIDTextView.getText().toString();
            String barcode = tvBarcode.getText().toString();
            String rowno = rownoTextView.getText().toString();

            Intent in = new Intent(getApplicationContext(), ViewScannedSelectActivity.class);

            //Intent modify_intent = new Intent(ViewScannedActivity.this, ViewScannedSelectActivity.class);
            in.putExtra("id", id);
            in.putExtra("row_no", rowno);
            in.putExtra("barcode", barcode);
            in.putExtra("rowno", rowno);
            in.putExtra("refno", tvwRefNo.getText());
            in.putExtra("deliverydate", "");

            startActivityForResult(in,0);
        });
    }

    void load_data(String refno) {
        try {
            List<Map<String, String>> scanned_list = new ArrayList<>();
            String[] from = {"id", "row_no", "barcode"};
            int[] views = {R.id.tv_rsa_scanid, R.id.tv_rsa_rowno, R.id.tv_rsa_barcode};
            final SimpleAdapter ADA = new SimpleAdapter(ViewScannedActivity.this,
                    scanned_list, R.layout.recyclerview_scanned_all, from,
                    views);
            Connection con = connectionClass.CONN(this);
            if (con == null) {
                Toast.makeText(this, "SQL Server Error!", Toast.LENGTH_LONG).show();
                return;
            }

            String query = "select id, carton, model, qty, row_no, barcode from tblbarcode where ref_no = '" + refno + "' order by id";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> datanum = new HashMap<>();
                datanum.put("id", rs.getString("id"));
                datanum.put("row_no", rs.getString("row_no"));
                datanum.put("barcode", rs.getString("barcode"));

                scanned_list.add(datanum);
            }
            lstBarcode.setAdapter(ADA);

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0)
        {
            load_data(tvwRefNo.getText().toString());
        }
    }
}