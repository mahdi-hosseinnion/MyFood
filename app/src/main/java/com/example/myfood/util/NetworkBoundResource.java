package com.example.myfood.util;

import android.util.Log;

import com.example.myfood.AppExecutors;
import com.example.myfood.requests.responses.ApiResponse;

import java.util.Calendar;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import retrofit2.Response;


// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {
    private static final String TAG = "NetworkBoundResource";
    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<CacheObject>> mResults = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    private void init() {
        //update LiveData for loading status
        mResults.setValue((Resource<CacheObject>) Resource.loading(null));
        //observe LiveData source from local cache
        final LiveData<CacheObject> dbSource = loadFromDb();
        mResults.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                mResults.removeSource(dbSource);
                if (shouldFetch(cacheObject)) {
                    //get data from netWork
                    fetchFromNetwork(dbSource);
                } else {
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

    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {
        /**
         * 1)observe local cache
         * 2)if<condition/> query the Network
         * 3)stop observing the local db
         * 4)insert new data into local db
         * 5)begin observing local db again to see the refreshed data from network
         */
        Log.d(TAG, "fetchFromNetwork: stated");
        //update live data for loading status
        mResults.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                mResults.setValue(Resource.loading(cacheObject));
            }
        });
        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();
        mResults.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(final ApiResponse<RequestObject> requestObjectApiResponse) {
                mResults.removeSource(dbSource);
                mResults.removeSource(apiResponse);
                /*
                    3 cases:
                    1)ApiSuccessResponse
                    2)ApiErroresponse
                    3)ApiEmptyResponse
                 */
                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                    Log.d(TAG, "onChanged: ApiSuccessResponse ");
                    //save the response to the local db
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse));
                            appExecutors.mainThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mResults.addSource(loadFromDb(), new Observer<CacheObject>() {
                                        @Override
                                        public void onChanged(CacheObject cacheObject) {
                                            setValue(Resource.success(cacheObject));
                                        }
                                    });
                                }
                            });
                        }
                    });


                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                    Log.d(TAG, "onChanged: ApiEmptyResponse ");
                    appExecutors.mainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            mResults.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                    Log.d(TAG, "onChanged: ApiErrorResponse ");
                    appExecutors.mainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            mResults.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.error(
                                            ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                                            cacheObject
                                    ));
                                }
                            });
                        }
                    });
                }
            }
        });


    }

    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response) {
        return (CacheObject) response.getBody();
    }

    private void setValue(Resource<CacheObject> newValue) {
        if (mResults.getValue() != newValue) {
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