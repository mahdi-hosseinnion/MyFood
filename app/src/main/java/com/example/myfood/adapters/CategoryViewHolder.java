package com.example.myfood.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.myfood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener {
    public CircleImageView img_category_image;
    public TextView txt_category_title;
    OnRecipeListener onRecipeListener;
    public CategoryViewHolder(@NonNull View itemView,OnRecipeListener onRecipeListener) {
        super(itemView);
        img_category_image=itemView.findViewById(R.id.img_category_image);
        txt_category_title=itemView.findViewById(R.id.txt_category_title);
        this.onRecipeListener=onRecipeListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.OnCategoryClick(txt_category_title.getText().toString());
    }
}
