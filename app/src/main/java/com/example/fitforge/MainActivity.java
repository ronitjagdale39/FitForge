package com.example.fitforge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fitforge.ui.home.HomeFragment;
import com.example.fitforge.ui.workout.WorkoutFragment;
import com.example.fitforge.ui.nutrition.NutritionFragment;
import com.example.fitforge.ui.progress.ProgressFragment;
import com.example.fitforge.ui.coach.CoachFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Load Home screen initially
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Handle bottom navigation clicks
        bottomNavigation.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();

            } else if (itemId == R.id.nav_workout) {
                selectedFragment = new WorkoutFragment();

            } else if (itemId == R.id.nav_nutrition) {
                selectedFragment = new NutritionFragment();

            } else if (itemId == R.id.nav_progress) {
                selectedFragment = new ProgressFragment();

            } else if (itemId == R.id.nav_coach) {
                selectedFragment = new CoachFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}