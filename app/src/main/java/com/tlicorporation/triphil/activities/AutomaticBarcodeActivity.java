package com.tlicorporation.triphil.activities;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.tlicorporation.triphil.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomaticBarcodeActivity extends AppCompatActivity
        implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {
    private BarcodeReader br;
    private ListView barList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.);
        setContentView(R.layout.activity_barcode);

        if(Build.MODEL.startsWith("VM1A")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // get bar code instance from MainActivity
       // barcodeReader = LoginActivity.getBarcodeObject();

        if (br != null) {
            // register bar code event listener
            br.addBarcodeListener(this);
            // set the trigger mode to client control
            try {
                br.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
            } catch (UnsupportedPropertyException e) {
                Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
            }
            // register trigger state change listener
            br.addTriggerListener(this);

            Map<String, Object> properties = new HashMap<>();
            // Set Symbologies On/Off
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
            // Set Max Code 39 barcode length
            properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
            // Turn on center decoding
            properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
            // Enable bad read response
            properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
            // Sets time period for decoder timeout in any mode
            properties.put(BarcodeReader.PROPERTY_DECODER_TIMEOUT,  400);
            // Apply the settings
            br.setProperties(properties);
            Toast.makeText(this, "barcode set", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "barcode null", Toast.LENGTH_SHORT).show();
        }

        // get initial list
        barList = findViewById(R.id.listViewData);
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {
        runOnUiThread(() -> {
            // update UI to reflect the data
            List<String> list = new ArrayList<>();
            list.add("Barcode data: " + event.getBarcodeData());
            list.add("Character Set: " + event.getCharset());
            list.add("Code ID: " + event.getCodeId());
            list.add("AIM ID: " + event.getAimId());
            list.add("Timestamp: " + event.getTimestamp());
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    AutomaticBarcodeActivity.this, android.R.layout.simple_list_item_1, list);

            barList.setAdapter(dataAdapter);
        });
    }

    // When using Automatic Trigger control do not need to implement the
    // onTriggerEvent function
    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResume() {
        super.onResume();
        if (br != null) {
            try {
                br.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (br != null) {
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            br.release();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (br != null) {
            // unregister barcode event listener
            br.removeBarcodeListener(this);
            // unregister trigger state change listener
            br.removeTriggerListener(this);
        }
    }
}
