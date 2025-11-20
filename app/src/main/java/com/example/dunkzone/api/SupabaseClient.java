package com.example.dunkzone.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {

    private static final String BASE_URL = "https://imtyxfutzsafpyzhplyf.supabase.co/rest/v1/";
    private static final String API_KEY = "";

    private static Retrofit retrofit = null;

    public static SupabaseApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                        .header("apikey", API_KEY)
                        .header("Authorization", "Bearer " + API_KEY)
                        .method(original.method(), original.body())
                        .build();
                    return chain.proceed(request);
                })
                .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(SupabaseApiService.class);
    }
}
