package com.example.myfood.requests;

import android.os.ParcelUuid;

import com.example.myfood.util.Constants;
import com.example.myfood.util.LiveDataCallAdapter;
import com.example.myfood.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myfood.util.Constants.CONNECTION_TIMEOUT;
import static com.example.myfood.util.Constants.READ_TIMEOUT;
import static com.example.myfood.util.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {
    private static OkHttpClient client = new OkHttpClient().newBuilder()
            //establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            //time between each byte read from server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            //time between each byte write from server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = retrofitBuilder.build();
    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi() {
        return recipeApi;
    }
}
