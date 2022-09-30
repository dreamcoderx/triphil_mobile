package com.tlicorporation.triphil.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Objects;

public class ScanViewActivity
        extends AppCompatActivity {
    String DELDATE = "01/01/2022";
    Integer CONTAINERNO = 1;
    String REFNO = "";
    ConnectionClass conClass;
    RecyclerView recVw;
    SearchView srcVw;
    Spinner spinSearch;
    TextView tvNoDataFound;
    Search search = new Search();

    private ScanViewAdapter mAdapter;
    private List<ScanView> scanList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_view);
        recVw = findViewById(R.id.recVwScannedBox);
        srcVw = findViewById(R.id.srcViewScanned);
        spinSearch = findViewById(R.id.spnrSearch);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);
        tvNoDataFound.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        REFNO = intent.getStringExtra("refNo");
        CONTAINERNO = intent.getIntExtra("containerNo", 1);
        DELDATE = intent.getStringExtra("deliveryDate");

        search.setDeliveryDate(DELDATE );
        search.setContainerNo(CONTAINERNO);

        srcVw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String strSearchBy;
                strSearchBy = spinSearch.getSelectedItem().toString();

                if (strSearchBy.equals("Carton") && !(s == "")){
                    try {
                        int number = Integer.parseInt(s);
                        search.setCartonNo(number);
                        search.setModelNo("");

                    } catch (NumberFormatException ex) {
                        Toast.makeText(ScanViewActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if (strSearchBy.equals("Model") && !(s == "")){
                    search.setModelNo(s);
                    search.setCartonNo(0);
                }

                loadData(search);
                loadRecyclerView();
            return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                    return false;
            }
        });
    }

    private void loadRecyclerView(){
        mAdapter = new ScanViewAdapter(scanList, this, search);
        recVw.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recVw.setLayoutManager(mLayoutManager);
        recVw.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recVw.setItemAnimator(new DefaultItemAnimator());
        recVw.setAdapter(mAdapter);

    }


    private String sqlWhereConcat(String s, String whereAdd){
        if (s.isEmpty()){
            return whereAdd;
        }
        return s + " and " + whereAdd;
    }

    void loadData(Search search){
        Toast.makeText(ScanViewActivity.this, "loading data...", Toast.LENGTH_LONG).show();
        try {
            Connection con = conClass.CONN(this);
            if (con == null) {
                Toast.makeText(this, "sql error", Toast.LENGTH_LONG).show();
                Log.e("error", "Error in connection with SQL server");
                return;
            }
            String sqlWhere = "";
            if (!(search.getDeliveryDate()== null || Objects.equals(search.getDeliveryDate(), ""))){
                sqlWhere = sqlWhereConcat(sqlWhere, " DeliveryDate = '" + search.getDeliveryDate() +  "'");
            }
            if (search.getContainerNo() != 0){
                sqlWhere = sqlWhereConcat(sqlWhere, " ContainerNo = '" + search.getContainerNo() + "'");
            }
            if (search.getCartonNo() != 0){
                sqlWhere = sqlWhereConcat(sqlWhere, " CartonNo = '" + search.getCartonNo() + "'");
            }
            if (!(search.getModelNo() == null || search.getModelNo() == "")){
                sqlWhere = sqlWhereConcat(sqlWhere, " ModelNo = '" + search.getModelNo() + "'");
            }
            String query = "select ScanID, RefNo, RowNo, CartonNo, ModelNo, Qty, " +
                    " ContainerNo, ScanNo, DeliveryDate" +
                    " from mob.tblContainerScannedCarton" ;
            if (!(sqlWhere=="")){
                query += " WHERE " + sqlWhere;
            }

            query += " ORDER BY RowNo";
            PreparedStatement stmt = con.prepareStatement(query);
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
            Toast.makeText(ScanViewActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}