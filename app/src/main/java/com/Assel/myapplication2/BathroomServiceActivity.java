package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

public class BathroomServiceActivity extends AppCompatActivity {

    Button towelAddBtn, towelRemoveBtn,
            toothbrushAddBtn, toothbrushRemoveBtn,
            shampooAddBtn, shampooRemoveBtn,
            saveBathroomServiceBtn;

    TextView towelResultTV, toothbrushResultTV, shampooResultTV;

    int numberTowel = 0;
    int numberToothbrush = 0;
    int numberShampoo = 0;

    String username, email, roomNumber;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_service);

        auth = FirebaseAuth.getInstance();
        getUserData();

        towelAddBtn = findViewById(R.id.towelAddBtn);
        towelRemoveBtn = findViewById(R.id.towelRemoveBtn);
        toothbrushAddBtn = findViewById(R.id.toothbrushAddBtn);
        toothbrushRemoveBtn = findViewById(R.id.toothbrushRemoveBtn);
        shampooAddBtn = findViewById(R.id.shampooAddBtn);
        shampooRemoveBtn = findViewById(R.id.shampooRemoveBtn);
        saveBathroomServiceBtn = findViewById(R.id.saveBathroomServiceBtn);
        towelResultTV = findViewById(R.id.towelResultTV);
        toothbrushResultTV = findViewById(R.id.toothbrushResultTV);
        shampooResultTV = findViewById(R.id.shampooResultTV);

        towelAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberTowel++;
                towelResultTV.setText(numberTowel + "");
            }
        });

        towelRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberTowel > 0) {
                    numberTowel--;
                    towelResultTV.setText(numberTowel + "");
                }
            }
        });

        toothbrushAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberToothbrush++;
                toothbrushResultTV.setText(numberToothbrush + "");
            }
        });

        toothbrushRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberToothbrush > 0) {
                    numberToothbrush--;
                    toothbrushResultTV.setText(numberToothbrush + "");
                }
            }
        });

        shampooAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberShampoo++;
                shampooResultTV.setText(numberShampoo + "");
            }
        });

        shampooRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberShampoo > 0) {
                    numberShampoo--;
                    shampooResultTV.setText(numberShampoo + "");
                }
            }
        });

        saveBathroomServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberTowel > 0 || numberToothbrush > 0 || numberShampoo > 0) {
                    showBathroomServiceDialog(numberTowel, numberToothbrush, numberShampoo);
                } else {
                    Toast.makeText(BathroomServiceActivity.this, "Choose Your Service, Please", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void showBathroomServiceDialog(int nTowel, int nToothbrush, int nShampoo) {

        final Dialog dialog = new Dialog(BathroomServiceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bathroom_dialog);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        TextView resultTowelDialogTV = dialog.findViewById(R.id.resultTowelDialogTV);
        TextView resultToothbrushDialogTV = dialog.findViewById(R.id.resultToothbrushDialogTV);
        TextView resultShampooDialogTV = dialog.findViewById(R.id.resultShampooDialogTV);
        Button saveBathroomServiceDialogBtn = dialog.findViewById(R.id.saveBathroomServiceDialogBtn);
        resultTowelDialogTV.setText(nTowel + "");
        resultToothbrushDialogTV.setText(nToothbrush + "");
        resultShampooDialogTV.setText(nShampoo + "");

        saveBathroomServiceDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBathroomService(nTowel, nToothbrush, nShampoo);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void saveBathroomService(int nTowel, int nToothbrush, int nShampoo) {

        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BathroomService");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("nTowel", nTowel);
        hashMap.put("nToothbrush", nToothbrush);
        hashMap.put("nShampoo", nShampoo);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("roomNumber", roomNumber);


        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BathroomServiceActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BathroomServiceActivity.this, ResidenceActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BathroomServiceActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
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

}