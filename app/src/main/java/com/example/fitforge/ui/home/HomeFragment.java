package com.example.fitforge.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitforge.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView tvDate;
    private TextView tvGreeting;
    private TextView tvCalories;
    private TextView tvRemaining;
    private TextView tvCaloriePercent;
    private TextView tvWeeklyWorkouts;

    private CircularProgressIndicator calorieProgress;

    private static final int DAILY_TARGET = 2700;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );

        initializeViews(view);
        updateDashboard();

        return view;
    }

    private void initializeViews(View view) {

        tvDate = view.findViewById(R.id.tv_date);
        tvGreeting = view.findViewById(R.id.tv_greeting);
        tvCalories = view.findViewById(R.id.tv_calories);
        tvRemaining = view.findViewById(R.id.tv_remaining);

        tvCaloriePercent = view.findViewById(
                R.id.tv_calorie_percent
        );

        tvWeeklyWorkouts = view.findViewById(
                R.id.tv_weekly_workouts
        );

        calorieProgress = view.findViewById(
                R.id.calorie_progress
        );
    }

    private void updateDashboard() {

        // Temporary values until backend integration
        int consumedCalories = 0;
        int completedWorkouts = 0;

        Calendar calendar = Calendar.getInstance();

        String date = new SimpleDateFormat(
                "EEEE, dd MMMM",
                Locale.getDefault()
        ).format(new Date());

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;

        if (hour < 12) {
            greeting = "Good morning, Ronit.";
        } else if (hour < 17) {
            greeting = "Good afternoon, Ronit.";
        } else {
            greeting = "Good evening, Ronit.";
        }

        int remaining = Math.max(
                0,
                DAILY_TARGET - consumedCalories
        );

        int progress = Math.min(
                100,
                consumedCalories * 100 / DAILY_TARGET
        );

        tvDate.setText(date);

        tvGreeting.setText(greeting);

        tvCalories.setText(
                consumedCalories + " kcal"
        );

        tvRemaining.setText(
                remaining + " kcal remaining"
        );

        tvCaloriePercent.setText(
                progress + "%"
        );

        calorieProgress.setProgress(progress);

        tvWeeklyWorkouts.setText(
                completedWorkouts + " Workouts completed"
        );
    }
}