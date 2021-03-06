package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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

public class ChooseTimeForCleaningActivity extends AppCompatActivity {

    Spinner spinner;
    Button saveNumberForCleanBtn;

    String startTimeClean, endTimeClean;

    boolean isSelectedItem = false;
    String username, email, roomNumber;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time_for_cleaning);

        spinner = findViewById(R.id.spinner);
        saveNumberForCleanBtn = findViewById(R.id.saveNumberForCleanBtn);

        auth = FirebaseAuth.getInstance();
        getUserData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isSelectedItem = true;
                String time = spinner.getItemAtPosition(i).toString();
                startTimeClean = time;
                endTimeClean = getEndTime(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isSelectedItem = false;
            }
        });

        saveNumberForCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectedItem) {
                    showCleanDialog(startTimeClean);
                } else {
                    Toast.makeText(ChooseTimeForCleaningActivity.this, "Select Time", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getEndTime(int postion) {
        String endTime = null;

        switch (postion) {
            case 0:
                endTime = "11:00";
                break;
            case 1:
                endTime = "12:00";
                break;
            case 2:
                endTime = "13:00";
                break;
            case 3:
                endTime = "14:00";
                break;
            case 4:
                endTime = "15:00";
                break;
        }

        return endTime;
    }

    private void saveNumberForClean() {

        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TimeCleaning");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("startTimeClean", startTimeClean);
        hashMap.put("endTimeClean", endTimeClean);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("roomNumber", roomNumber);


        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChooseTimeForCleaningActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChooseTimeForCleaningActivity.this, ResidenceActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChooseTimeForCleaningActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String emailUser = snapshot1.child("email").getValue(String.class);
                    if (emailUser.equals(auth.getCurrentUser().getEmail())) {
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

    public void showCleanDialog(String time) {

        final Dialog dialog = new Dialog(ChooseTimeForCleaningActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clean_dialog);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        TextView yourTimeForCleanTV = dialog.findViewById(R.id.yourTimeForCleanTV);
        Button cleanSaveData = dialog.findViewById(R.id.cleanSaveData);
        yourTimeForCleanTV.setText(time + "");

        cleanSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNumberForClean();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}