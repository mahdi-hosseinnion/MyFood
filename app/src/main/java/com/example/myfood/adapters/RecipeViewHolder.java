package com.example.myfood.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.myfood.Models.Recipe;
import com.example.myfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txt_title, txt_publisher, txt_social_rank;
    ImageView recipe_image;
    private OnRecipeListener onRecipeListener;
    private RequestManager mRequestManager;
    public ViewPreloadSizeProvider mViewPreloadSizeProvider;
    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener,RequestManager requestManager,ViewPreloadSizeProvider viewPreloadSizeProvider) {

        super(itemView);
        txt_title = itemView.findViewById(R.id.recipe_title);
        txt_publisher = itemView.findViewById(R.id.recipe_publisher);
        txt_social_rank = itemView.findViewById(R.id.recipe_social_score);
        recipe_image = itemView.findViewById(R.id.recipe_image);
        this.onRecipeListener = onRecipeListener;
        this.mRequestManager=requestManager;
        this.mViewPreloadSizeProvider=viewPreloadSizeProvider;
        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe) {

        txt_title.setText(recipe.getTitle());
        txt_publisher.setText(recipe.getPublisher());
        txt_social_rank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
        //setImage
        mRequestManager.load(recipe.getImage_url())
                .into(recipe_image);
        mViewPreloadSizeProvider.setView(recipe_image);
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.OnRecipeClick(getAdapterPosition());
    }
}
