package com.example.myfood.viewmodels;

import com.example.myfood.Models.Recipe;
import com.example.myfood.repositories.RecipeRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipesRepository;
    public RecipeListViewModel() {
        mRecipesRepository=RecipeRepository.getInstance();
    }
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipesRepository.getRecipes();
    }
    public void searchRecipeApi(String query,int pageNO){
        mRecipesRepository.searchRecipeApi(query,pageNO);
    }
}
