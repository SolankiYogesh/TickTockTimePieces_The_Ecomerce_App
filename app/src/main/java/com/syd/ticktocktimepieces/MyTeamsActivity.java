package com.syd.ticktocktimepieces;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MyTeamsActivity extends AppCompatActivity {
    CardView yog,prat,vrubh,vrut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);
        yog = findViewById(R.id.yogesh);
        prat = findViewById(R.id.pratik);
        vrubh = findViewById(R.id.vrushabh);
        vrut = findViewById(R.id.vrutik);

        vrubh.setOnClickListener(v -> startActivity(new Intent(MyTeamsActivity.this,VrushabhActivity.class)));
        prat.setOnClickListener(v -> startActivity(new Intent(MyTeamsActivity.this,PratikActivity.class)));
        yog.setOnClickListener(v -> startActivity(new Intent(MyTeamsActivity.this,AboutActiviti.class)));
        vrut.setOnClickListener(v -> startActivity(new Intent(MyTeamsActivity.this,VrutikActivity.class)));
    }
}