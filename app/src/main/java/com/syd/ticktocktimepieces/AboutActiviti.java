package com.syd.ticktocktimepieces;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActiviti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_);
        Toast.makeText(AboutActiviti.this, "Please Give 5 Star Rate In PlayStore", Toast.LENGTH_LONG).show();
    }
}