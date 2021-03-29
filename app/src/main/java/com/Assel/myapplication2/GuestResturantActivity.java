package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Assel.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class GuestResturantActivity extends AppCompatActivity {

    TextView addPersonTV, removePersonTV,
            personNumberTV,
            inTV, outTV,
            inOrOutTV;

    Button saveDataBtn;

    int personNumber;
    String username, email, phoneNumber;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_resturant);


        addPersonTV = findViewById(R.id.addPersonTV);
        removePersonTV = findViewById(R.id.removePersonTV);
        personNumberTV = findViewById(R.id.personNumberTV);
        inTV = findViewById(R.id.inTV);
        outTV = findViewById(R.id.outTV);
        inOrOutTV = findViewById(R.id.inOrOutTV);

        saveDataBtn = findViewById(R.id.saveDataBtn);

        auth = FirebaseAuth.getInstance();
        getUserData();

        addPersonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personNumber++;
                personNumberTV.setText(personNumber + "");
            }
        });

        removePersonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personNumber > 0) {
                    personNumber--;
                    personNumberTV.setText(personNumber + "");
                }
            }
        });

        inTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inOrOutTV.setText("IN");
            }
        });

        outTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inOrOutTV.setText("OUT");
            }
        });

        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!personNumberTV.getText().toString().isEmpty() && personNumberTV.getText().toString() != null &&
                        !inOrOutTV.getText().toString().isEmpty() && inOrOutTV.getText().toString() != null) {
                    saveData(personNumberTV.getText().toString(), inOrOutTV.getText().toString());
                } else {
                    Toast.makeText(GuestResturantActivity.this, "Enter Person Number And In Or Out", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersGuest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(auth.getCurrentUser().getEmail())) {
                        username = snapshot1.child("username").getValue(String.class);
                        email = snapshot1.child("email").getValue(String.class);
                        phoneNumber = snapshot1.child("phoneNumber").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveData(String personNumber, String inOrOut) {
        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GuestResturant");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("personNumber", personNumber);
        hashMap.put("inOrOut", inOrOut);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("phoneNumber", phoneNumber);

        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(GuestResturantActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GuestResturantActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}