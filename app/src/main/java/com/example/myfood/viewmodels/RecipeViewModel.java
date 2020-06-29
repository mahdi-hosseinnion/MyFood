package com.example.myfood.viewmodels;

import android.app.Application;

import com.example.myfood.Models.Recipe;
import com.example.myfood.repositories.RecipeRepository;
import com.example.myfood.util.Resource;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RecipeViewModel extends AndroidViewModel{
    //var
    private RecipeRepository mRecipeRepository;
    public RecipeViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository=RecipeRepository.getInstance(application);
    }
    public LiveData<Resource<Recipe>> searchRecipeApi(String recipeId){
        return mRecipeRepository.searchRecipeApi(recipeId);
    }
}
