package com.Assel.myapplication2;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetData {


    public static boolean getStatus = false;

    public static void getData(String email) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(email)) {
                        getStatus = true;
                    } else {
                        getStatus = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getStatus = false;
            }
        });


    }


    public static void getData2(String email) {
        getStatus = true;
    }
}
