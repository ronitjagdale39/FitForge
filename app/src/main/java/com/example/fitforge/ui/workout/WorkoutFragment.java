package com.example.fitforge.ui.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforge.R;
import com.example.fitforge.data.remote.ApiClient;
import com.example.fitforge.data.remote.ExerciseApi;
import com.example.fitforge.data.remote.dto.exercise.Exercise;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutFragment extends Fragment {

    private RecyclerView rvExercises;
    private ProgressBar progressBar;
    private ExerciseAdapter adapter;
    private ExerciseApi exerciseApi;
    private TextView tvExerciseCount;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        
        rvExercises = view.findViewById(R.id.rv_exercises);
        progressBar = view.findViewById(R.id.progress_bar);
        tvExerciseCount = view.findViewById(R.id.tv_exercise_count);
        
        exerciseApi = ApiClient.getClient().create(ExerciseApi.class);
        
        rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExerciseAdapter(new ArrayList<>(), new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int exerciseId) {
                fetchAndShowExerciseDetails(exerciseId);
            }
        });
        rvExercises.setAdapter(adapter);
        
        view.findViewById(R.id.fab_add_exercise).setOnClickListener(v -> showAddExerciseDialog());
        
        // Setup Search Listener
        android.widget.EditText etSearch = view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Handle filter chips (Visual only as per constraints)
        setupFilterChips(view);
        
        fetchExercises();

        return view;
    }
    
    private void fetchAndShowExerciseDetails(int id) {
        progressBar.setVisibility(View.VISIBLE);
        exerciseApi.getExerciseById(id).enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    showExerciseDetailDialog(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to fetch details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showExerciseDetailDialog(Exercise exercise) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_exercise_detail, null);
        builder.setView(dialogView);
        
        android.app.AlertDialog dialog = builder.create();
        
        TextView tvName = dialogView.findViewById(R.id.tv_detail_name);
        TextView tvDesc = dialogView.findViewById(R.id.tv_detail_description);
        TextView tvPrimary = dialogView.findViewById(R.id.tv_detail_primary);
        TextView tvEquipment = dialogView.findViewById(R.id.tv_detail_equipment);
        TextView tvType = dialogView.findViewById(R.id.tv_detail_type);
        
        tvName.setText(exercise.getName());
        
        if (exercise.getDescription() != null && !exercise.getDescription().isEmpty()) {
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(exercise.getDescription());
        }
        
        tvPrimary.setText(exercise.getPrimaryMuscle());
        tvEquipment.setText(exercise.getEquipment());
        tvType.setText(exercise.getExerciseType());
        
        dialogView.findViewById(R.id.btn_close_detail).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btn_edit_detail).setOnClickListener(v -> {
            dialog.dismiss();
            showEditExerciseDialog(exercise);
        });
        
        dialogView.findViewById(R.id.btn_delete_detail).setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(getContext())
                .setTitle("Delete Exercise")
                .setMessage("Are you sure you want to delete this exercise?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    dialog.dismiss();
                    deleteExercise(exercise.getId());
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
        
        dialog.show();
    }
    
    private void deleteExercise(int id) {
        progressBar.setVisibility(View.VISIBLE);
        exerciseApi.deleteExercise(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();
                    fetchExercises();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditExerciseDialog(Exercise exercise) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);
        
        android.app.AlertDialog dialog = builder.create();
        
        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        if (tvTitle != null) {
            tvTitle.setText("Edit Exercise");
        }
        
        android.widget.EditText etName = dialogView.findViewById(R.id.et_exercise_name);
        android.widget.EditText etMuscle = dialogView.findViewById(R.id.et_primary_muscle);
        android.widget.EditText etEquipment = dialogView.findViewById(R.id.et_equipment);
        android.widget.EditText etType = dialogView.findViewById(R.id.et_exercise_type);
        
        // Pre-fill existing data
        etName.setText(exercise.getName());
        etMuscle.setText(exercise.getPrimaryMuscle());
        etEquipment.setText(exercise.getEquipment());
        etType.setText(exercise.getExerciseType());
        
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String muscle = etMuscle.getText().toString().trim();
            String equipment = etEquipment.getText().toString().trim();
            String type = etType.getText().toString().trim();
            
            if (name.isEmpty() || muscle.isEmpty() || equipment.isEmpty() || type.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            
            progressBar.setVisibility(View.VISIBLE);
            dialog.dismiss();
            
            com.example.fitforge.data.remote.dto.exercise.ExerciseUpdateRequest request = 
                new com.example.fitforge.data.remote.dto.exercise.ExerciseUpdateRequest(name, muscle, equipment, type);
                
            exerciseApi.updateExercise(exercise.getId(), request).enqueue(new Callback<Exercise>() {
                @Override
                public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Exercise updated!", Toast.LENGTH_SHORT).show();
                        fetchExercises(); 
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed to update exercise", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Exercise> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        
        dialog.show();
    }
    
    private void setupFilterChips(View view) {
        // Simple visual toggle for the "All" chip as demonstration
        TextView chipAll = view.findViewById(R.id.chip_all);
        TextView chipChest = view.findViewById(R.id.chip_chest);
        
        chipChest.setOnClickListener(v -> {
            chipChest.setBackgroundResource(R.drawable.bg_chip_selected);
            chipChest.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            
            chipAll.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        });
        
        chipAll.setOnClickListener(v -> {
            chipAll.setBackgroundResource(R.drawable.bg_chip_selected);
            chipAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            
            chipChest.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipChest.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        });
    }
    
    private void showAddExerciseDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);
        
        android.app.AlertDialog dialog = builder.create();
        
        android.widget.EditText etName = dialogView.findViewById(R.id.et_exercise_name);
        android.widget.EditText etMuscle = dialogView.findViewById(R.id.et_primary_muscle);
        android.widget.EditText etEquipment = dialogView.findViewById(R.id.et_equipment);
        android.widget.EditText etType = dialogView.findViewById(R.id.et_exercise_type);
        
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String muscle = etMuscle.getText().toString().trim();
            String equipment = etEquipment.getText().toString().trim();
            String type = etType.getText().toString().trim();
            
            if (name.isEmpty() || muscle.isEmpty() || equipment.isEmpty() || type.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            
            progressBar.setVisibility(View.VISIBLE);
            dialog.dismiss();
            
            com.example.fitforge.data.remote.dto.exercise.ExerciseCreateRequest request = 
                new com.example.fitforge.data.remote.dto.exercise.ExerciseCreateRequest(name, muscle, equipment, type);
                
            exerciseApi.createExercise(request).enqueue(new Callback<Exercise>() {
                @Override
                public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Exercise created!", Toast.LENGTH_SHORT).show();
                        fetchExercises(); // Refresh the list
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed to create exercise", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Exercise> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        
        dialog.show();
    }
    
    private void fetchExercises() {
        progressBar.setVisibility(View.VISIBLE);
        exerciseApi.getExercises().enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Exercise> fetchedExercises = response.body();
                    adapter.updateData(fetchedExercises);
                    
                    // Update exercise count
                    int count = fetchedExercises.size();
                    tvExerciseCount.setText(count + (count == 1 ? " Exercise" : " Exercises"));
                } else {
                    Toast.makeText(getContext(), "Failed to load exercises", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}