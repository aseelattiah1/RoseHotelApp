package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Assel.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText usernameET, roomNumberET, emailET, passwordET;
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
                String username = usernameET.getText().toString();
                String roomNumber = roomNumberET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (username == null || username.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                } else if (roomNumber == null || roomNumber.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Enter RoomNumber", Toast.LENGTH_SHORT).show();
                } else if (email == null || email.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (password == null || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.length() < 11) {
                        Toast.makeText(RegisterActivity.this, "Email Wrong", Toast.LENGTH_SHORT).show();
                    } else if (!email.contains("@")) {
                        Toast.makeText(RegisterActivity.this, "Email Wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        int beginIndex = email.indexOf("@");
                        String gmailCom = email.substring(beginIndex, email.length());
                        if (gmailCom.equals("@gmail.com")) {
                            if (isValidPassword(password) && password.length() >= 8) {
                                signInUser(username, roomNumber, email, password);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Password Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Email Wrong", Toast.LENGTH_SHORT).show();
                            Log.d("ttt", gmailCom);
                        }
                    }
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
                Intent intent = new Intent(getApplicationContext(), ResidenceActivity.class);
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

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


}