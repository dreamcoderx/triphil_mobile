package com.tlicorporation.triphil.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.tlicorporation.triphil.R;

import java.util.HashMap;
import java.util.Map;

public class TestScanner extends AppCompatActivity
        implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener,
        View.OnClickListener {

    private static BarcodeReader barcodeReader;
    private Button btnDisable, btnEnable;
    private TextView tvScanner;
    private boolean pauseScan = false;
    private TextView etBarcode;

    //static BarcodeReader getBarcodeObject() {
    //    return barcodeReader;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scanner);
        btnEnable = findViewById(R.id.btnEnable);
        btnDisable = findViewById(R.id.btnDisable);
        tvScanner = findViewById(R.id.tvBarcode);
        etBarcode = findViewById(R.id.etBarcode);

        btnEnable.setOnClickListener(this);
        btnDisable.setOnClickListener(this);

        tvScanner.setText("on create");

        loadScanner();
        pauseScan = false;

        etBarcode.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!(etBarcode.getText().toString().equals(""))) {
                         Toast.makeText(this, etBarcode.getText().toString(), Toast.LENGTH_SHORT).show();
                     }
                }
                return true;
            }
            return false;
        });



    }



    void showDialog() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    //return;

            }
        };

        //Maximum Qty reached.
        //        Would you like to continue scanning?

        AlertDialog.Builder builder = new AlertDialog.Builder(TestScanner.this);
        builder.setMessage("Maximum Qty reached.Would you like to continue scanning?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }



    void loadScanner() {

        barcodeReader = LoginActivity.getBarcodeObject();
        if (barcodeReader == null) {
            tvScanner.setText("barcode null");

            Log.e("barcode", "barcode  null!");
            return;
        }
        tvScanner.setText("barcode ok");

        Log.e("barcode", "barcode not null!");
        // register bar code event listener
        barcodeReader.addBarcodeListener(this);
        // set the trigger mode to client control
        try {
            barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                    BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
            tvScanner.setText("barcode set settings");


        } catch (UnsupportedPropertyException e) {
            tvScanner.setText("fail to apply");

            Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
        }

        // register trigger state change listener
        barcodeReader.addTriggerListener(this);
        Map<String, Object> properties = new HashMap<>();
        // Set Symbologies On/Off
        properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
        properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
        // Set Max Code 39 barcode length
        properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
        // Turn on center decoding
        properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
        // Enable bad read response
        properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
        // Sets time period for decoder timeout in any mode
        properties.put(BarcodeReader.PROPERTY_DECODER_TIMEOUT, 400);
        // Apply the settings
        barcodeReader.setProperties(properties);
        tvScanner.setText("barcode set");
        Toast.makeText(this, "barcode set", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(() -> {
            try {
                if (pauseScan){
                    return;
                }
                String barcodeData = barcodeReadEvent.getBarcodeData();
                //Toast.makeText(this, barcodeData, Toast.LENGTH_LONG);
                    tvScanner.setText(barcodeData);
                    ScanProcess(barcodeData);
                //list.add("Timestamp: " + barcodeReadEvent.getTimestamp());
                //etBarcodeScan.setText(barcodeData);
                //ScanProcess(barcodeData);
            }catch (Exception ex){
                tvScanner.setText(ex.getMessage());
                //Log.e("SQL", ex.getMessage());
                //messageBox(ex.getMessage());
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnEnable) {
            pauseScan = false;
            //barcodeReader.addBarcodeListener(this);
            //barcodeReader.addTriggerListener(this);

        }else if (v.getId() == R.id.btnDisable) {
            pauseScan = true;
            //barcodeReader.removeBarcodeListener(this);
            //barcodeReader.removeTriggerListener(this);
            /*
            try {
                barcodeReader.light(false);
                Toast.makeText(this, "set light off", Toast.LENGTH_SHORT).show();

            } catch (ScannerNotClaimedException e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            } catch (ScannerUnavailableException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

             */
            //barcodeReader.light(false);
        }
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
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            barcodeReader.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (barcodeReader != null) {
            // unregister barcode event listener
            barcodeReader.removeBarcodeListener(this);
            // unregister trigger state change listener
            barcodeReader.removeTriggerListener(this);
        }
    }

    void ScanProcess(String barcodeScan){
        try{
            String[] barData = barcodeScan.split("/");
            String strCarton = barData[0];
            String strModel = barData[1];
            String strQty = barData[2];

            String[] arrCarton = strCarton.split(":");

            if (arrCarton.length > 1) {
                strCarton = arrCarton[1];
            }

            String[] arrQty = strQty.split(":");

            if (arrQty.length > 1) {
                strQty = arrQty[1];
            }

            String strMsg = "";
            strMsg = "Carton:" + strCarton;
            strMsg += "Model:" + strModel;
            strMsg += "Qty:" + strQty;
            Toast.makeText(this, strMsg, Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            Toast.makeText(this, "barcode error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //messageBox(ex.getMessage());
        }
    }


}