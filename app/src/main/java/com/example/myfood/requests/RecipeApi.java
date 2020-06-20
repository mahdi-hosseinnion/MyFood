package com.example.myfood.requests;

import com.example.myfood.requests.responses.ApiResponse;
import com.example.myfood.requests.responses.RecipeResponse;
import com.example.myfood.requests.responses.RecipeSearchResponse;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    //searchRecipe Request
    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
            @Query("q") String query,
            @Query("page")String page
    );
    //get Recipe Request
    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("rId")String rId
    );

}
