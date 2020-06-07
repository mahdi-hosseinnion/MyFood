package com.example.myfood;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myfood.Models.Recipe;

import androidx.annotation.Nullable;

public class RecipeActivity extends BasicActivity {
    //ui component
    private ImageView mRecipe_image;
    private TextView mRecipe_title,mRecipe_social_score;
    private ScrollView parent;
    private LinearLayout mRecipeIngredientsContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_recipe);
        findViews();
        getIncomingIntent();
    }
    private void getIncomingIntent(){
        if (getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
        }
    }
    private void findViews(){
        mRecipe_image=findViewById(R.id.recipe_image);
        mRecipe_title=findViewById(R.id.recipe_title);
        mRecipe_social_score=findViewById(R.id.recipe_social_score);
        mRecipe_image=findViewById(R.id.recipe_image);
        mRecipeIngredientsContainer=findViewById(R.id.ingredients_container);
    }
}
