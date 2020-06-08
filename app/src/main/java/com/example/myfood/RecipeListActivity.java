package com.example.myfood;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;


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

        if (!mRecipeListViewModel.isViewingRecipes()) {
            // display search categories
            displaySearchCategories();
        }

    }

    private void displaySearchCategories() {
        mRecipeListViewModel.setIsViewingRecipes(false);
        mRecipeRecyclerAdapter.displaySearchCategories();
    }

    private void findViews() {
        mRecyclerView_recipe = findViewById(R.id.recyclerView_recipe);
        mSearchView_main = findViewById(R.id.searchView_main);
    }

    private void init() {
        //set support action bar for add menu to toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initRecycler();
        initSearchView();
    }

    private void initRecycler() {
        mRecipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        mRecyclerView_recipe.addItemDecoration(new VerticalSpacingDecorator(30));
        mRecyclerView_recipe.setAdapter(mRecipeRecyclerAdapter);
        mRecyclerView_recipe.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView_recipe.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView_recipe.canScrollVertically(1)){
                    mRecipeListViewModel.searchNextPage();
                }

            }
        });
    }

    private void subscribeToLiveData() {
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null && mRecipeListViewModel.isViewingRecipes()){
                    mRecipeListViewModel.setIsPerformingQuery(false);
                    mRecipeRecyclerAdapter.setRecipes(recipes);
                }
            }
        });
    }

    private void initSearchView() {
        mSearchView_main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView_main.clearFocus();
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


    @Override
    public void OnRecipeClick(int position) {
        Intent intent=new Intent(this,RecipeActivity.class);
        intent.putExtra("recipe",mRecipeRecyclerAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void OnCategoryClick(String category) {
        mSearchView_main.clearFocus();
        mRecipeRecyclerAdapter.displayLoading();
        mRecipeListViewModel.searchRecipeApi(category, 1);
    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.onBackPressed()) {
            super.onBackPressed();
        } else
            displaySearchCategories();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set menu to toolbar
        getMenuInflater().inflate(R.menu.recipe_search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //set onClick for menu
        if (item.getItemId()==R.id.action_categories)
            displaySearchCategories();
        return super.onOptionsItemSelected(item);
    }
}
