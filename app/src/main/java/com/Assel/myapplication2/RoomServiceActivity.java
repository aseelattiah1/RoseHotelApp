package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Assel.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class RoomServiceActivity extends AppCompatActivity {

    ImageView menuImageViewCleaning;
    Button cleaningBtn, otherBtn, bathroomServiceBtn, minibarBtn;

    public static DrawerLayout drawerLayout;
    NavigationView navigationView;

    String username, email, roomNumber;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_service);

        auth = FirebaseAuth.getInstance();
        getUserData();

        cleaningBtn = findViewById(R.id.cleaningBtn);
        otherBtn = findViewById(R.id.otherBtn);
        minibarBtn = findViewById(R.id.minibarBtn);
        bathroomServiceBtn = findViewById(R.id.bathroomServiceBtn);
        menuImageViewCleaning = findViewById(R.id.menuImageViewCleaning);

        drawerLayout = findViewById(R.id.dlCleaning);
        navigationView = findViewById(R.id.nav_viewCleaning);

        cleaningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomServiceActivity.this, ChooseTimeForCleaningActivity.class);
                startActivity(intent);
            }
        });

        minibarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMinibarDialog();
            }
        });

        otherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        bathroomServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomServiceActivity.this, BathroomServiceActivity.class);
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

    private void showMinibarDialog() {

        final Dialog dialog = new Dialog(RoomServiceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.minibar_dialog);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        Button yesMinibarBtn = dialog.findViewById(R.id.yesMinibarBtn);
        Button noMinibarBtn = dialog.findViewById(R.id.noMinibarBtn);

        yesMinibarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMinibarAnswer("yes");
                dialog.dismiss();
            }
        });

        noMinibarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMinibarAnswer("no");
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void saveMinibarAnswer(String result) {

        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Minibar");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("result", result);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("roomNumber", roomNumber);

        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RoomServiceActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RoomServiceActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showDialog() {

        final Dialog dialog = new Dialog(RoomServiceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.service_number_dialog);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        Button callNowBtn = dialog.findViewById(R.id.callNowBtn);

        callNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });
        dialog.show();
    }

    public void getUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(auth.getCurrentUser().getEmail())) {
                        username = snapshot1.child("username").getValue(String.class);
                        email = snapshot1.child("email").getValue(String.class);
                        roomNumber = snapshot1.child("roomNumber").getValue(String.class);
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