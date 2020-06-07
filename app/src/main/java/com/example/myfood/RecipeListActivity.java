package com.example.myfood;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;


import com.example.myfood.Models.Recipe;
import com.example.myfood.adapters.OnRecipeListener;
import com.example.myfood.adapters.RecipeRecyclerAdapter;
import com.example.myfood.util.VerticalSpacingDecorator;
import com.example.myfood.viewmodels.RecipeListViewModel;


import java.util.List;

public class RecipeListActivity extends BasicActivity implements
        OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    //var
    RecipeListViewModel mRecipeListViewModel;
    RecipeRecyclerAdapter mRecipeRecyclerAdapter;
    //ui component
    RecyclerView mRecyclerView_recipe;
    SearchView mSearchView_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        findViews();
        init();
        subscribeToLiveData();
        Log.d(TAG, "onCreate: sssssssssssssssssssssstaaaaaaaaaaaaarted");
        if(!mRecipeListViewModel.isViewingRecipes()){
            // display search categories
            displaySearchCategories();
        }

    }
    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mRecipeRecyclerAdapter.displaySearchCategories();
    }
    private void findViews() {
        mRecyclerView_recipe = findViewById(R.id.recyclerView_recipe);
        mSearchView_main = findViewById(R.id.searchView_main);
    }

    private void init() {
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initRecycler();
        initSearchView();
    }
    private void initSearchView(){
        mSearchView_main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecipeRecyclerAdapter.displayLoading();
                mRecipeListViewModel.searchRecipeApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void initRecycler() {
        mRecipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView_recipe.addItemDecoration(new VerticalSpacingDecorator(30));
        mRecyclerView_recipe.setAdapter(mRecipeRecyclerAdapter);
        mRecyclerView_recipe.setLayoutManager(new LinearLayoutManager(this));
    }

    private void subscribeToLiveData() {
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes!=null&&mRecipeListViewModel.isViewingRecipes())
                mRecipeRecyclerAdapter.setRecipes(recipes);
            }
        });
    }


    @Override
    public void OnRecipeClick(int position) {
        Log.d(TAG, "OnRecipeClick: " + position + " clicked");
    }

    @Override
    public void OnCategoryClick(String category) {
        mRecipeRecyclerAdapter.displayLoading();
        mRecipeListViewModel.searchRecipeApi(category, 1);
    }
    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }else
            displaySearchCategories();

    }
}
