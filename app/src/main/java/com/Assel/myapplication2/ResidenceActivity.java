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

public class ResidenceActivity extends AppCompatActivity {

    ImageView menuImageViewService;
    Button roomServiceBtn, laundryBtn;
    public static DrawerLayout drawerLayout;
    NavigationView navigationView;

    FirebaseAuth auth;

    TextView showRoomNumberRoomServiceTV, showUsernameRoomServiceTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);

        roomServiceBtn = findViewById(R.id.roomServiceBtn);
        laundryBtn = findViewById(R.id.laundryBtn);
        menuImageViewService = findViewById(R.id.menuImageViewService);
        drawerLayout = findViewById(R.id.dlRoomService);
        navigationView = findViewById(R.id.nav_viewRoomService);
        showUsernameRoomServiceTV = findViewById(R.id.showUsernameRoomServiceTV);
        showRoomNumberRoomServiceTV = findViewById(R.id.showRoomNumberRoomServiceTV);

        auth = FirebaseAuth.getInstance();

        showData();

        roomServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResidenceActivity.this, RoomServiceActivity.class);
                startActivity(intent);
            }
        });

        laundryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResidenceActivity.this, LaundaryNumberActivity.class);
                startActivity(intent);
            }
        });

        menuImageViewService.setOnClickListener(new View.OnClickListener() {
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(auth.getCurrentUser().getEmail())) {
                        String name = snapshot1.child("username").getValue(String.class);
                        String roomNumber = snapshot1.child("roomNumber").getValue(String.class);
                        showUsernameRoomServiceTV.setText("Username: " + name + "");
                        showRoomNumberRoomServiceTV.setText("Room Number: " + roomNumber + "");
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