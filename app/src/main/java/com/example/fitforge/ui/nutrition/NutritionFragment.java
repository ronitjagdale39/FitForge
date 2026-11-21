package com.example.fitforge.ui.nutrition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.fitforge.R;

import android.widget.Toast;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.text.TextWatcher;
import android.text.Editable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitforge.data.remote.ApiClient;
import com.example.fitforge.data.remote.NutritionApi;
import com.example.fitforge.data.remote.dto.nutrition.Food;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutritionFragment extends Fragment {

    private NutritionApi nutritionApi;
    private FoodAdapter adapter;
    private RecyclerView rvFoodSearch;
    private ProgressBar progressSearchFood;
    private View layoutNutritionDashboard;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
            
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        
        nutritionApi = ApiClient.getClient().create(NutritionApi.class);
        
        rvFoodSearch = view.findViewById(R.id.rv_food_search);
        progressSearchFood = view.findViewById(R.id.progress_search_food);
        layoutNutritionDashboard = view.findViewById(R.id.layout_nutrition_dashboard);
        
        rvFoodSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodAdapter(new ArrayList<>(), food -> {
            showFoodDetailDialog(food);
        });
        rvFoodSearch.setAdapter(adapter);
        
        EditText etSearchFood = view.findViewById(R.id.et_search_food);
        etSearchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    rvFoodSearch.setVisibility(View.GONE);
                    progressSearchFood.setVisibility(View.GONE);
                    layoutNutritionDashboard.setVisibility(View.VISIBLE);
                } else {
                    layoutNutritionDashboard.setVisibility(View.GONE);
                    searchFood(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup mock listeners for water tracking
        view.findViewById(R.id.btn_water_plus).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Added 0.5L of Water", Toast.LENGTH_SHORT).show()
        );
        
        view.findViewById(R.id.btn_water_minus).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Removed 0.5L of Water", Toast.LENGTH_SHORT).show()
        );
        
        return view;
    }
    
    private void searchFood(String query) {
        progressSearchFood.setVisibility(View.VISIBLE);
        rvFoodSearch.setVisibility(View.GONE);
        
        nutritionApi.searchFoods(query).enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                progressSearchFood.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                    rvFoodSearch.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Failed to search food", Toast.LENGTH_SHORT).show();
                    layoutNutritionDashboard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                progressSearchFood.setVisibility(View.GONE);
                layoutNutritionDashboard.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFoodDetailDialog(Food food) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_food_detail, null);
        builder.setView(dialogView);
        
        android.app.AlertDialog dialog = builder.create();
        
        android.widget.TextView tvName = dialogView.findViewById(R.id.tv_detail_food_name);
        android.widget.TextView tvUnit = dialogView.findViewById(R.id.tv_detail_food_unit);
        android.widget.TextView tvCalories = dialogView.findViewById(R.id.tv_detail_food_calories);
        android.widget.TextView tvProtein = dialogView.findViewById(R.id.tv_detail_protein);
        android.widget.TextView tvCarbs = dialogView.findViewById(R.id.tv_detail_carbs);
        android.widget.TextView tvFats = dialogView.findViewById(R.id.tv_detail_fats);
        android.widget.TextView tvFibre = dialogView.findViewById(R.id.tv_detail_fibre);
        
        tvName.setText(food.getName());
        tvUnit.setText("per " + (food.getBaseUnit() != null ? food.getBaseUnit() : "100g"));
        tvCalories.setText((int)food.getCalories() + " kcal");
        
        tvProtein.setText((int)food.getProtein() + "g");
        tvCarbs.setText((int)food.getCarbohydrates() + "g");
        tvFats.setText((int)food.getFats() + "g");
        tvFibre.setText((int)food.getFibre() + "g");
        
        dialogView.findViewById(R.id.btn_close_food_detail).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btn_add_food_detail).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Added " + food.getName() + " to meal", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            
            // Clear search and return to dashboard
            android.widget.EditText etSearchFood = getView().findViewById(R.id.et_search_food);
            if (etSearchFood != null) {
                etSearchFood.setText("");
            }
        });
        
        dialog.show();
    }
}