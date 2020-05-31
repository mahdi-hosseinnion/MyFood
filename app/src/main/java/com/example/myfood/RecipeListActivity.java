package com.example.myfood;

import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;



import com.example.myfood.requests.RecipeApi;
import com.example.myfood.requests.ServiceGenerator;
import com.example.myfood.requests.responses.RecipeResponse;
import com.example.myfood.viewmodels.RecipeListViewModel;


import java.io.IOException;

public class RecipeListActivity extends BasicActivity {
    //var
    private static final String TAG = "RecipeListActivity";
    //objects
    RecipeListViewModel recipeListViewModel;

    //ui component
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeListViewModel= ViewModelProviders.of(this).get(RecipeListViewModel.class);

    }

    private void testRetrofitRequests() {
        final RecipeApi recipeApi = ServiceGenerator.getRecipeApi();
//        Call<RecipeSearchResponse> responseCall =
//                recipeApi.searchRecipe("chicken","1");
//        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
//            @Override
//            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
//                if (response.code() == 200) {
//                    for (Recipe recipe : response.body().getRecipes()) {
//                        Log.d(TAG, "onResponse: " + recipe.toString());
//                    }
//                } else {
//
//                    try {
//                        Log.e(TAG, "onResponse: " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: call failed");
//            }
//        });
        Call<RecipeResponse> responseCall =
                recipeApi.getRecipe("39468");
        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.code() == 200) {

                    Log.d(TAG, "onResponse: " + response.body().getRecipe().toString());
                } else {
                    try {
                        Log.e(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: call failed");
            }
        });
    }
}
