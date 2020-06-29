package com.example.myfood;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myfood.Models.Recipe;
import com.example.myfood.util.Resource;
import com.example.myfood.viewmodels.RecipeViewModel;

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


    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            if (recipe != null)
                subscribeToObservers(recipe.getRecipe_id());
            Log.d(TAG, "onChanged: timed start ");

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

    private void subscribeToObservers(final String recipeId) {
        mRecipeViewModel.searchRecipeApi(recipeId).observe(this, new Observer<Resource<Recipe>>() {
            @Override
            public void onChanged(Resource<Recipe> recipeResource) {
                if (recipeResource != null) {
                    if (recipeResource.data != null) {
                        switch (recipeResource.status) {
                            case LOADING: {
                                showProgressBar(true);
                            }
                            case ERROR: {
                                Log.e(TAG, "onChanged: status: ERROR, Recipe: " + recipeResource.data.getTitle());
                                Log.e(TAG, "onChanged: status: ERROR message: " + recipeResource.message);
                                showProgressBar(false);
                                mParent.setVisibility(View.VISIBLE);
                                setRecipeProperties(recipeResource.data);
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed");
                                Log.d(TAG, "onChanged: status: SUCCESS, Recipe: " + recipeResource.data.getTitle());
                                showProgressBar(false);
                                mParent.setVisibility(View.VISIBLE);
                                setRecipeProperties(recipeResource.data);
                            }
                        }
                    }
                }
            }
        });
    }

    private void setRecipeProperties(Recipe recipe) {
        if (recipe != null) {
            RequestOptions RQ = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Glide.with(this)
                    .setDefaultRequestOptions(RQ)
                    .load(recipe.getImage_url())
                    .into(mRecipe_image);
            mRecipe_title.setText(recipe.getTitle());
            mRecipe_social_score.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
            setIngredients(recipe);
        }
    }

    private void setIngredients(Recipe recipe) {
        if (recipe.getIngredients() != null) {
            mRecipeIngredientsContainer.removeAllViews();
            for (String ingredient : recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredientsContainer.addView(textView);
            }
        } else {
            mRecipeIngredientsContainer.removeAllViews();
            TextView textView = new TextView(this);
            textView.setText("Error retrieving ingredients \n Check network connection");
            textView.setTextSize(15);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mRecipeIngredientsContainer.addView(textView);
        }

    }

    private void displayErrorScreen(String errorMessage) {
        RequestOptions RQ = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
        Glide.with(this)
                .setDefaultRequestOptions(RQ)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipe_image);
        mRecipe_title.setText("error reterieve data");
        mRecipe_social_score.setText("");
        mRecipeIngredientsContainer.removeAllViews();
        TextView textView = new TextView(this);
        if (!errorMessage.equals(""))
            textView.setText(errorMessage);
        else
            textView.setText("Error");
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRecipeIngredientsContainer.addView(textView);

        mParent.setVisibility(View.VISIBLE);
        showProgressBar(false);
    }
}
