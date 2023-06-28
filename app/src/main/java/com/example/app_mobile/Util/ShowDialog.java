package com.example.app_mobile_phone.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.app_mobile_phone.R;

public class ShowDialog {
    private boolean isSuccess;
    private String text_content;
    private Context currentContext;
    private Class<?> newClass;

    private boolean showSuccessNotify;

    public ShowDialog(Dialog dialog, boolean isSuccess, String text_content, Context currentContext, Class<?> newClass, boolean showSuccessNotify){
        this.isSuccess = isSuccess;
        this.text_content = text_content;
        this.currentContext = currentContext;
        this.newClass = newClass;
        this.showSuccessNotify = showSuccessNotify;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_message);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);

        Button btnDialogOke = dialog.findViewById(R.id.btnDialogOke);
        TextView txtDialogContent = dialog.findViewById(R.id.txtDialogContent);
        if (isSuccess) {
            txtDialogContent.setText(this.text_content);
        } else {
            txtDialogContent.setText(this.text_content);
        }

        btnDialogOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSuccess && showSuccessNotify) {
                    Intent intent = new Intent(currentContext, newClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    currentContext.startActivity(intent);
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public ShowDialog(Dialog dialog, String text_content, Context currentContext){
        this.text_content = text_content;
        this.currentContext = currentContext;
    }

    public Dialog CreateDialog(Dialog dialog){
        dialog.setCancelable(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_message);

        Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);

        Button btnDialogOke = dialog.findViewById(R.id.btnDialogOke);
        TextView txtDialogContent = dialog.findViewById(R.id.txtDialogContent);
        txtDialogContent.setText(this.text_content);

        btnDialogOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) currentContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null){
                    dialog.dismiss();
                    dialog.show();
                }else{
                    dialog.dismiss();
                }
            }
        });
//        dialog.show();
        return dialog;
    }
}
