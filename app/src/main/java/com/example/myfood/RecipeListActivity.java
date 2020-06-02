package com.example.myfood;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.myfood.Models.Recipe;
import com.example.myfood.requests.RecipeApi;
import com.example.myfood.requests.ServiceGenerator;
import com.example.myfood.requests.responses.RecipeResponse;
import com.example.myfood.viewmodels.RecipeListViewModel;


import java.io.IOException;
import java.util.List;

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
        subscribeToLiveData();
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequests();
            }
        });

    }
    private void subscribeToLiveData(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                for(Recipe recipe:recipes){
                    Log.d(TAG, "onChanged: "+recipe.getTitle());
                }
            }
        });
    }
    private void testRetrofitRequests() {
        recipeListViewModel.searchRecipeApi("chicken",1);
    }
}
