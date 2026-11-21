package com.example.fitforge.data.remote;

import com.example.fitforge.data.remote.dto.nutrition.Food;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NutritionApi {
    @GET("nutrition/")
    Call<List<Food>> searchFoods(@Query("query") String query);
}