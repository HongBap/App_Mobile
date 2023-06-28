package com.example.app_mobile_phone.Util;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static boolean isConnected = false;
    public Dialog dialog = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            dialog = new Dialog(context);
            isConnected = false;
            dialog = new ShowDialog(dialog,"Lỗi kết nối mạng! Vui lòng thử lại.", context).CreateDialog(dialog);
            dialog.show();
        }else{
            if(dialog != null){
                dialog.dismiss();
            }
            isConnected = true;
        }
    }
}
