package com.example.dunkzone.api;

import com.example.dunkzone.models.Product;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface SupabaseApiService {

    @POST("rpc/api_login")
    Call<JsonArray> login(@Body JsonObject body);

    @POST("rpc/api_register")
    Call<Void> register(@Body JsonObject body);

    @GET("products")
    Call<List<Product>> getProducts(@Query("select") String select, @Query("category_id") String categoryId);
}
