package com.syd.ticktocktimepieces;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.views.PosterSlider;
import com.syd.ticktocktimepieces.databse.DatabaseHandler;
import com.syd.ticktocktimepieces.models.WatchModel;
import com.syd.ticktocktimepieces.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class WatchDetailActivity extends AppCompatActivity {
    Button imageaddtocar;
    ImageView imageback,plus,minus;
    TextView txtdescription, txtcompany, txtpublishyear, txtprice, textToolbarTitle,quant;
    int counter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_detail);



        plus = findViewById(R.id.increase);
        minus = findViewById(R.id.decrease);
        quant = findViewById(R.id.integer_number);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter>0){
                    counter--;
                    quant.setText(String.valueOf(counter));

                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter>=10){
                    Toast.makeText(WatchDetailActivity.this,"You Can't Buy More Than Product At A Time!",Toast.LENGTH_SHORT).show();
                }else {
                    counter++;
                    quant.setText(String.valueOf(counter));
                }

            }
        });
        imageaddtocar = findViewById(R.id.add_to_cart);
        txtcompany = findViewById(R.id.text_comapny);
        txtpublishyear =  findViewById(R.id.text_Published_year);
        txtdescription =  findViewById(R.id.text_description);
        txtprice =  findViewById(R.id.text_price);
        textToolbarTitle =  findViewById(R.id.text_toolbar_title);
        imageback =  findViewById(R.id.image_back);
        imageback.setOnClickListener(v -> onBackPressed());

        txtdescription.setText(getIntent().getStringExtra("description"));
        txtpublishyear.setText("Published :" + getIntent().getStringExtra("published_year"));
        txtcompany.setText("By :" + getIntent().getStringExtra("Company"));
        textToolbarTitle.setText(getIntent().getStringExtra("name"));
        txtprice.setText("\u20B9" + getIntent().getStringExtra("price"));

        try {
            PosterSlider posterSlider =  findViewById(R.id.poster_slider);
            List<Poster> posters = new ArrayList<>();

            posters.add(new RemoteImage(getIntent().getStringExtra("image")));
            posters.add(new RemoteImage(getIntent().getStringExtra("image_2")));
            posters.add(new RemoteImage(getIntent().getStringExtra("image_3")));
            posters.add(new RemoteImage(getIntent().getStringExtra("image_4")));
            posterSlider.setPosters(posters);

        } catch (Exception ignored) {
        }

        imageaddtocar.setOnClickListener(v -> {
            WatchModel watchModel = new WatchModel();
            watchModel.setId(getIntent().getStringExtra("id"));
            watchModel.setCategory(getIntent().getStringExtra("category"));
            watchModel.setName(getIntent().getStringExtra("name"));
            watchModel.setCOmpany(getIntent().getStringExtra("Company"));
            watchModel.setDescription(getIntent().getStringExtra("description"));
            watchModel.setImage(getIntent().getStringExtra("image"));
            watchModel.setPublishedYear(getIntent().getStringExtra("published_year"));
            watchModel.setPrice(getIntent().getStringExtra("price"));


            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            databaseHandler.AddToCart(watchModel);

            Toast.makeText(WatchDetailActivity.this, "Added To Cart !", Toast.LENGTH_SHORT).show();

        });

    }

}
