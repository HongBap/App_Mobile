package com.example.app_mobile.Activity;

import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mobile.Model.ChangePassword;
import com.example.app_mobile.R;
import com.example.app_mobile.Retrofit.ApiService;
import com.example.app_mobile.Util.NetworkChangeReceiver;
import com.example.app_mobile.Util.ShowDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword_SetPass extends AppCompatActivity {
    Button btnEnterNewPassword;
    EditText txtEnterNewPassword, txtEnterReNewPassword;
    ImageButton btnPrevious;
    boolean passwordVisible, rePasswordVisible;

    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_set_pass);
        ForgotPassword_EnterEmail.otpCode = -1;
        addControls();
        addEvents();
    }


    private void addEvents() {
        btnEnterNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = txtEnterNewPassword.getText().toString().trim();
                String reNewPassword = txtEnterReNewPassword.getText().toString().trim();
                if (newPassword.length() == 0) {
                    Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please enter a new password!", ForgotPassword_SetPass.this, Login.class, true);
                    return;
                }
                else if (newPassword.length() < 6) {
                    Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Password needs 6 or more characters.", ForgotPassword_SetPass.this, Login.class, true);
                    return;
                }

                else if (reNewPassword.length() == 0) {
                    Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Please re-enter new password!", ForgotPassword_SetPass.this, Login.class, true);
                    return;
                }

                else if (newPassword.equals(reNewPassword) == false) {
                    Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
                    ShowDialog showDialog = new ShowDialog(dialog, false, "Hai mật khẩu không giống nhau! Vui lòng nhập lại.", ForgotPassword_SetPass.this, Login.class, true);
                    return;
                }

                handleLoginApi();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Show password
        txtEnterNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= txtEnterNewPassword.getRight() - txtEnterNewPassword.getCompoundDrawables()[Right].getBounds().width()){
                        if(passwordVisible){
                            txtEnterNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0);
                            txtEnterNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else{
                            txtEnterNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                            txtEnterNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                    }
                }
                return false;
            }
        });

        // Show RePassword
        txtEnterReNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= txtEnterReNewPassword.getRight() - txtEnterReNewPassword.getCompoundDrawables()[Right].getBounds().width()){
                        if(rePasswordVisible){
                            txtEnterReNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0);
                            txtEnterReNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            rePasswordVisible = false;
                        }else{
                            txtEnterReNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                            txtEnterReNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            rePasswordVisible = true;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void handleLoginApi() {
        String email = ForgotPassword_EnterEmail.emailInput;
        String password = txtEnterNewPassword.getText().toString().trim();
        if (email.length() == 0) {
            Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
            ShowDialog showDialog = new ShowDialog(dialog, false, "Email entered is not valid!\nPlease go back to entering your email address!", ForgotPassword_SetPass.this, Login.class, true);
            return;
        }
        if (!email.contains("@gmail.com")) {
            Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
            ShowDialog showDialog = new ShowDialog(dialog, false, "Email entered is not valid!\nPlease go back to entering your email address!", ForgotPassword_SetPass.this, Login.class, true);
            return;
        }

        LoadingDialog loadingDialog = new LoadingDialog(ForgotPassword_SetPass.this);
        loadingDialog.startLoadingDialog();

        Log.e("email", email);
        Log.e("txtPassword", password);
        ChangePassword changePassword = new ChangePassword(email, password);
        ApiService.apiService.postChangePassByEmail(changePassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
                        ShowDialog showDialog = new ShowDialog(dialog, true, "Password change successful!", ForgotPassword_SetPass.this, Login.class, true);
                    }
                } else {
                    try {
                        String strResponseBody = "";
                        strResponseBody = response.errorBody().string();
                        JSONObject messageObject = new JSONObject(strResponseBody);
                        Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
                        ShowDialog showDialog = new ShowDialog(dialog, false, "Password change failed!\n" + messageObject.get("message"), ForgotPassword_SetPass.this, Login.class, true);
                        Log.v("Error code 400", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadingDialog.closeLoadingDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Dialog dialog = new Dialog(ForgotPassword_SetPass.this);
                loadingDialog.closeLoadingDialog();
                ShowDialog showDialog = new ShowDialog(dialog, false, "Connection errors! Please try again later", ForgotPassword_SetPass.this, Login.class, true);
            }
        });
    }

    private void addControls() {
        btnEnterNewPassword = findViewById(R.id.btnEnterNewPassword);
        txtEnterNewPassword = findViewById(R.id.txtEnterNewPassword);
        txtEnterReNewPassword = findViewById(R.id.txtEnterReNewPassword);
        btnPrevious = findViewById(R.id.btnPrevious);
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