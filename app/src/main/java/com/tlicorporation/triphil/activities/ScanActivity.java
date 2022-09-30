package com.tlicorporation.triphil.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.adapters.ScannedVwAdapt;
import com.tlicorporation.triphil.model.Barcode;
import com.tlicorporation.triphil.mssql.ContainerScan;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Vibrator;
public class ScanActivity
        extends AppCompatActivity
        implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {

    private static BarcodeReader barcodeReader;

    EditText etBcScan;
    Button btnNewRow, btnDetails, btnScanned;
    ProgressBar pbScan;
    RecyclerView recView;

    private boolean isPauseScanner = false;
    ListView dataList;
    ContainerScan conScan;
    private ScannedVwAdapt mAdapter;

    String lblCtrCarQtyRow = "CARTON QTY/ROW : ";
    String lblCtrCartons = "TOTAL CARTONS : ";
    String lblCtrGloves = "TOTAL GLOVES : ";

    String strCarton, strModel, strQty;
    int selectID, selRowNo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        etBcScan = findViewById(R.id.etBarcodeScan);
        recView = findViewById(R.id.recycler_view);
        btnNewRow = findViewById(R.id.btnnewrow);
        btnDetails = findViewById(R.id.btnDetails);
        btnScanned = findViewById(R.id.btnScanned);
        pbScan = findViewById(R.id.pbScan);
        dataList = findViewById(R.id.listViewData);

        this.setFinishOnTouchOutside(true);
        Intent intent = getIntent();

        conScan = new ContainerScan(this);
        conScan.setScannedBy("mobile");
        try {
            conScan.getTotalGlovesCartonsQty();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        conScan.setReferenceNo(intent.getStringExtra("refNo"));
        conScan.setDeliveryDate(intent.getStringExtra("deliveryDate"));
        conScan.setContainerNo(intent.getIntExtra("containerNo", 1));
        etBcScan.setText("");
        pbScan.setVisibility(View.INVISIBLE);
        dataList.setVisibility(View.VISIBLE);
        recView.setVisibility(View.INVISIBLE);
        selectID = 0;
        selRowNo = 0;

        if (!conScan.GetLastScanRowNo()){
            Toast.makeText(this, "fail to get server current row!", Toast.LENGTH_SHORT).show();
            return;
        }

        SetMaxRowActivityForResult(conScan.getCurrentRow());

        isPauseScanner = false;
        loadScanner();
        loadCounters();
        if (conScan.isMaxReached()){
            setMaxQty();
        }
        btnDetails.setOnClickListener(v -> {
            dataList.setVisibility(View.VISIBLE);
            recView.setVisibility(View.INVISIBLE);
            loadCounters();
        });

        btnScanned.setOnClickListener(v -> {
            dataList.setVisibility(View.INVISIBLE);
            recView.setVisibility(View.VISIBLE);
            LoadRecViewScanned();
        });

        btnNewRow.setOnClickListener(v -> {
            conScan.incrementRowNo();
            SetMaxRowActivityForResult(conScan.getCurrentRow());
            loadCounters();
        });

        etBcScan.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_TAB:
                        //ScanProcess(etBcScan.getText().toString());
                    default:
                        break;
                }
            }
            return false;
        });
    }


    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(() -> {
            try {
                if (isPauseScanner) {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                    Toast.makeText(this, "scanner is paused...", Toast.LENGTH_LONG).show();
                }else{
                    String barcodeData = barcodeReadEvent.getBarcodeData();
                    etBcScan.setText(barcodeData);
                    ScanProcess(barcodeData);
                }

            }catch (Exception ex){
                Toast.makeText(this, "barcode error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void messageBox(String msg){
        isPauseScanner = true;
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE){
                isPauseScanner = false;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setMessage(msg).setPositiveButton("OK", dialogClickListener);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    void ScanProcess(String barcodeScan){
       try{
            String[] barData = barcodeScan.split("/");
            strCarton = barData[0];
            strModel = barData[1];
            strQty = barData[2];
            String[] arrCarton = strCarton.split(":");
            if (arrCarton.length > 1) {
                strCarton = arrCarton[1];
            }
            String[] arrQty = strQty.split(":");
            if (arrQty.length > 1) {
                strQty = arrQty[1];
            }
           Barcode bc = new Barcode();
           bc.setModel(strModel);
           bc.setQty(Integer.parseInt(strQty));
           bc.setCartonNo(Integer.parseInt(strCarton));
           bc.setRefNo(conScan.getReferenceNo());

           if (strModel.equals("MIXED MODEL")){
               conScan.SaveMixedModel(bc);
           }

           if (!strModel.equals("MIXED MODEL")){
               conScan.SaveDataSQL(bc);
           }

           loadCounters();
           if (conScan.isMaxReached()){
               setMaxQty();
           }
           Toast.makeText(this, conScan.getMessage(), Toast.LENGTH_SHORT).show();

       }catch (Exception ex){
           Toast.makeText(this, "barcode error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
       }
    }


    void setMaxQty(){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    conScan.incrementRowMax();
                    loadCounters();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    conScan.incrementRowNo();
                    SetMaxRowActivityForResult(conScan.getCurrentRow());
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setMessage("Maximum Qty reached.Would you like to continue scanning?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    void loadCounters() {
        try{
            int glovesCount;
            int ctrCartons;

            glovesCount = conScan.getCtrGloves();
            ctrCartons = conScan.getCtrCartons();
            conScan.setCounter(conScan.getCounter());

            conScan.getTotalGlovesCartonsQty();
            String strCtrCartons = lblCtrCartons + (ctrCartons) + " / " + conScan.getTotCartonQty();
            String strCtrGloves = lblCtrGloves + (glovesCount) + " / " + conScan.getTotGlovesQty();
            String strCtrCarQtyRow = lblCtrCarQtyRow + conScan.getCounter() + " / " +  conScan.getRowMaxQty();
            List<String> list = new ArrayList<>();
            list.add("ROW NO: " + conScan.getCurrentRow() );
            list.add(strCtrCarQtyRow);
            list.add("CARTON QTY/CONTAINER: " + conScan.getContainerQty() );
            list.add(strCtrCartons);
            list.add(strCtrGloves);
            list.add("REFERENCE NUMBER: " + conScan.getReferenceNo());
            list.add("DELIVERY DATE: " + conScan.getDeliveryDate());
            list.add("CONTAINER NO: " + conScan.getContainerNo() );
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    ScanActivity.this, android.R.layout.simple_list_item_1, list);
            dataList.setAdapter(dataAdapter);
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        //auto generated
    }
    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {
        //auto generated
    }
    @Override
    public void onResume() {
        super.onResume();
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader != null) {
            barcodeReader.release();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (barcodeReader != null) {
            barcodeReader.removeBarcodeListener(this);
            barcodeReader.removeTriggerListener(this);
        }
    }
    void LoadRecViewScanned(){
        try {
            Toast.makeText(this, "Loading Scanned Items...", Toast.LENGTH_LONG).show();
            mAdapter = new ScannedVwAdapt(conScan.getScannedList());
            recView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recView.setLayoutManager(mLayoutManager);
            recView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recView.setItemAnimator(new DefaultItemAnimator());
            recView.setAdapter(mAdapter);
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void loadScanner() {
        barcodeReader = LoginActivity.getBarcodeObject();
        if (barcodeReader == null) {
            Toast.makeText(this, "error: barcode null", Toast.LENGTH_LONG).show();
            return;
        }
        // register bar code event listener
        barcodeReader.addBarcodeListener(this);
        // set the trigger mode to client control
        try {
            barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                    BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
        } catch (UnsupportedPropertyException e) {
            Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
        }
        barcodeReader.addTriggerListener(this);
        Map<String, Object> properties = new HashMap<>();
        properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
        properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
        properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
        properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_DECODER_TIMEOUT, 400);
        barcodeReader.setProperties(properties);
        Toast.makeText(this, "barcode set", Toast.LENGTH_SHORT).show();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    int maxQty, rowNo;

                    rowNo = data.getIntExtra("rowno",0);
                    maxQty = data.getIntExtra("maxqty",0);
                    //conScan.setRowMaxDb(maxQty);
                    conScan.setRowMaxQty(maxQty);
                    conScan.setCurrentRow(rowNo);
                    conScan.updateRowMax();
                    loadCounters();
                    isPauseScanner = false;
                }
            });

    public void SetMaxRowActivityForResult(int RowNo) {
        isPauseScanner = true;
        Intent it=new Intent(this,SetMaxRowActivity.class);
        it.putExtra("rowNo", RowNo);
        it.putExtra("containerNo", conScan.getContainerNo());
        it.putExtra("deliveryDate", conScan.getDeliveryDate());
        someActivityResultLauncher.launch(it);
    }
}