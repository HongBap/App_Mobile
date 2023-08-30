package com.example.app_mobile.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.R;
import com.example.app_mobile.Util.InputHandle;
import com.example.app_mobile.Util.NetworkChangeReceiver;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword_EnterEmail extends AppCompatActivity {
    Button btnEnterEmail;
    EditText txtEnterEmail;
    ImageButton btnPrevious;

    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
    private boolean checkSendEmail = false;

    public static int otpCode = -1;
    public static String emailInput = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_enter_email);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnEnterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEnterEmail.getText().toString().trim().toLowerCase();
                if (email.length() == 0) {
                    openDialogEnterEmail(false, "Please enter your Email!");
                    return;
                }
                if (!email.contains("@gmail.com")) {
                    openDialogEnterEmail(false, "Invalid email input!");
                    return;
                }
                LoadingDialog loadingDialog = new LoadingDialog(ForgotPassword_EnterEmail.this);
                loadingDialog.startLoadingDialog();
                sendEmailOTP();
                loadingDialog.closeLoadingDialog();
                if (checkSendEmail) {
                    openDialogEnterEmail(true, "OTP sent successfully!\n Please check your email address");
                } else {
                    openDialogEnterEmail(false, "OTP sending failed!\nPlease check your email address and try again");
                }
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
        btnEnterEmail = findViewById(R.id.btnEnterEmail);
        txtEnterEmail = findViewById(R.id.txtEnterEmail);
        btnPrevious = findViewById(R.id.btnPrevious);
        txtEnterEmail.setFilters(new InputFilter[] {InputHandle.filter});
    }


    public void sendEmailOTP() {
        try {
            String email = txtEnterEmail.getText().toString().trim().toLowerCase();
            String stringSenderEmail = "n19dccn123@student.ptithcm.edu.vn";
            String stringReceiverEmail = email;
            String stringPasswordSenderEmail = "thotnbocbttjwhpw";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            otpCode = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
            mimeMessage.setSubject("Mã xác thực OTP đổi mật khẩu");
            mimeMessage.setText("Chào Quý khách\n" +
                    "\n" +
                    "Quý khách đang thực hiện lấy lại mật khẩu mới trên APP LEZADA SHOP. Để tránh rủi ro, vui lòng không gửi OTP cho bất kỳ ai. Mã OTP: " + otpCode + "\n" + "\nĐể được hỗ trợ thêm, vui lòng liên hệ 1900 1000"
                    + "\n" + "\nCảm ơn Quý khách.");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        checkSendEmail = true;
                        emailInput = email;
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        checkSendEmail = false;
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void openDialogEnterEmail(boolean check, String text_content) {
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
                if (check) {
                    Intent intent = new Intent(ForgotPassword_EnterEmail.this, ForgotPassword_EnterCode.class);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        unregisterReceiver(networkChangeReceiver);
        super.onStop();
    }
}