package com.example.myfood.requests;

import android.util.Log;

import com.example.myfood.AppExecutors;
import com.example.myfood.Models.Recipe;
import com.example.myfood.requests.responses.RecipeSearchResponse;
import com.example.myfood.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient {
    private static final String TAG = "RecipeApiClient";
    //var
    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;

    public static RecipeApiClient getInstance() {
        if (instance == null)
            instance = new RecipeApiClient();
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public void searchRecipeApi(String query, int page) {
        if (mRetrieveRecipeRunnable!=null)
            mRetrieveRecipeRunnable=null;
        mRetrieveRecipeRunnable=new RetrieveRecipeRunnable(query,page);
        final Future handler = AppExecutors.getInstance().getNetWordIO().submit(mRetrieveRecipeRunnable);
        AppExecutors.getInstance().getNetWordIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let user know its time out
                handler.cancel(true);
            }
        }, Constants.TIME_OUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipeRunnable implements Runnable {
        private String query;
        private int pageNO;
        private boolean isCanceled;

        public RetrieveRecipeRunnable(String query, int pageNO) {
            this.query = query;
            this.pageNO = pageNO;
            isCanceled = false;
        }

        @Override
        public void run() {

            try {

                Response response = getRecipes(query, pageNO).execute();

                if (isCanceled) {
                    return;
                }
                if (response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNO == 1) {
                        mRecipes.postValue(list);

                    } else {
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(list);
                    }
                } else {
                    Log.e(TAG, "run: " + response.errorBody());
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNO) {
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    query,
                    String.valueOf(pageNO)
            );
        }

        public void cancelRequest() {
            isCanceled = true;
        }
    }
    public void cancelRequest(){
        if (mRetrieveRecipeRunnable!=null){
            mRetrieveRecipeRunnable.cancelRequest();
        }
    }
}
