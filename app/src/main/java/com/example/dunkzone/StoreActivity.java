package com.example.dunkzone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dunkzone.adapters.ProductAdapter;
import com.example.dunkzone.api.SupabaseApiService;
import com.example.dunkzone.api.SupabaseClient;
import com.example.dunkzone.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private SupabaseApiService apiService;
    private BottomNavigationView bottomNavigationView;
    private Button logoutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        logoutButton = findViewById(R.id.logoutButton);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        productsRecyclerView.setAdapter(productAdapter);

        apiService = SupabaseClient.getApiService();

        loadProducts(null); // Load all products by default

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_all) {
                loadProducts(null);
                return true;
            } else if (itemId == R.id.nav_shoes) {
                loadProducts("eq.1"); // category_id for shoes
                return true;
            } else if (itemId == R.id.nav_balls) {
                loadProducts("eq.2"); // category_id for balls
                return true;
            } else if (itemId == R.id.nav_apparel) {
                loadProducts("eq.3"); // category_id for apparel
                return true;
            }
            return false;
        });

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadProducts(String categoryFilter) {
        productList.clear(); // Clear existing products
        productAdapter.notifyDataSetChanged();

        apiService.getProducts("*", categoryFilter).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(StoreActivity.this, R.string.products_load_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("StoreActivity", "Failed to load products", t);
                Toast.makeText(StoreActivity.this, getString(R.string.products_load_failed_with_error, t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
