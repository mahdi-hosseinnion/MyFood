package com.example.myfood.repositories;

import android.content.Context;
import android.content.pm.ResolveInfo;

import com.example.myfood.AppExecutors;
import com.example.myfood.Models.Recipe;
import com.example.myfood.persistense.RecipeDao;
import com.example.myfood.persistense.RecipeDatabase;
import com.example.myfood.requests.RecipeApiClient;
import com.example.myfood.requests.responses.ApiResponse;
import com.example.myfood.requests.responses.RecipeSearchResponse;
import com.example.myfood.util.NetworkBoundResource;
import com.example.myfood.util.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class RecipeRepository {
    private static RecipeRepository instance;
    private RecipeDao recipeDao;
    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRepository(context);
        }
        return instance;

    }

    public RecipeRepository(Context context) {
        recipeDao= RecipeDatabase.getInstance(context).getRecipeDao();
    }
    public LiveData<Resource<List<Recipe>>> searchRecipeApi(final String query,final int pageNumber){
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.getRecipes(query,pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return null;
            }
        }.getAsLiveData();
    }
}