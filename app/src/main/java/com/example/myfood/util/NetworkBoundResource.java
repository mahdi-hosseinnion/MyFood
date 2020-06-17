package com.example.myfood.util;

import com.example.myfood.AppExecutors;
import com.example.myfood.requests.responses.ApiResponse;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import okhttp3.Cache;

// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {
    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<CacheObject>> mResults = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }
    private void init(){
        //update LiveData for loading status
        mResults.setValue((Resource<CacheObject>)Resource.loading(null));
        //observe LiveData source from local cache
        final LiveData<CacheObject> dbSource=loadFromDb();
        mResults.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                mResults.removeSource(dbSource);
                if (shouldFetch(cacheObject)){
                    //get data from netWork

                }else{
                    mResults.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }
    private void setValue(Resource<CacheObject>newValue){
        if (mResults.getValue()!=newValue){
            mResults.setValue(newValue);
        }
    }
    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData() {
        return mResults;
    }

    ;
}