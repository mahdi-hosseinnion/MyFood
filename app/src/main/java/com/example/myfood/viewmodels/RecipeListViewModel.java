package com.example.myfood.viewmodels;

import com.example.myfood.Models.Recipe;
import com.example.myfood.repositories.RecipeRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {
    private boolean mIsViewingRecipes;
    private RecipeRepository mRecipesRepository;
    public RecipeListViewModel() {

        mRecipesRepository=RecipeRepository.getInstance();
    }
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipesRepository.getRecipes();
    }
    public void searchRecipeApi(String query,int pageNO){
        mIsViewingRecipes = true;
        mRecipesRepository.searchRecipeApi(query,pageNO);
    }
    public boolean isViewingRecipes(){
        return mIsViewingRecipes;
    }
    public void setIsViewingRecipes(boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }
    public boolean onBackPressed(){
        if (isViewingRecipes()){
            mIsViewingRecipes=false;
            return false;
        }
        return true;
    }
}
