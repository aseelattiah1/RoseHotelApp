package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.Assel.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GuestActivity extends AppCompatActivity {

    TextView showUsernameGuestTV, showPhoneNumberGuestTV;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        showUsernameGuestTV = findViewById(R.id.showUsernameGuestTV);
        showPhoneNumberGuestTV = findViewById(R.id.showPhoneNumberGuestTV);

        auth = FirebaseAuth.getInstance();

        showData();
    }


    private void showData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersGuest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(auth.getCurrentUser().getEmail())) {
                        String name = snapshot1.child("username").getValue(String.class);
                        String phoneNumber = snapshot1.child("phoneNumber").getValue(String.class);
                        showUsernameGuestTV.setText("Username: "+name + "");
                        showPhoneNumberGuestTV.setText("Phone Number: "+phoneNumber + "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}