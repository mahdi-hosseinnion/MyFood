package com.example.myfood.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.myfood.Models.Recipe;
import com.example.myfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener {
    public CircleImageView img_category_image;
    public TextView txt_category_title;
    OnRecipeListener onRecipeListener;
    public RequestManager mRequestManager;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener,RequestManager requestManager) {
        super(itemView);
        img_category_image = itemView.findViewById(R.id.img_category_image);
        txt_category_title = itemView.findViewById(R.id.txt_category_title);
        this.onRecipeListener = onRecipeListener;
        this.mRequestManager=requestManager;
        itemView.setOnClickListener(this);

    }

    public void onBind(Recipe recipe) {
        Uri path = Uri.parse("android.resource://com.example.myfood/drawable/" + recipe.getImage_url());
        mRequestManager
                .load(path)
                .into(img_category_image);

        txt_category_title.setText(recipe.getTitle());
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.OnCategoryClick(txt_category_title.getText().toString());
    }
}
