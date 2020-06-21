package com.example.myfood.viewmodels;

import android.app.Application;

import com.example.myfood.Models.Recipe;
import com.example.myfood.repositories.RecipeRepository;
import com.example.myfood.util.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends AndroidViewModel {
    public enum ViewState {CATEGORY, RECIPE}
    private MutableLiveData <ViewState> viewState;
    private MediatorLiveData<Resource<List<Recipe>>> mRecipes=new MediatorLiveData<>();
    private RecipeRepository mRecipeRepository;
    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository=RecipeRepository.getInstance(application);
        init();
    }
    private void init(){
        if (viewState==null){
            viewState=new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORY);
        }
    }

    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }
    public void searchRecipeApi(String query,int pageNumber){
        final LiveData<Resource<List<Recipe>>> repositorySource=mRecipeRepository.searchRecipeApi(query,pageNumber);
        mRecipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                //react to data
                mRecipes.setValue(listResource);
            }
        });
    }

    public LiveData<Resource<List<Recipe>>> getmRecipes() {
        return mRecipes;
    }
}
