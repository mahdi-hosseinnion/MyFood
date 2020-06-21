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


import com.example.myfood.Models.Recipe;
import com.example.myfood.adapters.OnRecipeListener;
import com.example.myfood.adapters.RecipeRecyclerAdapter;
import com.example.myfood.util.Resource;
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
        subscribeObservers();

//        if (!mRecipeListViewModel.isViewingRecipes()) {
//            // display search categories
//            displaySearchCategories();
//        }

    }

    private void displaySearchCategories() {
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
//                if (!mRecyclerView_recipe.canScrollVertically(1)) {
//                    mRecipeListViewModel.searchNextPage();
//                }

            }
        });
    }

    private void subscribeObservers() {
        mRecipeListViewModel.getViewState().observe(this, new Observer<RecipeListViewModel.ViewState>() {
            @Override
            public void onChanged(RecipeListViewModel.ViewState viewState) {
                if (viewState != null)
                    switch (viewState) {
                        case RECIPE:
                            break;
                        case CATEGORY:
                            displaySearchCategories();
                            break;
                    }
            }
        });
        mRecipeListViewModel.getmRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if (listResource != null) {
                    Log.d(TAG, "onChanged: status " + listResource.status);
                    if (listResource.data != null) {
                        for (Recipe recipe : listResource.data) {
                            Log.d(TAG, "onChanged: -----" + recipe.getTitle());
                        }
                    }
                }
            }
        });
    }

    private void searchRecipeApi(String query) {
        mRecipeListViewModel.searchRecipeApi(query, 1);
    }

    private void initSearchView() {
        mSearchView_main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView_main.clearFocus();
                mRecipeRecyclerAdapter.displayLoading();
                searchRecipeApi(query);
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
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mRecipeRecyclerAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void OnCategoryClick(String category) {
        mSearchView_main.clearFocus();
        mRecipeRecyclerAdapter.displayLoading();
        searchRecipeApi(category);
//        mRecipeListViewModel.searchRecipeApi(category, 1);
    }

    @Override
    public void onBackPressed() {
//        if (mRecipeListViewModel.onBackPressed()) {
//            super.onBackPressed();
//        } else
//            displaySearchCategories();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set menu to toolbar
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //set onClick for menu
        if (item.getItemId() == R.id.action_categories)
            displaySearchCategories();
        return super.onOptionsItemSelected(item);
    }
}
