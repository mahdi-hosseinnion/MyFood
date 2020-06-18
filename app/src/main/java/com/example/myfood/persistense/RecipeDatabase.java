package com.example.myfood.persistense;

import android.content.Context;

import com.example.myfood.Models.Recipe;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.room.TypeConverters;


@Database(entities = {Recipe.class},version = 1)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {
    private static final String DATABASE_NAME="recipes_DB";

    private static RecipeDatabase instance;
    public static RecipeDatabase getInstance(final Context context){
        if (instance==null){
            instance= Room.databaseBuilder(
                    context.getApplicationContext(),
                    RecipeDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }
}
