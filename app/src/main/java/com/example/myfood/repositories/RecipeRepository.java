package com.example.myfood.repositories;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.example.myfood.AppExecutors;
import com.example.myfood.Models.Recipe;
import com.example.myfood.persistense.RecipeDao;
import com.example.myfood.persistense.RecipeDatabase;
import com.example.myfood.requests.RecipeApiClient;
import com.example.myfood.requests.ServiceGenerator;
import com.example.myfood.requests.responses.ApiResponse;
import com.example.myfood.requests.responses.RecipeResponse;
import com.example.myfood.requests.responses.RecipeSearchResponse;
import com.example.myfood.util.Constants;
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
    private static final String TAG = "RecipeRepository";
    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRepository(context);
        }
        return instance;

    }

    public RecipeRepository(Context context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    public LiveData<Resource<List<Recipe>>> searchRecipeApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {
                if (item.getRecipes() != null) {
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];
                    int index = 0;
                    for (long rowId : recipeDao.insertRecipes((Recipe[]) item.getRecipes().toArray(recipes))) {
                        if (rowId == -1) {
                            //if the recipe already exist . i don't want to set the ingredients or timeStamp b/c
                            //they will be erased.
                            recipeDao.updateRecipe(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );

                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.getRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().
                        searchRecipe(
                                query,
                                String.valueOf(pageNumber)
                        );
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Recipe>> searchRecipeApi(final String recipeId) {
        return new NetworkBoundResource<Recipe, RecipeResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull RecipeResponse item) {
                if (item.getRecipe() != null) {
                    item.getRecipe().setTimeStamp((int) (System.currentTimeMillis() / 1000));
                    recipeDao.insertRecipe(item.getRecipe());
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Recipe data) {
                int currentTime = (int) (System.currentTimeMillis() / 1000);
                int lastRefresh = 0;
                if (data != null)
                    lastRefresh = data.getTimeStamp();
                Log.d(TAG, "shouldFetch: current time = "+currentTime);
                Log.d(TAG, "shouldFetch: last Refresh = "+lastRefresh);
                Log.d(TAG, "shouldFetch: it's been "+((currentTime-lastRefresh)/60/60/24)+"days since last refresh");
                if (currentTime - lastRefresh >= Constants.RECIPE_REFRESH_TIME) {
                    Log.d(TAG, "shouldFetch: true");
                    return true;
                } else {
                    Log.d(TAG, "shouldFetch: false");
                    return false;
                }
            }

            @NonNull
            @Override
            protected LiveData<Recipe> loadFromDb() {
                return recipeDao.getRecipe(recipeId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().getRecipe(
                        recipeId
                );
            }
        }.getAsLiveData();
    }
}