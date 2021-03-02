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
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuestRegisterActivity extends AppCompatActivity {

    TextInputEditText usernameGuestET, phoneNumberGuestET, emailGeustET, passwordGuestET;
    Button signUpGuestBtn;
    TextView haveAccountGuestTV;

    CountryCodePicker countryCodePicker;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);

        usernameGuestET = findViewById(R.id.usernameGuestET);
        phoneNumberGuestET = findViewById(R.id.phoneNumberGuestET);
        emailGeustET = findViewById(R.id.emailGeustET);
        passwordGuestET = findViewById(R.id.passwordGuestET);
        countryCodePicker = findViewById(R.id.countryCodePicker);

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

                if (username == null || username.isEmpty()) {

                    Toast.makeText(GuestRegisterActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber == null || phoneNumber.isEmpty()) {

                    Toast.makeText(GuestRegisterActivity.this, "Enter PhoneNumber", Toast.LENGTH_SHORT).show();
                } else if (email == null || email.isEmpty()) {

                    Toast.makeText(GuestRegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (password == null || password.isEmpty()) {
                    Toast.makeText(GuestRegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.length() < 11) {
                        Toast.makeText(GuestRegisterActivity.this, "Email Wrong", Toast.LENGTH_SHORT).show();
                    } else if (!email.contains("@")) {
                        Toast.makeText(GuestRegisterActivity.this, "Email Wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        int beginIndex = email.indexOf("@");
                        String gmailCom = email.substring(beginIndex, email.length());
                        if (gmailCom.equals("@gmail.com")) {
                            if (isValidPassword(password) && password.length() >= 8) {
                                String cCPicker = countryCodePicker.getFullNumber();
                                signInUserGuest(username, cCPicker + phoneNumber, email, password);
                            } else {
                                Toast.makeText(GuestRegisterActivity.this, "Password Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(GuestRegisterActivity.this, "Email Wrong", Toast.LENGTH_SHORT).show();
                            Log.d("ttt", gmailCom);
                        }
                    }
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
                Intent intent = new Intent(getApplicationContext(), GuestActivity.class);
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

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


}