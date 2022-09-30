package com.tlicorporation.triphil.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MessageBox
{
    Activity activity;
    private AlertDialog dialog;
    public MessageBox(Activity activity){
        this.activity = activity;
    }
    public void show(String title, String message)
    {
        dialog = new AlertDialog.Builder(activity) // Pass a reference to your main activity here
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialog.cancel();
                    }
                })
                .show();
    }
    public void dispose(){
        this.dispose();
    }

}