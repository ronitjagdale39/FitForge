package com.example.fitforge.ui.nutrition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitforge.R;
import com.example.fitforge.data.remote.dto.nutrition.Food;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Food food);
    }

    private List<Food> foods;
    private OnItemClickListener listener;

    public FoodAdapter(List<Food> foods, OnItemClickListener listener) {
        this.foods = foods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.tvName.setText(food.getName());
        holder.tvCalories.setText((int)food.getCalories() + " kcal");
        
        String macros = String.format("P: %dg • C: %dg • F: %dg", 
            (int)food.getProtein(), (int)food.getCarbohydrates(), (int)food.getFats());
        holder.tvMacros.setText(macros);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods != null ? foods.size() : 0;
    }

    public void updateData(List<Food> newFoods) {
        this.foods = newFoods;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMacros, tvCalories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvMacros = itemView.findViewById(R.id.tv_food_macros);
            tvCalories = itemView.findViewById(R.id.tv_food_calories);
        }
    }
}