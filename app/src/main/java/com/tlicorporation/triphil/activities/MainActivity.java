package com.tlicorporation.triphil.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.tlicorporation.triphil.R;
import com.tlicorporation.triphil.sql.DatabaseHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity
        extends AppCompatActivity
        implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private static BarcodeReader barcodeReader;
    private AidcManager manager;
    private DatabaseHelper databaseHelper;
    private static TextInputEditText textInputEditTextLocation;
    private static TextInputEditText textInputEditTextParts;
    private static AppCompatTextView textViewStatus;
    private static String strUser;

    // get bar code instance from MainActivity
    private static void startScan(){

        textInputEditTextLocation.setEnabled(true);
        textInputEditTextParts.setEnabled(true);
        textInputEditTextLocation.setText("");
        textInputEditTextParts.setText("");
        textInputEditTextLocation.requestFocus();


    }
    public void errorTone(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void WriteTextfile(String strKanban, String strLocation, String strActual) {

        String statusResult;
        if (!strActual.substring(0,5).equals(strKanban.substring(0,5))){
            statusResult = "MISMATCH";
            errorTone();
        }else if (!strActual.substring(0,5).equals(strLocation.substring(0,5))){
            statusResult = "MISMATCH" ;
            errorTone();}
        else{
            statusResult = "OK";
        }

        textViewStatus.setText(statusResult);

        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String writeData = strKanban + ", " + strLocation + ", " + strActual + ", " + statusResult + ", " + dateStr + "," + strUser + "\r\n";

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File myFile = new File(dir ,  "scandata.csv");

        if(myFile.exists())
        {
            try
            {
                FileWriter textFileWriter = new FileWriter(myFile, true);
                BufferedWriter out = new BufferedWriter(textFileWriter);
                out.write(writeData);
                out.close();
                Log.d("file create", "File Exists: " + writeData + "/" + myFile.getName());
                textViewStatus.setText(statusResult);
            } catch(Exception e) {
                textViewStatus.setText("FILE UPDATE ERROR!");
                Log.e("file create", "error:" + e.getMessage());
            }
        }
        else
        {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(myFile, true));
                String headerData = "Kanban, Location, Actual, Result, TimeStamp, Username" + "\r\n";
                writeData = headerData + writeData;
                out.write(writeData);
                out.close();
                Log.d("file create", myFile.getName());
            } catch (IOException e) {
                textViewStatus.setText("FILE CREATE ERROR!");
                Log.e("file create", e.getMessage());
            }
        }
    }

    private void initViews() {
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        TextInputLayout textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        TextInputLayout textInputLayoutParts = findViewById(R.id.textInputLayoutParts);
        textInputEditTextLocation = findViewById(R.id.textInputEditTextLocation);
        textInputEditTextParts = findViewById(R.id.textInputEditTextParts);
        textViewStatus = findViewById(R.id.textViewStatus);
        strUser = getIntent().getStringExtra("NAME");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        startScan();
        databaseHelper = new DatabaseHelper(this);
        AidcManager.create(this, aidcManager -> {
            manager = aidcManager;
            try{
                barcodeReader = manager.createBarcodeReader();
            }
            catch (InvalidScannerNameException e){
                Toast.makeText(MainActivity.this, "Invalid Scanner Name Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //barcodeReader = LoginActivity.getBarcodeObject();
        textViewStatus.setText("get barcode object");
        if (barcodeReader != null) {
            // register bar code event listener
            barcodeReader.addBarcodeListener(this);
            // set the trigger mode to client control
            try {
                barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
            } catch (UnsupportedPropertyException e) {
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
            barcodeReader.setProperties(properties);
            textViewStatus.setText("barcode set");
        }
        else{
            textViewStatus.setText("barcode null");
        }

        /*
        textInputEditTextKanban.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //Toast.makeText(getApplicationContext(),KeyEvent.keyCodeToString(keyCode),Toast.LENGTH_SHORT).show();
                    switch (keyCode)
                    {

                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:

                            if (textInputEditTextKanban.getText().length() < 13) {
                                textViewStatus.setText("INVALID KANBAN!");
                                textInputEditTextKanban.setError("INVALID KANBAN!");
                                //hideKeyboard(this);
                                //hideKeyboardFrom(textInputEditTextKanban);
                                errorTone();
                                startScan();
                            }
                            else{
                                textViewStatus.setText("OK KANBAN!");
                                textInputEditTextLocation.setEnabled(true);
                                textInputEditTextLocation.setFocusable(true);
                                textInputEditTextLocation.setFocusableInTouchMode(true);
                                textInputEditTextLocation.requestFocus();

                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

         */

        /*
        textInputEditTextLocation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //Toast.makeText(getApplicationContext(),KeyEvent.keyCodeToString(keyCode),Toast.LENGTH_SHORT).show();
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_TAB:
                            if (textInputEditTextLocation.getText().length() != 5){
                                textViewStatus.setText("INVALID LOCATION!");
                                textInputEditTextLocation.setError("INVALID LOCATION!");
                                errorTone();
                                startScan();
                            }else if (!textInputEditTextLocation.getText().toString().substring(0,5).equals(textInputEditTextKanban.getText().toString().substring(0,5))) {
                                textViewStatus.setText("MISMATCH!");
                                textInputEditTextLocation.setError("MISMATCH!");
                                errorTone();
                                startScan();
                            }
                            else{
                                textViewStatus.setText("OK LOCATION");
                                 textInputEditTextActual.setEnabled(true);
                                textInputEditTextActual.requestFocus();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        textInputEditTextActual.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (textInputEditTextActual.getText().length() >= 13){
                        WriteTextfile(textInputEditTextKanban.getText().toString(), textInputEditTextLocation.getText().toString(), textInputEditTextActual.getText().toString());
                    }
                    else{
                        errorTone();
                        textViewStatus.setText("INVALID ACTUAL!");
                    }
                    startScan();
                }
            }
        });

 */
    }
    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // update UI to reflect the data
                List<String> list = new ArrayList<String>();
                list.add("Barcode data: " + event.getBarcodeData());
                list.add("Character Set: " + event.getCharset());
                list.add("Code ID: " + event.getCodeId());
                list.add("AIM ID: " + event.getAimId());
                list.add("Timestamp: " + event.getTimestamp());
                String barScan = event.getBarcodeData();
                textViewStatus.setText(barScan);
                //textViewStatus.setText(event.getBarcodeData().toString());

                if (textInputEditTextLocation.hasFocus()){
                    textInputEditTextLocation.setText(barScan);
                    if (barScan.length() != 5) {
                        textViewStatus.setText("Invalid Location");
                        errorTone();
                        return;
                    }

                    textInputEditTextParts.requestFocus();
                }else if(textInputEditTextParts.hasFocus()) {
                    textInputEditTextParts.setText(barScan);

                    if (barScan.length() != 13) {
                        textViewStatus.setText("Invalid Parts Barcode");
                        errorTone();
                        return;
                    }
                    if (textInputEditTextLocation.getText().toString().substring(0,5).equals(barScan) ){
                        textViewStatus.setText("MODEL NUMBER MISMATCH");
                        errorTone();
                        //showDialog();
                        return;
                    }
                        textInputEditTextLocation.requestFocus();

                }
                //Toast.makeText(getApplicationContext(),event.getBarcodeData().toString(),Toast.LENGTH_SHORT).show();
                //final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                //        AutomaticBarcodeActivity.this, android.R.layout.simple_list_item_1, list);
                //barcodeList.setAdapter(dataAdapter);
            }
        });
    }
    public void showDialog() {
        final EditText name, password;
        AppCompatButton btnLogIn;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.activity_login);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        name =  dialog.findViewById(R.id.textInputEditName);
        password = dialog.findViewById(R.id.textInputEditTextPassword);
        btnLogIn = dialog.findViewById(R.id.appCompatButtonRegister);
        btnLogIn.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Please Enter Username");
                 }else if(password.getText().toString().isEmpty()) {
                    password.setError("Please Enter Password!");
                }else {
                    if (databaseHelper.checkUser(name.toString().trim(), password.toString().trim())) {
                        if(name.toString().equals("admin")){
                           return;
                        }else{
                            name.setError("only admin have access!");
                        }
                    } else {
                        password.setError("invalid user or password");
                      }
                }
            }
        });
    }
    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
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
        if (barcodeReader != null) {
            // close BarcodeReader to clean up resources.
            barcodeReader.close();
            barcodeReader = null;
        }
        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }
    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {
    }


}