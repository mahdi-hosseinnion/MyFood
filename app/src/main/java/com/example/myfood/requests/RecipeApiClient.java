package com.example.myfood.requests;

import com.example.myfood.AppExecutors;
import com.example.myfood.Models.Recipe;
import com.example.myfood.util.Constants;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RecipeApiClient {
    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    public static RecipeApiClient getInstance(){
        if (instance==null)
            instance=new RecipeApiClient();
        return instance;
    }

    private RecipeApiClient() {
        mRecipes=new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
    private void searchRecipeApi(String query,int page){

        final Future handler = AppExecutors.getInstance().getNetWordIO().submit(new Runnable() {
            @Override
            public void run() {
                //retrieve data form api
            }
        });
        AppExecutors.getInstance().getNetWordIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let user know its time out
                handler.cancel(true);
            }
        }, Constants.TIME_OUT, TimeUnit.MILLISECONDS);
    }


}
