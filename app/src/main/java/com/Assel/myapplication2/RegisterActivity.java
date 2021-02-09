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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameET, roomNumberET, emailET, passwordET;
    Button singUpBtn;
    TextView haveAccountTV;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = findViewById(R.id.usernameET);
        roomNumberET = findViewById(R.id.roomNumberET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

        singUpBtn = findViewById(R.id.singUpBtn);
        haveAccountTV = findViewById(R.id.haveAccountTV);

        auth = FirebaseAuth.getInstance();

        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameET.getText().toString().trim();
                String roomNumber = roomNumberET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if (!(username == null && username.isEmpty())
                        && !(roomNumber == null && roomNumber.isEmpty())
                        && !(email == null && email.isEmpty())
                        && !(password == null && password.isEmpty())) {
                    signInUser(username, roomNumber, email, password);
                }
            }
        });
        haveAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signInUser(String username, String roomNumber, String email, String password) {
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email, password);
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                saveUserData(username, roomNumber, email, password);
                Intent intent = new Intent(getApplicationContext(), RoomServiceActivity.class);
                startActivity(intent);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Email or Password Error, try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveUserData(String username, String roomNumber, String email, String password) {
        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("roomNumber", roomNumber);


        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

}