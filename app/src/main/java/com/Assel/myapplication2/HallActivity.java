package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class HallActivity extends AppCompatActivity {

    String date, dateType = null;
    Button dateBtn, weedingBtn, meetingBtn, saveHallBtn;
    TextView textView25,textView26;
    DatePickerDialog datePickerDialog;

    int n;
    String username, email, phoneNumber, roomNumber;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        n = getIntent().getIntExtra("type", 0);

        dateBtn = findViewById(R.id.dateBtn);
        weedingBtn = findViewById(R.id.weedingBtn);
        meetingBtn = findViewById(R.id.meetingBtn);
        saveHallBtn = findViewById(R.id.saveHallBtn);
        textView25 = findViewById(R.id.textView25);
        textView26 = findViewById(R.id.textView26);

        auth = FirebaseAuth.getInstance();

        if (n == 1) {
            getUserGestData();
        } else {
            getUserResidenceData();
        }

        weedingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType = "weeding";
                textView26.setText(dateType);
            }
        });

        meetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType = "meeting";
                textView26.setText(dateType);
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(HallActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myYear, int myMonth, int myDay) {
                        date = myDay + "/" + myMonth + "/" + myYear;
                        textView25.setText(date);
                    }
                }, day, month, year);
                datePickerDialog.show();
            }
        });

        saveHallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date != null && !date.isEmpty() && dateType != null && !dateType.isEmpty()) {
                    if (n == 1) {
                        saveGestDate(date, dateType);
                    } else {
                        saveResidenceDate(date, dateType);
                    }
                } else {
                    Toast.makeText(HallActivity.this, "Choose Your Date And Date Type, Please", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getUserResidenceData() {
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

    private void getUserGestData() {
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

    private void saveGestDate(String date, String dateType) {
        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HallGest");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("date", date);
        hashMap.put("dateType", dateType);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("phoneNumber", phoneNumber);

        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HallActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HallActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveResidenceDate(String date, String dateType) {
        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HallResidence");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("date", date);
        hashMap.put("dateType", dateType);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("roomNumber", roomNumber);

        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HallActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HallActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}