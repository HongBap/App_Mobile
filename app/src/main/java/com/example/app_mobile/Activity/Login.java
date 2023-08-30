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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.Model.User;
import com.example.app_mobile.Model.UserLogin;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;
import com.example.app_mobile.Util.InputHandle;
import com.example.app_mobile.Util.NetworkChangeReceiver;
import com.example.app_mobile.Util.ShowDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    // Biến toàn cục
    private static String username_save = "";
    private static String password_save = "";
    private static boolean checkBox = false;
    Button btnSignIn;
    Boolean checkLogin = false;
    EditText txtUsername, txtPassword;

    CheckBox isRemember;

    TextView createAccount, forgotPassword;

    boolean passwordVisible;

    private long backPressTime;
    private Toast mToast;
    public static User userInfoLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        addEvents();
    }

    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            mToast.cancel();
            super.onBackPressed();
            return;
        } else {
            mToast = Toast.makeText(Login.this, "Press back 2 times to exit", Toast.LENGTH_LONG);
            mToast.show();
        }
        backPressTime = System.currentTimeMillis();
    }

    private void addControls() {
        btnSignIn = findViewById(R.id.btnSignIn);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        createAccount = findViewById(R.id.createAccount);
        forgotPassword = findViewById(R.id.forgotPassword);
        isRemember = findViewById(R.id.isRemember);

        txtUsername.setText(username_save);
        txtPassword.setText(password_save);
        isRemember.setChecked(checkBox);
        txtUsername.setFilters(new InputFilter[] {InputHandle.filter});
    }

    private void addEvents() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = false;
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (username.length() == 0) {
                    Dialog dialog = new Dialog(Login.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please enter username", Login.this, MainActivity.class, true);
                    return;
                }
                if (password.length() == 0) {
                    Dialog dialog = new Dialog(Login.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please enter password", Login.this, MainActivity.class, true);
                    return;
                }
                handleLoginApi();
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

        // createAccount
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        // forgotPassword
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword_EnterEmail.class);
                startActivity(intent);
            }
        });

    }


    public void handleLoginApi() {
        LoadingDialog loadingDialog = new LoadingDialog(Login.this);
        loadingDialog.startLoadingDialog();
        Log.e("txtUsername", txtUsername.getText().toString());
        Log.e("txtPassword", txtPassword.getText().toString());
        String u_name = txtUsername.getText().toString().trim().toLowerCase();
        String u_password = txtPassword.getText().toString().toLowerCase();
        UserLogin user = new UserLogin(u_name, u_password);
        ApiService.apiService.postLogin(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    checkLogin = true;
                    userInfoLogin = response.body();

                    if (isRemember.isChecked()) {
                        username_save = u_name;
                        password_save = u_password;
                        checkBox = true;
                    } else {
                        username_save = "";
                        password_save = "";
                        checkBox = false;
                    }
                    // Dialog dialog = new Dialog(Login.this);
                    // ShowDialog showDialog = new ShowDialog(dialog, true, "Đăng nhập thành công!", Login.this, MainActivity.class, true);
                    loadingDialog.closeLoadingDialog();
                    userInfoLogin.setToken("Bearer " + userInfoLogin.getToken());
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("userInfoLogin", userInfoLogin);
                    startActivity(intent);
                } else {
                    checkLogin = false;
                    userInfoLogin = null;
                    Dialog dialog = new Dialog(Login.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Incorrect account or password!", Login.this, MainActivity.class, true);
                }
                loadingDialog.closeLoadingDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                checkLogin = false;
                Dialog dialog = new Dialog(Login.this);
                loadingDialog.closeLoadingDialog();
                ShowDialog showDialog = new ShowDialog(dialog, false, "Connection errors! Please try again later", Login.this, MainActivity.class, true);
            }
        });
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