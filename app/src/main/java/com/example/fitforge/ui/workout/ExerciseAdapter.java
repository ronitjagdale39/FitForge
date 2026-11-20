package com.example.fitforge.ui.workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitforge.R;
import com.example.fitforge.data.remote.dto.exercise.Exercise;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int exerciseId);
    }
    
    private List<Exercise> exercises;
    private List<Exercise> exercisesFull; // For searching
    private OnItemClickListener listener;

    public ExerciseAdapter(List<Exercise> exercises, OnItemClickListener listener) {
        this.exercises = exercises;
        this.exercisesFull = new java.util.ArrayList<>(exercises);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.tvExerciseName.setText(exercise.getName());
        holder.tvPrimaryMuscle.setText(exercise.getPrimaryMuscle());
        
        // Combine equipment and type for the new UI
        String equipmentType = exercise.getEquipment() + " • " + exercise.getExerciseType();
        holder.tvEquipmentType.setText(equipmentType);
        
        // Bind click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(exercise.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    public void updateData(List<Exercise> newExercises) {
        this.exercises = newExercises;
        this.exercisesFull = new java.util.ArrayList<>(newExercises);
        notifyDataSetChanged();
    }
    
    public void filter(String text) {
        exercises.clear();
        if (text.isEmpty()) {
            exercises.addAll(exercisesFull);
        } else {
            text = text.toLowerCase();
            for (Exercise item : exercisesFull) {
                if (item.getName().toLowerCase().contains(text) || item.getPrimaryMuscle().toLowerCase().contains(text)) {
                    exercises.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName, tvPrimaryMuscle, tvEquipmentType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
            tvPrimaryMuscle = itemView.findViewById(R.id.tv_primary_muscle);
            tvEquipmentType = itemView.findViewById(R.id.tv_equipment_type);
        }
    }
}