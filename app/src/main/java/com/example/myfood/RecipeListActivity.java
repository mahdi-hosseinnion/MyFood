package com.example.myfood;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;


import com.example.myfood.Models.Recipe;
import com.example.myfood.adapters.OnRecipeListener;
import com.example.myfood.adapters.RecipeRecyclerAdapter;
import com.example.myfood.requests.RecipeApi;
import com.example.myfood.requests.ServiceGenerator;
import com.example.myfood.requests.responses.RecipeResponse;
import com.example.myfood.viewmodels.RecipeListViewModel;


import java.io.IOException;
import java.util.List;

public class RecipeListActivity extends BasicActivity implements
        OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    //var
    RecipeListViewModel recipeListViewModel;
    RecipeRecyclerAdapter mRecipeRecyclerAdapter;
    //ui component
    RecyclerView recyclerView_recipe;
    SearchView searchView_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        findViews();
        init();
        subscribeToLiveData();


    }

    private void findViews() {
        recyclerView_recipe = findViewById(R.id.recyclerView_recipe);
        searchView_main = findViewById(R.id.searchView_main);
    }

    private void init() {
        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initRecycler();
        initSearchView();
    }
    private void initSearchView(){
        searchView_main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecipeRecyclerAdapter.displayLoading();
                recipeListViewModel.searchRecipeApi(query, 1);
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
        recyclerView_recipe.setAdapter(mRecipeRecyclerAdapter);
        recyclerView_recipe.setLayoutManager(new LinearLayoutManager(this));
    }

    private void subscribeToLiveData() {
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
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
        Log.d(TAG, "OnCategoryClick: " + category + " clicked");
    }
}
