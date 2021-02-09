package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    EditText emailLoginET, passwordLogInET;
    Button logInBtn;
    TextView dontHavaAccountSignInTV;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    int numberUser;

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
                    logInUser(email, passwrod);
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
                    Intent intent = new Intent(getApplicationContext(), RoomServiceActivity.class);
                    startActivity(intent);
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

}