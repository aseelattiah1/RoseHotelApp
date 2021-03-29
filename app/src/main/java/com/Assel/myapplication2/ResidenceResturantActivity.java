package com.Assel.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ResidenceResturantActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button saveMealResidenceBtn;

    List<MenuResturant> menuResturantList;

    FirebaseAuth auth;
    String username, email, roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence_resturant);

        menuResturantList = new ArrayList<>();

        saveMealResidenceBtn = findViewById(R.id.saveMealResidenceBtn);

        auth = FirebaseAuth.getInstance();
        getUserData();

        recyclerView = findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        menuResturantList.add(new MenuResturant(R.drawable.shawerma, "Shawerma", "10$"));
        menuResturantList.add(new MenuResturant(R.drawable.burger, "Burger", "40$"));
        menuResturantList.add(new MenuResturant(R.drawable.pizze, "Pizze", "45$"));
        menuResturantList.add(new MenuResturant(R.drawable.pasta, "Pasta", "37$"));
        menuResturantList.add(new MenuResturant(R.drawable.kababe, "Kababe", "28$"));

        MenuResturantAdapter menuResturantAdapter = new MenuResturantAdapter(getApplicationContext(), menuResturantList);
        recyclerView.setAdapter(menuResturantAdapter);

        saveMealResidenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void showDialog() {
        final Dialog dialog = new Dialog(ResidenceResturantActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.resturant_dialog);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        TextView shawermaTV = dialog.findViewById(R.id.shawermaTV);
        TextView burgerTV = dialog.findViewById(R.id.burgerTV);
        TextView pizzeTV = dialog.findViewById(R.id.pizzeTV);
        TextView pasteTV = dialog.findViewById(R.id.pasteTV);
        TextView kababeTV = dialog.findViewById(R.id.kababeTV);

        TextView shawermaTitleTV = dialog.findViewById(R.id.shawermaTitleTV);
        TextView burgerTitleTV = dialog.findViewById(R.id.burgerTitleTV);
        TextView pizzeTitleTV = dialog.findViewById(R.id.pizzeTitleTV);
        TextView pastaTitleTV = dialog.findViewById(R.id.pastaTitleTV);
        TextView kababeTitleTV = dialog.findViewById(R.id.kababeTitleTV);

        Button saveResturantBtn = dialog.findViewById(R.id.saveResturantBtn);

        shawermaTV.setText(MenuResturantAdapter.numberArray[0] + "");
        burgerTV.setText(MenuResturantAdapter.numberArray[1] + "");
        pizzeTV.setText(MenuResturantAdapter.numberArray[2] + "");
        pasteTV.setText(MenuResturantAdapter.numberArray[3] + "");
        kababeTV.setText(MenuResturantAdapter.numberArray[4] + "");

        if (MenuResturantAdapter.numberArray[0] == 0){
            shawermaTitleTV.setVisibility(View.GONE);
            shawermaTV.setVisibility(View.GONE);
        }
        if (MenuResturantAdapter.numberArray[1] == 0){
            burgerTitleTV.setVisibility(View.GONE);
            burgerTV.setVisibility(View.GONE);

        }
        if (MenuResturantAdapter.numberArray[2] == 0){
            pizzeTitleTV.setVisibility(View.GONE);
            pizzeTV.setVisibility(View.GONE);

        }
        if (MenuResturantAdapter.numberArray[3] == 0){
            pastaTitleTV.setVisibility(View.GONE);
            pasteTV.setVisibility(View.GONE);

        } if (MenuResturantAdapter.numberArray[4] == 0){
            kababeTitleTV.setVisibility(View.GONE);
            kababeTV.setVisibility(View.GONE);

        }

        saveResturantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MenuResturantAdapter.numberArray[0] > 0 ||
                        MenuResturantAdapter.numberArray[1] > 0 ||
                        MenuResturantAdapter.numberArray[2] > 0 ||
                        MenuResturantAdapter.numberArray[3] > 0 ||
                        MenuResturantAdapter.numberArray[4] > 0) {

                    saveResturant(MenuResturantAdapter.numberArray[0],
                            MenuResturantAdapter.numberArray[1],
                            MenuResturantAdapter.numberArray[2],
                            MenuResturantAdapter.numberArray[3],
                            MenuResturantAdapter.numberArray[4]);

                    dialog.dismiss();
                } else {
                    Toast.makeText(ResidenceResturantActivity.this, "You Don't choose Any Meal ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
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

    private void saveResturant(int shawerma, int burger, int pizze, int paste, int kababe) {
        String uniqueID = UUID.randomUUID().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ResidenceResturant");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", uniqueID);
        hashMap.put("shawerma", shawerma);
        hashMap.put("burger", burger);
        hashMap.put("pizze", pizze);
        hashMap.put("paste", paste);
        hashMap.put("kababe", kababe);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("roomNumber", roomNumber);

        databaseReference.child(uniqueID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ResidenceResturantActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResidenceResturantActivity.this, "Add Faile,Please try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}