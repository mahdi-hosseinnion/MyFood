package com.example.myfood.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.myfood.Models.Recipe;
import com.example.myfood.R;
import com.example.myfood.util.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //constants
    private final int RECIPE_TYPE = 1;
    private final int LOADING_TYPE = 2;
    private final int CATEGORY_TYPE = 3;
    private final int EXHAUSTED_TYPE = 4;
    private final String LOADING = "LOADING...778";
    private final String EXHAUSTED = "EXHAUSTED...842";
    //var
    private List<Recipe> mRecipes;
    private OnRecipeListener onRecipeListener;
    private RequestManager mRequestManager;
    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener, RequestManager requestManager) {
        this.onRecipeListener = onRecipeListener;
        this.mRequestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view = null;
        switch (viewType) {
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exhausted_list_item, parent, false);
                return new ExhaustedViewHolder(view);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            case CATEGORY_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, onRecipeListener,mRequestManager);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, onRecipeListener,mRequestManager);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == RECIPE_TYPE) {
            ((RecipeViewHolder)holder).onBind(mRecipes.get(position));
        } else if (itemViewType == CATEGORY_TYPE) {
            ((CategoryViewHolder) holder).onBind(mRecipes.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecipes.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        } else if (mRecipes.get(position).getTitle().equals(LOADING)) {
            return LOADING_TYPE;
        } else if (mRecipes.get(position).getTitle().equals(EXHAUSTED)) {
            return EXHAUSTED_TYPE;
        } else
            return RECIPE_TYPE;
    }

    public void displaySearchCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        mRecipes = categories;
        notifyDataSetChanged();
    }

    public void displayQueryExhausted() {
        hideLoading();
        Recipe recipe = new Recipe();
        recipe.setTitle(EXHAUSTED);
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    public void hideLoading() {
        if (isLoading()) {
            if (mRecipes.get(0).getTitle().equals(LOADING)) {
                mRecipes.remove(0);
            } else if (mRecipes.get(mRecipes.size() - 1).getTitle().equals(LOADING)) {
                mRecipes.remove(mRecipes.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    //display loading during search request
    public void displayOnlyLoading() {
        clearRecipeList();
        Recipe recipe = new Recipe();
        recipe.setTitle(LOADING);
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    private void clearRecipeList() {
        if (mRecipes == null) {
            mRecipes = new ArrayList<>();
        } else
            mRecipes.clear();
        notifyDataSetChanged();
    }

    public void displayLoading() {
        if (mRecipes == null) {
            mRecipes = new ArrayList<>();
        }
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle(LOADING);
            mRecipes.add(recipe);
        }
        notifyDataSetChanged();

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

    public Recipe getSelectedRecipe(int position) {
        if (mRecipes != null && mRecipes.size() > 0) {
            return mRecipes.get(position);
        }
        return null;
    }
}
