package com.example.app_mobile.api;
import com.example.app_mobile.Model.ChangePassword;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.Model.UserLogin;
import com.example.app_mobile.Model.UserSignup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

;

public interface UserApi {
    //http://192.168.1.6:8080/api/products

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS)
            .build();
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public UserApi userAPI = new Retrofit.Builder()
            .baseUrl("http://192.168.1.139:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserApi.class);
    @POST("api/auth/signin")
    Call<User> postLogin(@Body UserLogin user);
   // Call<User> postLogin(@Body UserLogin user , @Header("Authorization") String token);
    @POST("api/auth/signup")
    Call<ResponseBody> postSignup(@Body UserSignup user);

    @POST("api/auth/changePassByEmail")
    Call<ResponseBody> postChangePassByEmail(@Body ChangePassword password);
}
