package com.example.myfood.requests;

import com.example.myfood.requests.responses.RecipeResponse;
import com.example.myfood.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    //searchRecipe Request
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("q") String query,
            @Query("page")String page
    );
    //get Recipe Request
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("rId")String rId
    );

}
