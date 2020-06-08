package com.example.myfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myfood.Models.Recipe;
import com.example.myfood.adapters.OnRecipeListener;
import com.example.myfood.adapters.RecipeRecyclerAdapter;
import com.example.myfood.util.VerticalSpacingDecorator;
import com.example.myfood.viewmodels.RecipeListViewModel;
import com.example.myfood.viewmodels.RecipeViewModel;

import java.util.List;

public class RecipeActivity extends BasicActivity {
    private static final String TAG = "RecipeActivity";
    //ui component
    private ImageView mRecipe_image;
    private TextView mRecipe_title, mRecipe_social_score;
    private ScrollView parent;
    private LinearLayout mRecipeIngredientsContainer;
    //var
    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_recipe);
        findViews();
        init();
        getIncomingIntent();
        subscribeToRecipeLiveData();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void findViews() {
        mRecipe_image = findViewById(R.id.recipe_image);
        mRecipe_title = findViewById(R.id.recipe_title);
        mRecipe_social_score = findViewById(R.id.recipe_social_score);
        mRecipe_image = findViewById(R.id.recipe_image);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
    }

    private void init() {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    }

    private void subscribeToRecipeLiveData() {
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    Log.d(TAG, "onChanged: --------------------------");
                    Log.d(TAG, "onChanged: " + recipe.toString());
                }
            }
        });
//        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
//            @Override
//            public void onChanged(Recipe recipe) {
//                if (recipe!=null){
//                Log.d(TAG, "onChanged: ------------------------------------------------------");
//                Log.d(TAG, "onChanged: "+recipe.toString());}
//            }
//        });

    }
}
