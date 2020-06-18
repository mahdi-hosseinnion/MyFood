package com.example.myfood.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {
    private String title;
    private String publisher;
    @PrimaryKey(autoGenerate = true)
    private String recipe_id;
    private String image_url;
    private String[]  ingredients;
    private float  social_rank;
    private int timeStamp;

    public Recipe() {
    }
@Ignore
    public Recipe(String title, String publisher, String recipe_id, String image_url, String[] ingredients, float social_rank, int timeStamp) {
        this.title = title;
        this.publisher = publisher;
        this.recipe_id = recipe_id;
        this.image_url = image_url;
        this.ingredients = ingredients;
        this.social_rank = social_rank;
        this.timeStamp = timeStamp;
    }
    @Ignore
    public Recipe(String title, String publisher, String image_url, String[] ingredients, float social_rank, int timeStamp) {
        this.title = title;
        this.publisher = publisher;
        this.image_url = image_url;
        this.ingredients = ingredients;
        this.social_rank = social_rank;
        this.timeStamp = timeStamp;
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        recipe_id = in.readString();
        image_url = in.readString();
        ingredients = in.createStringArray();
        social_rank = in.readFloat();
        timeStamp = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publisher);
        dest.writeString(recipe_id);
        dest.writeString(image_url);
        dest.writeStringArray(ingredients);
        dest.writeFloat(social_rank);
        dest.writeInt(timeStamp);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public float getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(float social_rank) {
        this.social_rank = social_rank;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static Creator<Recipe> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", recipe_id='" + recipe_id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", social_rank=" + social_rank +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
