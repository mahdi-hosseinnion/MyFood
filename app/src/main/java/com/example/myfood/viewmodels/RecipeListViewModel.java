package com.example.myfood.viewmodels;

import android.app.Application;
import android.util.Log;

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

    private static final String TAG = "RecipeListViewModel";
    private static final String QUERY_EXHAUSTED = "QUERY_EXHAUSTED BLA BLA BLA";
    private MutableLiveData<ViewState> viewState;
    private MediatorLiveData<Resource<List<Recipe>>> mRecipes = new MediatorLiveData<>();
    private RecipeRepository mRecipeRepository;
    //improving the query
    private boolean mIsQueryExhausted;
    private boolean mIsPerformingQuery;
    private int mPageNumber;
    private String mQuery;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository = RecipeRepository.getInstance(application);
        init();
    }

    private void init() {
        if (viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.CATEGORY);
        }
    }

    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }
    public void searchNextPage(){
        if (!mIsPerformingQuery&&!mIsQueryExhausted){
            mPageNumber++;
            executeSearch();
        }
    }
    public void searchRecipeApi(String query, int pageNumber) {
        if (!mIsPerformingQuery) {
            if (pageNumber == 0)
                pageNumber = 1;
            this.mPageNumber = pageNumber;
            this.mQuery = query;
            mIsQueryExhausted = false;
            executeSearch();
        }

    }

    private void executeSearch() {
        mIsPerformingQuery = true;
        viewState.setValue(ViewState.RECIPE);
        final LiveData<Resource<List<Recipe>>> repositorySource = mRecipeRepository.searchRecipeApi(mQuery, mPageNumber);
        mRecipes.addSource(repositorySource, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if (listResource != null) {
                    mRecipes.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        mIsPerformingQuery = false;
                        if (listResource.data != null) {
                            //TODO i think this if should be -> if (listResource.data.size()/30!=0){
                            if (listResource.data.size() == 0) {
                                Log.d(TAG, "onChanged: query is exhausted");
                                mRecipes.setValue(
                                        new Resource<List<Recipe>>(
                                                Resource.Status.ERROR,
                                                listResource.data,
                                                QUERY_EXHAUSTED
                                        )
                                );

                            }
                        }
                        mRecipes.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        mIsPerformingQuery = false;
                        mRecipes.removeSource(repositorySource);
                    }
                } else {
                    mRecipes.removeSource(repositorySource);
                }
            }
        });

    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return mRecipes;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
