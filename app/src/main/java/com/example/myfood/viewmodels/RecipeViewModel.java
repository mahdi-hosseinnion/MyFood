package com.example.myfood.viewmodels;

import com.example.myfood.Models.Recipe;
import com.example.myfood.repositories.RecipeRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;
    private String mRecipeId;

    public RecipeViewModel() {
        mRecipeRepository=RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeById(String rId){
        mRecipeRepository.searchRecipeById(rId);
        mRecipeId=rId;
    }

    public String getRecipeId() {
        return mRecipeId;
    }
}
