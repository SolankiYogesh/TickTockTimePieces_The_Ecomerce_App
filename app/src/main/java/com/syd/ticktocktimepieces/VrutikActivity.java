
package com.syd.ticktocktimepieces;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class VrutikActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrutik);
        Toast.makeText(VrutikActivity.this,"+Android Developer Thank For Visiting !+",Toast.LENGTH_LONG).show();

    }
}