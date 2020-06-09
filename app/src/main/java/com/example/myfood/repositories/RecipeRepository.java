package com.example.myfood.repositories;

import com.example.myfood.Models.Recipe;
import com.example.myfood.requests.RecipeApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        mRecipeApiClient = mRecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeApiClient.getRecipes();
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeApiClient.getRecipe();
    }

    public LiveData<Boolean> isRecipeRequestTimeOut() {
        return mRecipeApiClient.isRecipeRequestTimeOut();
    }

    public void searchRecipeApi(String query, int pageNO) {
        if (pageNO < 1)
            pageNO = 1;
        mQuery = query;
        mPageNumber = pageNO;
        mRecipeApiClient.searchRecipeApi(query, pageNO);
    }

    public void searchRecipeById(String rId) {
        mRecipeApiClient.searchRecipeById(rId);
    }

    public void searchNextPage() {
        searchRecipeApi(mQuery, mPageNumber + 1);
    }

    public void cancelRequest() {
        mRecipeApiClient.cancelRequest();
    }
}
