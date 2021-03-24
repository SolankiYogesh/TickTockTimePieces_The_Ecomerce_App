package com.syd.ticktocktimepieces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.syd.ticktocktimepieces.models.CategoryModel;
import com.syd.ticktocktimepieces.models.CategoryResponseModel;
import com.syd.ticktocktimepieces.network.NetworkClient;
import com.syd.ticktocktimepieces.network.NetworkServices;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView categoriesRecyclerView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = findViewById(R.id.image_back);
        back.setOnClickListener(v -> onBackPressed());
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        categoriesRecyclerView.setHasFixedSize(true);
        getCategory();
    }

    private void getCategory() {

        AlertDialog alertDialog = new SpotsDialog(MainActivity.this, R.style.Custom);
        alertDialog.show();

        NetworkServices networkService = NetworkClient.getClient().create(NetworkServices.class);

        Call<CategoryResponseModel> getCategoryResponseModal = networkService.getCategoriesFromServer();
        getCategoryResponseModal.enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponseModel> call, @NonNull Response<CategoryResponseModel> response) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                CategoryResponseModel categoryResponseModel = response.body();
                CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoryResponseModel.getCategories());
                categoriesRecyclerView.setAdapter(categoriesAdapter);
                alertDialog.cancel();

            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "PLEASE CHECK YOUR INTERNET..", Toast.LENGTH_LONG).show();
                alertDialog.cancel();

            }
        });
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView categoryItemLayout;
        TextView textCategory;

        CategoryViewHolder(View view) {
            super(view);
            categoryItemLayout = view.findViewById(R.id.category_card_view);
            textCategory = view.findViewById(R.id.text_category);
        }
    }

    private class CategoriesAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        List<CategoryModel> categories;

        CategoriesAdapter(List<CategoryModel> categories) {
            this.categories = categories;
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_container, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

            if (categories.get(holder.getAdapterPosition()).getCategory() != null) {
                holder.textCategory.setText(categories.get(holder.getAdapterPosition()).getCategory());
                holder.categoryItemLayout.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), WatchActivity.class);
                    intent.putExtra("category", categories.get(holder.getAdapterPosition()).getCategory());
                    startActivity(intent);
                });
            }
        }
    }
}
