package com.example.myfood.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     TextView txt_title,txt_publisher,txt_social_rank;
     ImageView recipe_image;
     private OnRecipeListener onRecipeListener;
    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {

        super(itemView);
        txt_title=itemView.findViewById(R.id.recipe_title);
        txt_publisher=itemView.findViewById(R.id.recipe_publisher);
        txt_social_rank=itemView.findViewById(R.id.recipe_social_score);
        recipe_image=itemView.findViewById(R.id.recipe_image);
        this.onRecipeListener=onRecipeListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    onRecipeListener.OnRecipeClick(getAdapterPosition());
    }
}
