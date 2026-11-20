package com.example.fitforge.data.remote;

import com.example.fitforge.data.remote.dto.exercise.Exercise;
import com.example.fitforge.data.remote.dto.exercise.ExerciseCreateRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface ExerciseApi {
    @GET("exercises/")
    Call<List<Exercise>> getExercises();
    
    @POST("exercises/")
    Call<Exercise> createExercise(@Body ExerciseCreateRequest request);

    @GET("exercises/{id}")
    Call<Exercise> getExerciseById(@Path("id") int id);
    
    @PUT("exercises/{id}")
    Call<Exercise> updateExercise(@Path("id") int id, @Body com.example.fitforge.data.remote.dto.exercise.ExerciseUpdateRequest request);
    
    @DELETE("exercises/{id}")
    Call<Void> deleteExercise(@Path("id") int id);
}