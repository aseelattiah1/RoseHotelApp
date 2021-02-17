package com.Assel.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.Assel.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button residenceBtn, guestBtn;
    int numberUser;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        residenceBtn = findViewById(R.id.residenceBtn);
        guestBtn = findViewById(R.id.guestBtn);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {
            switch (numberUser) {
                case 2:
                    Intent intent = new Intent(getApplicationContext(), GuestActivity.class);
                    intent.putExtra("email", firebaseUser.getEmail());
                    startActivity(intent);
                    break;
                default:
                    Intent intent1 = new Intent(getApplicationContext(), ResidenceActivity.class);
                    intent1.putExtra("email", firebaseUser.getEmail());
                    startActivity(intent1);
                    break;
            }
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


}