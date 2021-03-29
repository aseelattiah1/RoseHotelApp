package com.Assel.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Assel.myapplication.R;

import java.util.List;

public class MenuResturantAdapter extends RecyclerView.Adapter<MenuResturantAdapter.ViewHolder> {

    Context context;
    List<MenuResturant> menuResturantList;
    public static int[] numberArray = {0, 0, 0, 0, 0};

    public MenuResturantAdapter(Context context, List<MenuResturant> menuResturantList) {
        this.context = context;
        this.menuResturantList = menuResturantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resturant_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuResturant resturant = menuResturantList.get(position);
        holder.imageMealIV.setImageResource(resturant.getImage());
        holder.nameMealTV.setText(resturant.getName());
        holder.costMealTV.setText(resturant.getCost());
        holder.mealNumberTV.setText(0 + "");
        numberArray[position] = 0;

        holder.addMealTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberArray[position]++;
                holder.mealNumberTV.setText(numberArray[position] + "");
            }
        });

        holder.removeMealTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberArray[position] > 0) {
                    numberArray[position]--;
                    holder.mealNumberTV.setText(numberArray[position] + "");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuResturantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageMealIV;
        TextView addMealTV, removeMealTV, mealNumberTV,
                nameMealTV, costMealTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMealIV = itemView.findViewById(R.id.imageMealIV);

            addMealTV = itemView.findViewById(R.id.addMealTV);
            removeMealTV = itemView.findViewById(R.id.removeMealTV);
            mealNumberTV = itemView.findViewById(R.id.mealNumberTV);
            nameMealTV = itemView.findViewById(R.id.nameMealTV);
            costMealTV = itemView.findViewById(R.id.costMealTV);

        }
    }
}
