package com.syd.ticktocktimepieces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.syd.ticktocktimepieces.models.WatchModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class OrderDetailActivity extends AppCompatActivity {
    RecyclerView orderrecyclerview;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        back = findViewById(R.id.image_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        List<WatchModel> watches = new ArrayList<>();
        orderrecyclerview = findViewById(R.id.order_detail_recycler_view);
        orderrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String products = getIntent().getStringExtra("products");
        Gson gson=new Gson();
        WatchModel[] watchitems = gson.fromJson(products,WatchModel[].class);
        watches = Arrays.asList(watchitems);
        watches = new ArrayList(watches);
        orderrecyclerview.setAdapter(new WatchAdapter(watches));


    }
    private class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.WatchViewHolder> {

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

}