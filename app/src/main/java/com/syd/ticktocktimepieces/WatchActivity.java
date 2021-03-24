package com.syd.ticktocktimepieces;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syd.ticktocktimepieces.models.WatchModel;
import com.syd.ticktocktimepieces.models.WatchResponseModel;
import com.syd.ticktocktimepieces.network.NetworkClient;
import com.syd.ticktocktimepieces.network.NetworkServices;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchActivity extends AppCompatActivity {
    RecyclerView WatchRecyclerView;
    TextView textToolBarTitle;
    ImageView imageback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);


        imageback = (ImageView) findViewById(R.id.image_back);
        imageback.setOnClickListener(v -> onBackPressed());
        textToolBarTitle = (TextView) findViewById(R.id.text_toolbar_title);
        textToolBarTitle.setText(getIntent().getStringExtra("category"));
        WatchRecyclerView = (RecyclerView) findViewById(R.id.watch_recycler_view);
        WatchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        WatchRecyclerView.setHasFixedSize(true);
        getWatch();
    }
    private void getWatch(){

        AlertDialog alertDialog = new SpotsDialog(WatchActivity.this,R.style.Custom2);
        alertDialog.show();

        NetworkServices networkServices = NetworkClient.getClient().create(NetworkServices.class);
        Call<WatchResponseModel> getWatch = networkServices.getWatchByCategories(getIntent().getStringExtra("category"));
        getWatch.enqueue(new Callback<WatchResponseModel>() {
            @Override
            public void onResponse(Call<WatchResponseModel> call, Response<WatchResponseModel> response) {
                alertDialog.cancel();
                WatchAdapter watchAdapter = new WatchAdapter(response.body().getWatch());
                WatchRecyclerView.setAdapter(watchAdapter);

            }

            @Override
            public void onFailure(Call<WatchResponseModel> call, Throwable t) {
                alertDialog.cancel();
            }
        });
    }

    public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.WatchViewHolder> {

        List<WatchModel> watch;
        WatchAdapter(List<WatchModel> watch){
            this.watch = watch;
        }

        @NonNull
        @Override
        public WatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_item_container,parent,false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull WatchViewHolder holder, int position) {
            if(watch.get(position).getName() !=null && !watch.get(position).getName().equals(""))
            {
                holder.textName.setText(watch.get(position).getName());
            }else {
                holder.textName.setVisibility(View.GONE);
            }
            if(watch.get(position).getCOmpany() !=null && !watch.get(position).getCOmpany().equals(""))
            {
                holder.textCompany.setText(watch.get(position).getCOmpany());
            }else {
                holder.textCompany.setVisibility(View.GONE);
            }
            if(watch.get(position).getPrice() !=null && !watch.get(position).getPrice().equals(""))
            {
                holder.textPrice.setText("\u20B9" + watch.get(position).getPrice());
            }else {
                holder.textPrice.setVisibility(View.GONE);
            }
            if(watch.get(position).getDescription() !=null && !watch.get(position).getDescription().equals(""))
            {
                holder.textDescription.setText(watch.get(position).getDescription());
            }else {
                holder.textDescription.setVisibility(View.GONE);
            }
            if(watch.get(position).getImage() !=null && !watch.get(position).getImage().equals(""))
            {
                Picasso.get().load(watch.get(position).getImage()).into(holder.imageWatch);
            }else {
                holder.imageWatch.setVisibility(View.GONE);
            }
            holder.CardWatch.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), WatchDetailActivity.class);
                intent.putExtra("image", watch.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("image_2", watch.get(holder.getAdapterPosition()).getImage_2());
                intent.putExtra("image_3", watch.get(holder.getAdapterPosition()).getImage_3());
                intent.putExtra("image_4", watch.get(holder.getAdapterPosition()).getImage_4());
                intent.putExtra("Company", watch.get(holder.getAdapterPosition()).getCOmpany());
                intent.putExtra("published_year", watch.get(holder.getAdapterPosition()).getPublishedYear());
                intent.putExtra("description", watch.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("price", watch.get(holder.getAdapterPosition()).getPrice());
                intent.putExtra("name", watch.get(holder.getAdapterPosition()).getName());
                intent.putExtra("id", watch.get(holder.getAdapterPosition()).getId());
                intent.putExtra("category", watch.get(holder.getAdapterPosition()).getCategory());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return watch.size();
        }

        public class WatchViewHolder extends RecyclerView.ViewHolder{
            CardView CardWatch;
            ImageView imageWatch;
            TextView textName,textCompany,textPrice,textDescription;
            public WatchViewHolder(@NonNull View view) {
                super(view);

                CardWatch = view.findViewById(R.id.watch_card_view);
                imageWatch = (ImageView)view.findViewById(R.id.image_watch);
                textName = (TextView)view.findViewById(R.id.text_watch_name);
                textCompany = (TextView)view.findViewById(R.id.text_watch_company);
                textPrice = (TextView)view.findViewById(R.id.text_watch_price);
                textDescription = (TextView)view.findViewById(R.id.text_watch_description);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}