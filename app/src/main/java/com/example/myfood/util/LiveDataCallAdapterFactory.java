package com.example.myfood.util;

import com.example.myfood.requests.responses.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.lifecycle.LiveData;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        /*
       * this method performs number of check and then return the response type for retrofit request.
       * @bodyType is the response type . it can be recipeResponse or RecipeSearchResponse
       * CHECK #1) return type return liveData
       * CHECK #2) type LiveData<T> is of ApiResponse class
       * CHECK #3) make sure ApiResponse is parameterized.AKA + APIResponse<T> exists.
         */

        //CHECK #1
        //make sure the CallAdapter is returning a type of LiveData
        if (CallAdapter.Factory.getRawType(returnType)!= LiveData.class){
            return null;
        }
        //CHECK #2
        //type that LiveData is wrapping.
        Type observableType= CallAdapter.Factory.getParameterUpperBound(0,(ParameterizedType)returnType);
        //check if it's of type ApiResponse
        Type rawObservalbeType=CallAdapter.Factory.getRawType(observableType);
        if (rawObservalbeType!= ApiResponse.class){
            throw new IllegalArgumentException("type must be defined resource");
        }
        //CHECK #3
        //check if ApiResponse is parameterized.AKA: Does ApiResponse<T> exist?(must wrap around T)
        //FYI = T is either RecipeResponse or T will be RecipeSearchResponse
        if (!(observableType instanceof ParameterizedType)){
            throw new IllegalArgumentException("resource must be parameterized");
        }
        Type bodyType=CallAdapter.Factory.getParameterUpperBound(0,(ParameterizedType)observableType);

        return new LiveDataCallAdapter<Type>(bodyType);
    }
}
