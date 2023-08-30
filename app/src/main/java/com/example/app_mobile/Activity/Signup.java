package com.example.app_mobile.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.Model.UserSignup;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;
import com.example.app_mobile.Util.InputHandle;
import com.example.app_mobile.Util.NetworkChangeReceiver;
import com.example.app_mobile.Util.ShowDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    Boolean checkSignup = false;

    EditText txtUsername, txtEmail, txtPassword, txtRePassword;
    Button btnSignup;

    TextView haveAccount;

    ImageButton btnPrevious;

    boolean passwordVisible;
    boolean rePasswordVisible;

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
                    Dialog dialog = new Dialog(Signup.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please enter username", Signup.this, Login.class, true);
                    return;
                }
                if (email.length() == 0) {
                    Dialog dialog = new Dialog(Signup.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please enter your email address", Signup.this, Login.class, true);
                    return;
                }

                if (password.length() == 0) {
                    Dialog dialog = new Dialog(Signup.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please enter password", Signup.this, Login.class, true);
                    return;
                }

                if (rePassword.length() == 0) {
                    Dialog dialog = new Dialog(Signup.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please re-enter password", Signup.this, Login.class, true);
                    return;
                }

                if (!rePassword.equals(password)) {
                    Dialog dialog = new Dialog(Signup.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Two passwords are not the same", Signup.this, Login.class, true);
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


        // Show password
        txtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= txtPassword.getRight() - txtPassword.getCompoundDrawables()[Right].getBounds().width()) {
                        if (passwordVisible) {
                            txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_password, 0, R.drawable.show_password, 0);
                            txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_password, 0, R.drawable.hide_password, 0);
                            txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                    }
                }
                return false;
            }
        });

        // Show RePassword
        txtRePassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= txtRePassword.getRight() - txtRePassword.getCompoundDrawables()[Right].getBounds().width()) {
                        if (rePasswordVisible) {
                            txtRePassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_password, 0, R.drawable.show_password, 0);
                            txtRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            rePasswordVisible = false;
                        } else {
                            txtRePassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_password, 0, R.drawable.hide_password, 0);
                            txtRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            rePasswordVisible = true;
                        }
                    }
                }
                return false;
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

        txtUsername.setFilters(new InputFilter[]{InputHandle.filter});
        txtEmail.setFilters(new InputFilter[]{InputHandle.filter});
    }

    private void handleSignupApi() {
        LoadingDialog loadingDialog = new LoadingDialog(Signup.this);
        loadingDialog.startLoadingDialog();
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
                            Dialog dialog = new Dialog(Signup.this);
                            ShowDialog showDialog = new ShowDialog(dialog, true, "Successful account registration!\n" + messageObject.get("message"), Signup.this, Login.class, true);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            String strResponseBody = response.errorBody().string();
                            JSONObject messageObject = new JSONObject(strResponseBody);
                            Dialog dialog = new Dialog(Signup.this);
                            ShowDialog showDialog = new ShowDialog(dialog, false, "Account registration failed!\n" + messageObject.get("message"), Signup.this, Login.class, true);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    Log.e("ERROR", "message: " + e.getMessage());
                }
                loadingDialog.closeLoadingDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Dialog dialog = new Dialog(Signup.this);
                loadingDialog.closeLoadingDialog();
                ShowDialog showDialog = new ShowDialog(dialog, false, "Connection errors! Please try again later", Signup.this, Login.class, true);
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
        Log.v("THEO DOI", "ON");
        super.onStart();
    }

    @Override
    protected void onStop() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        unregisterReceiver(networkChangeReceiver);
        Log.v("THEO DOI", "OFF");
        super.onStop();
    }
}