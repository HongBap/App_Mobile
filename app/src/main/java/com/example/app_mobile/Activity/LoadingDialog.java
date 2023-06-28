package com.example.app_mobile_phone.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.app_mobile_phone.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;
    LoadingDialog(Activity myActivity){
        activity = myActivity;

    }

    void startLoadingDialog(){
        AlertDialog.Builder builder =new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    void closeLoadingDialog(){
        dialog.dismiss();
    }
}
