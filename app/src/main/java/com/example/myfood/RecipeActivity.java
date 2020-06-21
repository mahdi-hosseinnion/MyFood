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
        subscribeToObservers();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
//            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
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

    private void subscribeToObservers() {
//        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
//            @Override
//            public void onChanged(Recipe recipe) {
//                if (recipe != null) {
//                    if (recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())) {
//                        setRecipeProperties(recipe);
//                        mRecipeViewModel.setDidRetrieveRecipe(true);
//                    }
//                }
//            }
//        });
//        mRecipeViewModel.isRecipeRequestTimeOut().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                Log.d(TAG, "onChanged: timed aBoolean " + aBoolean.toString());
//                Log.d(TAG, "onChanged: timed didRetrieveRecipe " + (!mRecipeViewModel.didRetrieveRecipe()));
//                if (aBoolean && !mRecipeViewModel.didRetrieveRecipe()) {
//                    displayErrorScreen("error retrieving data.Check network connection");
//                }
//            }
//        });

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
            mRecipeIngredientsContainer.removeAllViews();
            for (String ingredient : recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredientsContainer.addView(textView);
            }
            mParent.setVisibility(View.VISIBLE);
            showProgressBar(false);
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
