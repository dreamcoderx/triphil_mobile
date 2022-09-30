package com.tlicorporation.triphil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.tlicorporation.triphil.ConnectionClass;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.helpers.singleToneClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReferenceNumberActivity
        extends AppCompatActivity
        implements View.OnClickListener{
    private Button btnScan, btnViewScan;
    private Button btnLockUnlock;
    private Button btnSelectDate;
    private TextView etRefNo;
    private TextView etShipDate;
    private TextView etCustomer;
    private Spinner spnrContainerNo;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_number);
        connectionClass = new ConnectionClass();

        btnLockUnlock = findViewById(R.id.btnLockUnlock);
        btnLockUnlock.setEnabled(false);

        btnScan = findViewById(R.id.buttonScan);
        btnScan.setEnabled(false);
        btnViewScan = findViewById(R.id.buttonViewScan);
        btnViewScan.setEnabled(false);

        btnSelectDate = findViewById(R.id.btnSelectDate);

        etRefNo = findViewById(R.id.etRefNo);
        etRefNo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        if (validRefno(etRefNo.getText().toString())){
                            refNoLockUnlock(etRefNo.getText().toString());
                        }
                    default:
                        break;
                    }
                }
                return false;
            }
        });
        etShipDate = findViewById(R.id.etShipDate);
        etCustomer = findViewById(R.id.etCustomer);
        spnrContainerNo = findViewById(R.id.spnrRowNo);
        btnScan.setOnClickListener(this);
        btnViewScan.setOnClickListener(this);
        btnLockUnlock.setOnClickListener(this);
        btnSelectDate.setOnClickListener(this);

        Integer[] items = new Integer[]{1,2,3,4};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spnrContainerNo.setAdapter(adapter);

    }

    Boolean validRefno(String refno){
        Boolean rv = false;
        Connection con = connectionClass.CONN(this);
        if (con == null) {
            Log.e("aab", "Error in connection with SQL server");
        } else {
            String query = "select * from sms.tbl_PackingListInfo where RefNo = '" + refno  + "'";
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    rv = false;
                }else{
                    etShipDate.setText(rs.getString("Shipdate").substring(0, 10));
                    etCustomer.setText(rs.getString("Customer"));
                    btnScan.setEnabled(true);
                    btnViewScan.setEnabled(true);
                    rv = true;

                }
            } catch (SQLException throwables) {
                Toast.makeText(ReferenceNumberActivity.this, "Exception: " + throwables.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return rv;
    }

    private void refNoLockUnlock(String refno){

        Connection con = connectionClass.CONN(this);
        if (con == null) {
            Log.e("aab", "Error in connection with SQL server");
        } else {
            String query = "SELECT [ref_no],[Shipdate] ,[Customer],[IsLock],[dtCreated],[dtUpdated],[CreatedBy],[LastUpdateBy]" +
                            " FROM [tblBarcodeInfo]" +
                            " where ref_no = '" + refno  + "'";
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    btnLockUnlock.setEnabled(true);
                    if (rs.getInt("IsLock")==1) {
                        btnLockUnlock.setText("UNLOCK");
                        btnScan.setEnabled(false);
                    }else{
                        btnLockUnlock.setText("LOCK");
                        btnScan.setEnabled(true);
                    }
                }else{
                    insertSQL();
                    btnLockUnlock.setEnabled(true);
                    btnLockUnlock.setText("LOCK");

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void setLockUnlock(String refno, Integer lock){

        Connection con = connectionClass.CONN(this);
        if (con == null) {
            Log.e("aab", "Error in connection with SQL server");
        } else {
            String query ="UPDATE [tblBarcodeInfo]" +
                    " SET [IsLock] =  "+ lock +"" +
                    " where ref_no = '" + refno  + "'";
            Log.e("aab", query);
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(query);
                ps.execute();
                Log.e("aab", query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Log.e("aab", throwables.getMessage());
            }
        }
    }

    private void insertSQL(){
        Connection con = connectionClass.CONN(this);
        if (con == null) {
            Log.e("aab", "Error in connection with SQL server");
        } else {
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            singleToneClass singleToneClass = com.tlicorporation.triphil.helpers.singleToneClass.getInstance();
            Integer userid = singleToneClass.getUser_id();
            String query = "INSERT INTO [tblBarcodeInfo]" +
                    "([ref_no]" +
                    ",[Shipdate]" +
                    ",[Customer]" +
                    ",[IsLock]" +
                    ",[dtCreated]" +
                    ",[dtUpdated]" +
                    ",[CreatedBy]" +
                    ",[LastUpdateBy])" +
                    "VALUES" +
                    "('" + etRefNo.getText().toString()  +"'" +
                    ", '" + etShipDate.getText().toString() + "'" +
                    ", '" + etCustomer.getText().toString() +"'" +
                    ", 0 " +
                    ",'" + mydate +"'" +
                    ", '"+ mydate +"'" +
                    ", "+ userid +"" +
                    ", "+ userid + ")";
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(query);
                ps.executeQuery();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void Cleartxtbox(){
        etRefNo.setText("");
        etShipDate.setText("");
        etCustomer.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectDate:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(year , monthOfYear , dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String datestring=format.format(c.getTime());
                                etShipDate.setText(datestring);
                                btnViewScan.setEnabled(true);
                            }
                        }, year, month, day);
                picker.show();


            case R.id.buttonScan:
                String refNo = etRefNo.getText().toString().trim();

                if (refNo.trim().equals("")) {
                    Toast.makeText(this, "Reference Number can not be blank!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validRefno(refNo)) {
                    Toast.makeText(this, "Reference Number not found!", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intentScan = new Intent(getApplicationContext(), ScanActivity.class);
                intentScan.putExtra("refNo", refNo);

                int value = (Integer) spnrContainerNo.getSelectedItem();

                intentScan.putExtra("containerNo", value);
                intentScan.putExtra("deliveryDate", etShipDate.getText().toString());
                Cleartxtbox();
                startActivity(intentScan);
                resetform();

                break;

            case R.id.buttonViewScan:
                Intent intent = new Intent(getApplicationContext(), ScanViewActivity.class);
                intent.putExtra("refno",etRefNo.getText().toString() );

                int valSpnr = (Integer) spnrContainerNo.getSelectedItem();
                intent.putExtra("containerNo", valSpnr);
                intent.putExtra("deliveryDate", etShipDate.getText().toString());
                //startActivityForResult(intent,0) ;
                startActivity(intent);
                break;

            case R.id.btnLockUnlock:
                Log.e("aab", "click button lock");
                if (btnLockUnlock.getText().toString().equals("LOCK")){
                    setLockUnlock(etRefNo.getText().toString(), 1);
                    btnLockUnlock.setText("UNLOCK");
                }else{
                    setLockUnlock(etRefNo.getText().toString(), 0);
                    btnLockUnlock.setText("LOCK");
                }
                refNoLockUnlock(etRefNo.getText().toString());
                break;

        }
    }

    void resetform(){
        btnScan.setEnabled(false);
        btnViewScan.setEnabled(false);
        btnLockUnlock.setEnabled(false);
    }

}