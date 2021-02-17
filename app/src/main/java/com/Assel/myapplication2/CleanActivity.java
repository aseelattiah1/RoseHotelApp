package com.Assel.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.Assel.myapplication.R;

public class CleanActivity extends AppCompatActivity {

    Button clean1Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);

        clean1Btn  = findViewById(R.id.clean1Btn);

        clean1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CleanActivity.this, ChooseTimeForCleaningActivity.class);
                startActivity(intent);
            }
        });

    }
}