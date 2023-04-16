package com.example.app_mobile.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.Model.UserSignup;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    Boolean checkSignup = false;

    EditText txtUsername, txtEmail, txtPassword, txtRePassword;
    Button btnSignup;

    TextView haveAccount;

    ImageButton btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSignup = false;

                String username = txtUsername.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String rePassword = txtRePassword.getText().toString().trim();

                Log.e("txtUsername", txtUsername.getText().toString());
                Log.e("txtEmail", txtEmail.getText().toString());
                Log.e("txtPassword", txtPassword.getText().toString());
                Log.e("txtUsername", txtRePassword.getText().toString());

                if (username.length() == 0) {
                    openDialogSignup(isSignup, "Please enter username");
                    return;
                }
                if (email.length() == 0) {
                    openDialogSignup(isSignup, "Please enter your email address");
                    return;
                }

                if (password.length() == 0) {
                    openDialogSignup(isSignup, "Please enter password");
                    return;
                }

                if (rePassword.length() == 0) {
                    openDialogSignup(isSignup, "Please re-enter password");
                    return;
                }

                if (!rePassword.equals(password)) {
                    openDialogSignup(isSignup, "Two passwords are not the same");
                    return;
                }

                handleSignupApi();
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
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
        txtUsername = findViewById(R.id.txtUsernameSignup);
        txtEmail = findViewById(R.id.txtEmailSignup);
        txtPassword = findViewById(R.id.txtPasswordSignup);
        txtRePassword = findViewById(R.id.txtRePasswordSignup);
        btnSignup = findViewById(R.id.btnSignup);
        haveAccount = findViewById(R.id.haveAccount);
        btnPrevious = findViewById(R.id.btnPrevious);
    }

    private void handleSignupApi() {
        String u_name = txtUsername.getText().toString().trim().toLowerCase();
        String u_email = txtEmail.getText().toString().trim().toLowerCase();
        String u_password = txtPassword.getText().toString();
        UserSignup user = new UserSignup(0, u_name, u_email, u_password);
        Log.e("user", user.toString());
        Call<ResponseBody> call = ApiService.apiService.postSignup(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String strResponseBody = response.body().string();
                        try {
                            JSONObject messageObject = new JSONObject(strResponseBody);
                            openDialogSignup(true, "Successful account registration!\n" + messageObject.get("message"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
//                        Log.v("TAG", "jsonResponseBody: " + strResponseBody);
                    } else {
                        try {
                            String strResponseBody = response.errorBody().string();
                            JSONObject messageObject = new JSONObject(strResponseBody);
                            openDialogSignup(false, "Account registration failed!\n" + messageObject.get("message"));
//                          Log.v("Error code 400",response.errorBody().string());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    Log.e("ERROR", "message: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void openDialogSignup(boolean isSignup, String text_content) {
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
        if (isSignup) {
            txtDialogContent.setText(text_content);
        } else {
            txtDialogContent.setText(text_content);
        }
        btnDialogOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isSignup) {
                    Intent intent = new Intent(Signup.this, Login.class);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}