package com.example.myfood.viewmodels;

import com.example.myfood.Models.Recipe;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {
    public RecipeListViewModel() {
    }
    private MutableLiveData<List<Recipe>> mRecipes=new MutableLiveData<>();

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
}
