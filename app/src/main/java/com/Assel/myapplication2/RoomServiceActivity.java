package com.Assel.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.Assel.myapplication.R;
import com.google.android.material.navigation.NavigationView;

public class RoomServiceActivity extends AppCompatActivity {

    ImageView menuImageViewCleaning;
    Button cleaningBtn;

    public static DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_service);

        cleaningBtn = findViewById(R.id.cleaningBtn);
        menuImageViewCleaning = findViewById(R.id.menuImageViewCleaning);

        drawerLayout = findViewById(R.id.dlCleaning);
        navigationView = findViewById(R.id.nav_viewCleaning);

        cleaningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomServiceActivity.this,CleanActivity.class);
                startActivity(intent);
            }
        });

        menuImageViewCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        switch (LogInActivity.numberUser) {
            case 2:
                View header = LayoutInflater.from(this).inflate(R.layout.nav_bar_guest_layout, null);
                navigationView.addHeaderView(header);
                break;
            default:
                View header1 = LayoutInflater.from(this).inflate(R.layout.nav_bar_residence_layout, null);
                navigationView.addHeaderView(header1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

}