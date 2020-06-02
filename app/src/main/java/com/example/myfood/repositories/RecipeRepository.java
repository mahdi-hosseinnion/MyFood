package com.example.myfood.repositories;

import com.example.myfood.Models.Recipe;
import com.example.myfood.requests.RecipeApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        recipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeApiClient.getRecipes();
    }
}
