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

public class NutritionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(requireContext());
        textView.setText("FitForge Nutrition");
        textView.setTextSize(24);
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        return textView;
    }
}