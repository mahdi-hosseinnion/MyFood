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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    private ScrollView mParent;
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
        mParent = findViewById(R.id.parent);
    }

    private void init() {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        showProgressBar(true);
    }

    private void subscribeToRecipeLiveData() {
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    if (recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId()))
                    setRecipeProperties(recipe);
                }
            }
        });


    }

    private void setRecipeProperties(Recipe recipe) {
        if (recipe != null) {
            RequestOptions RQ=new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Glide.with(this)
                    .setDefaultRequestOptions(RQ)
                    .load(recipe.getImage_url())
                    .into(mRecipe_image);
            mRecipe_title.setText(recipe.getTitle());
            mRecipe_social_score.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
            mRecipeIngredientsContainer.removeAllViews();
            for (String ingredient:recipe.getIngredients()){
                TextView textView=new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredientsContainer.addView(textView);
            }
            mParent.setVisibility(View.VISIBLE);
            showProgressBar(false);
        }
    }
}
