package com.example.myfood.viewmodels;

import android.app.Application;

import com.example.myfood.Models.Recipe;
import com.example.myfood.repositories.RecipeRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends AndroidViewModel {
    public enum ViewState {CATEGORY, RECIPE}
    private MutableLiveData <ViewState> viewState;


    public RecipeListViewModel(@NonNull Application application) {
        super(application);
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
}
