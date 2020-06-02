package com.example.myfood.repositories;

import com.example.myfood.Models.Recipe;
import com.example.myfood.requests.RecipeApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient RecipeApiClient;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        RecipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return RecipeApiClient.getRecipes();
    }
    public void searchRecipeApi(String query,int pageNO){
        if (pageNO<1)
            pageNO=1;
        RecipeApiClient.searchRecipeApi(query,pageNO);
    }
}
