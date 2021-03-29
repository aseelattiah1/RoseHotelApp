package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Assel.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GuestActivity extends AppCompatActivity {

    TextView showUsernameGuestTV, showPhoneNumberGuestTV;
    ImageView menuImageViewGuest;
    public static DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button restaurantGuestBtn, hallGuestBtn;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        hallGuestBtn = findViewById(R.id.hallGuestBtn);
        restaurantGuestBtn = findViewById(R.id.restaurantGuestBtn);
        showUsernameGuestTV = findViewById(R.id.showUsernameGuestTV);
        showPhoneNumberGuestTV = findViewById(R.id.showPhoneNumberGuestTV);
        menuImageViewGuest = findViewById(R.id.menuImageViewGuest);
        drawerLayout = findViewById(R.id.dlGuest);
        navigationView = findViewById(R.id.nav_viewGuest);

        auth = FirebaseAuth.getInstance();

        showData();

        hallGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestActivity.this, HallActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        restaurantGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestActivity.this, GuestResturantActivity.class);
                startActivity(intent);
            }
        });

        menuImageViewGuest.setOnClickListener(new View.OnClickListener() {
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

        switch (LogInActivity.numberUser) {
            case 2:
                View header = navigationView.getHeaderView(0);
                Button logOutGuest = header.findViewById(R.id.logOutGuest);
                logOutGuest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (auth.getCurrentUser().getEmail() != null) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            auth.signOut();
                        }
                    }
                });

                break;
            default:
                View header1 = navigationView.getHeaderView(0);
                Button logOutResidence = header1.findViewById(R.id.logOutResidence);

                logOutResidence.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (auth.getCurrentUser().getEmail() != null) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            auth.signOut();
                        }
                    }
                });
                break;
        }
    }


    private void showData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersGuest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(auth.getCurrentUser().getEmail())) {
                        String name = snapshot1.child("username").getValue(String.class);
                        String phoneNumber = snapshot1.child("phoneNumber").getValue(String.class);
                        showUsernameGuestTV.setText("Username: " + name + "");
                        showPhoneNumberGuestTV.setText("Phone Number: " + phoneNumber + "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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