package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Assel.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    TextInputEditText emailLoginET, passwordLogInET;
    Button logInBtn;
    TextView dontHavaAccountSignInTV;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    public static int numberUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        numberUser = getIntent().getIntExtra("numberUser", 0);

        emailLoginET = findViewById(R.id.emailLoginET);
        passwordLogInET = findViewById(R.id.passwordLogInET);
        logInBtn = findViewById(R.id.logInBtn);
        dontHavaAccountSignInTV = findViewById(R.id.dontHavaAccountSignInTV);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLoginET.getText().toString().trim();
                String passwrod = passwordLogInET.getText().toString().trim();
                if (!(email == null && email.isEmpty())
                        && !(passwrod == null && passwrod.isEmpty())) {
                    switch (numberUser) {
                        case 2:
                            showDataUserGuest(email, passwrod);
                            break;
                        default:
                            showDataUserResidence(email, passwrod);
                            break;
                    }
                }
            }
        });

        dontHavaAccountSignInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (numberUser) {
                    case 2:
                        Intent intent2 = new Intent(LogInActivity.this, GuestRegisterActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        Intent intent0 = new Intent(LogInActivity.this, RegisterActivity.class);
                        startActivity(intent0);
                        break;
                }
            }
        });

    }

    private void logInUser(String email, String password) {
        Task<AuthResult> task = auth.signInWithEmailAndPassword(email, password);
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    switch (numberUser) {
                        case 2:
                            Intent intent = new Intent(getApplicationContext(), GuestActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            Intent intent1 = new Intent(getApplicationContext(), ResidenceActivity.class);
                            startActivity(intent1);
                            break;
                    }
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogInActivity.this, "Email or Password Error, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDataUserResidence(String email, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(email)) {
                        logInUser(email, password);
                    } else {
                        Toast.makeText(LogInActivity.this, "Wrong, try agin", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDataUserGuest(String email, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersGuest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(email)) {
                        logInUser(email, password);
                    } else {
                        Toast.makeText(LogInActivity.this, "Wrong, try agin", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }
}