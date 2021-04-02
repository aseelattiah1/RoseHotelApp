package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.Assel.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button residenceBtn, guestBtn, locationBtn, rateUsBtn, contactUsBtn;
    int numberUser;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        residenceBtn = findViewById(R.id.residenceBtn);
        guestBtn = findViewById(R.id.guestBtn);
        locationBtn = findViewById(R.id.locationBtn);
        rateUsBtn = findViewById(R.id.rateUsBtn);
        contactUsBtn = findViewById(R.id.contactUsBtn);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                }catch (Exception X){
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }

            }
        });

        rateUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RateUsActivity.class);
                startActivity(intent);
            }
        });

        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            showDataUserResidence(firebaseUser.getEmail());
        }

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

    }

    private void showDataUserResidence(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> stringList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    stringList.add(emailUser);

                }

                if (stringList.contains(email)) {
                    Log.d("ttt","true");
                    numberUser = 1;
                    Intent intent1 = new Intent(getApplicationContext(), ResidenceActivity.class);
                    intent1.putExtra("email", firebaseUser.getEmail());
                    startActivity(intent1);

                } else {
                    Log.d("ttt","false");
                    numberUser = 2;
                    Intent intent = new Intent(getApplicationContext(), GuestActivity.class);
                    intent.putExtra("email", firebaseUser.getEmail());
                    startActivity(intent);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
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

}