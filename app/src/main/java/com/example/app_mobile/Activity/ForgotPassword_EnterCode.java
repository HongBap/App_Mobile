package com.example.app_mobile.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.R;

public class ForgotPassword_EnterCode extends AppCompatActivity {

    Button btnEnterCode;
    EditText txtEnterCode;

    ImageButton btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_enter_code);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnEnterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = txtEnterCode.getText().toString().trim().toLowerCase();
                if (code.length() == 0) {
                    openDialogEnterCode("Vui lòng nhập mã xác minh!");
                    return;
                }
                if (!code.equals(String.valueOf(ForgotPassword_EnterEmail.otpCode))) {
                    openDialogEnterCode("Mã xác minh không chính xác, vui lòng kiểm tra lại!");
                    return;
                }

                Intent intent = new Intent(ForgotPassword_EnterCode.this, ForgotPassword_SetPass.class);
                startActivity(intent);
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addControls() {
        btnEnterCode = findViewById(R.id.btnEnterCode);
        txtEnterCode = findViewById(R.id.txtEnterCode);
        btnPrevious = findViewById(R.id.btnPrevious);
    }


    public void openDialogEnterCode(String text_content) {
        final Dialog dialog = new Dialog(this);
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
        txtDialogContent.setText(text_content);
        btnDialogOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}