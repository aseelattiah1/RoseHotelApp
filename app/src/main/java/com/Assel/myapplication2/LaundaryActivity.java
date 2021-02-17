package com.Assel.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.Assel.myapplication.R;

public class LaundaryActivity extends AppCompatActivity {

    Button laundar1, laundar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundary);

        laundar1 = findViewById(R.id.laundar1);
        laundar2 = findViewById(R.id.laundar2);

        laundar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaundaryActivity.this, LaundaryNumberActivity.class);
                startActivity(intent);
            }
        });

    }
}