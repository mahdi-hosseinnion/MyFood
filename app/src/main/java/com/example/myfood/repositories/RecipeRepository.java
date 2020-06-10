package com.example.myfood.repositories;

import com.example.myfood.Models.Recipe;
import com.example.myfood.requests.RecipeApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;

    }

    private RecipeRepository() {
        mRecipeApiClient = mRecipeApiClient.getInstance();
        initMediators();
    }

    private void initMediators() {
        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                } else {
                    //search DataBase cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Recipe> recipeList) {
        if (recipeList != null) {
            if (recipeList.size() % 30!=0) {
                mIsQueryExhausted.setValue(true);
            }
        } else
            mIsQueryExhausted.setValue(true);

    }


    public void searchRecipeApi(String query, int pageNO) {
        if (pageNO < 1)
            pageNO = 1;
        mQuery = query;
        mPageNumber = pageNO;
        mIsQueryExhausted.setValue(false);
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

    //*get functions*//
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeApiClient.getRecipe();
    }

    public LiveData<Boolean> isRecipeRequestTimeOut() {
        return mRecipeApiClient.isRecipeRequestTimeOut();
    }

    public LiveData<Boolean> getIsQueryExhausted() {
        return mIsQueryExhausted;
    }
}