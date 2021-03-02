package com.Assel.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.Assel.myapplication.R;
public class CleaningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning);
        Button cleaningBtn = findViewById(R.id.cleaningBtn);

        cleaningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CleaningActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}