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

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    public static DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuImageView;

    Button residenceBtn, guestBtn;
    int numberUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        residenceBtn = findViewById(R.id.residenceBtn);
        guestBtn = findViewById(R.id.guestBtn);

        drawerLayout = findViewById(R.id.dl);
        navigationView = findViewById(R.id.nav_view1);
        menuImageView = findViewById(R.id.menu_imagview);

        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        residenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberUser = 1;
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                intent.putExtra("numberUser", numberUser);
                startActivity(intent);
            }
        });

        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberUser = 2;
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                intent.putExtra("numberUser", numberUser);
                startActivity(intent);
            }
        });

        View header = LayoutInflater.from(this).inflate(R.layout.nav_bar_layout, null);
        navigationView.addHeaderView(header);

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