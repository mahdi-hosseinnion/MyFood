package com.example.myfood.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myfood.Models.Recipe;
import com.example.myfood.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //constants
    public static final int RECIPE_TYPE = 1;
    public static final int LOADING_TYPE = 2;
    public static final String LOADING = "LOADING...";
    //var
    private List<Recipe> mRecipes;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view = null;
        switch (viewType) {
            case RECIPE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, onRecipeListener);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, onRecipeListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == RECIPE_TYPE) {
            Recipe recipe = mRecipes.get(position);
            ((RecipeViewHolder) holder).txt_title.setText(recipe.getTitle());
            ((RecipeViewHolder) holder).txt_publisher.setText(recipe.getPublisher());
            ((RecipeViewHolder) holder).txt_social_rank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
            //setImage
            RequestOptions RQ = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);
            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(RQ)
                    .load(recipe.getImage_url())
                    .into(((RecipeViewHolder) holder).recipe_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecipes.get(position).getTitle().equals(LOADING)) {
            return LOADING_TYPE;
        } else
            return RECIPE_TYPE;
    }

    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle(LOADING);
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            mRecipes = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mRecipes != null && mRecipes.size() > 0) {
            if (mRecipes.get(mRecipes.size() - 1).getTitle().equals(LOADING)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mRecipes != null)
            return mRecipes.size();
        return 0;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }
}
