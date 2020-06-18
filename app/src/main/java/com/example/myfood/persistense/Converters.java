package com.example.myfood.persistense;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static String[] fromString(String value){
        Type listType =new TypeToken<String[]>(){}.getType();
        return new Gson().fromJson(value,listType);
    }
    @TypeConverter
    public static String fromArrayList(String[] list){
        Gson gson=new Gson();
        return gson.toJson(list);
    }

}
