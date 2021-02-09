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

public class GuestRegisterActivity extends AppCompatActivity {

    EditText usernameGuestET, phoneNumberGuestET, emailGeustET, passwordGuestET;
    Button signUpGuestBtn;
    TextView haveAccountGuestTV;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);

        usernameGuestET = findViewById(R.id.usernameGuestET);
        phoneNumberGuestET = findViewById(R.id.phoneNumberGuestET);
        emailGeustET = findViewById(R.id.emailGeustET);
        passwordGuestET = findViewById(R.id.passwordGuestET);

        signUpGuestBtn = findViewById(R.id.signUpGuestBtn);
        haveAccountGuestTV = findViewById(R.id.haveAccountGuestTV);

        auth = FirebaseAuth.getInstance();

        signUpGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameGuestET.getText().toString().trim();
                String phoneNumber = phoneNumberGuestET.getText().toString().trim();
                String email = emailGeustET.getText().toString().trim();
                String password = passwordGuestET.getText().toString().trim();

                if (!(username == null && username.isEmpty())
                        && !(phoneNumber == null && phoneNumber.isEmpty())
                        && !(email == null && email.isEmpty())
                        && !(password == null && password.isEmpty())) {

                    signInUserGuest(username, phoneNumber, email, password);


                }
            }
        });

        haveAccountGuestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestRegisterActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signInUserGuest(String username, String phoneNumber, String email, String password) {
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email, password);
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                saveUserGuestData(username, phoneNumber, email, password);
                Intent intent = new Intent(getApplicationContext(), RoomServiceActivity.class);
                startActivity(intent);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GuestRegisterActivity.this, "Email or Password Error, try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveUserGuestData(String username, String phoneNumber, String email, String password) {
        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersGuest");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("phoneNumber", phoneNumber);


        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(GuestRegisterActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GuestRegisterActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}