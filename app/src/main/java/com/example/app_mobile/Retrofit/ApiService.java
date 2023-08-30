package com.example.app_mobile.Retrofit;

import com.example.app_mobile.Model.ChangePassword;
import com.example.app_mobile.Model.Feature;
import com.example.app_mobile.Model.Order;
import com.example.app_mobile.Model.OrderDetailView;
import com.example.app_mobile.Model.Product;
import com.example.app_mobile.Model.Rate;
import com.example.app_mobile.Model.User;
import com.example.app_mobile.Model.UserLogin;
import com.example.app_mobile.Model.UserSignup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

;

public interface ApiService {
    //http://192.168.1.6:8080/api/products
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS)
            .build();
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.139:8081/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    ///Ngoc
    @POST("auth/signin")
    Call<User> postLogin(@Body UserLogin user);

    @POST("auth/signup")
    Call<ResponseBody> postSignup(@Body UserSignup user);

    @POST("auth/changePassByEmail")
    Call<ResponseBody> postChangePassByEmail(@Body ChangePassword password);

    ///// Hong
    @GET("products")
    Call<List<Product>> productListData();
    @GET("products")
    Call<List<Product>> getProductListSale(@Query("eventId") int eventId);
    @GET("features")
    Call<List<Feature>> featureListData();

    //// My
    @GET("orders")
    Call<List<Order>> getOrdersfromUserId(@Query("userId") Long userId , @Header("Authorization") String token);
    @POST("orders")
    Call<Order> PostOrder(@Body Order order ,@Header("Authorization") String token );
    @GET("rates")
    Call<List<Rate>> getRateFromProductId(@Query("productId") Long productId, @Header("Authorization") String token );

    @POST("rates")
    Call<Rate> PostRate(@Body Rate rate,@Header("Authorization") String token );

    @PUT("rates/{productId}-{userId}")
    Call<Rate> PutRate(@Body Rate rate, @Path("productId")Long productId, @Path("userId")Long userId);
    @PUT("orders/{orderId}")
    Call<Order> PutOrderByOrderID(@Path("orderId") int orderId, @Body Order order,@Header("Authorization") String token );

    /// Hào
    //http://localhost:8080/api/orderDetailViews?userId=10101
    @GET("orderDetailViews")
    Call<List<OrderDetailView>> getOrderDetailViews(@Query("userId") int userId , @Header("Authorization") String token);
    @DELETE("orders/{orderId}")
    Call<Void> DeleteOder(@Path("orderId") int orderId , @Header("Authorization") String token);

}
