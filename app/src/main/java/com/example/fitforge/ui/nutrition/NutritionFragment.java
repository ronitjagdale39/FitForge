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

public class NutritionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
            
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        
        // Setup mock listeners for water tracking
        view.findViewById(R.id.btn_water_plus).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Added 0.5L of Water", Toast.LENGTH_SHORT).show()
        );
        
        view.findViewById(R.id.btn_water_minus).setOnClickListener(v -> 
            Toast.makeText(getContext(), "Removed 0.5L of Water", Toast.LENGTH_SHORT).show()
        );
        
        return view;
    }
}