package com.syd.ticktocktimepieces;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class PratikActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratik);
        Toast.makeText(PratikActivity.this,"+Android Developer Thank For Visiting !+",Toast.LENGTH_LONG).show();

    }
}