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

public class LaundaryNumberActivity extends AppCompatActivity {

    Button addNumberBtn, removeNumberBtn, saveNumberBtn;
    TextView showNumberTV;

    int number = 0;

    FirebaseAuth auth;

    String username,email,roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundary_number);

        addNumberBtn = findViewById(R.id.addNumberBtn);
        removeNumberBtn = findViewById(R.id.removeNumberBtn);
        saveNumberBtn = findViewById(R.id.saveNumberBtn);
        showNumberTV = findViewById(R.id.showNumberTV);
        showNumberTV.setText(number + "");

        auth = FirebaseAuth.getInstance();
        getUserData();
        addNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number++;
                showNumberTV.setText(number + "");
            }
        });

        removeNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number > 0) {
                    number--;
                    showNumberTV.setText(number + "");
                }
            }
        });

        saveNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLaundaryNumber(number);
            }
        });

    }

    private void saveLaundaryNumber(int laundaryNumber) {

        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LaundaryNumber");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("laundaryNumber", laundaryNumber);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("roomNumber", roomNumber);


        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LaundaryNumberActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LaundaryNumberActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(auth.getCurrentUser().getEmail())){
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

}