package com.example.myfood.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myfood.Models.Recipe;
import com.example.myfood.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Recipe> mRecipes;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(List<Recipe> mRecipes, OnRecipeListener onRecipeListener) {
        this.mRecipes = mRecipes;
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item,parent,false);
        return new RecipeViewHolder(view,onRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Recipe recipe=mRecipes.get(position);
        ((RecipeViewHolder)holder).txt_title.setText(recipe.getTitle());
        ((RecipeViewHolder)holder).txt_publisher.setText(recipe.getPublisher());
        ((RecipeViewHolder)holder).txt_social_rank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        //setImage
        RequestOptions RQ = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);
        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(RQ)
                .load(recipe.getImage_url())
                .into(((RecipeViewHolder)holder).recipe_image);
    }

    @Override
    public int getItemCount() {
        if (mRecipes!=null)
            return mRecipes.size();
        return 0;
    }
    public void setRecipes(List<Recipe>recipes){
        this.mRecipes=recipes;
        notifyDataSetChanged();
    }
}
