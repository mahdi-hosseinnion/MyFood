package com.example.myfood;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.annotations.Expose;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    private static AppExecutors instance;
    private final Executor mDiskIO = Executors.newSingleThreadExecutor();
    private final Executor mMainThreadExecutor = new MainThreadExecutor();


    public static AppExecutors getInstance() {
        if (instance == null)
            instance = new AppExecutors();
        return instance;
    }

    private AppExecutors() {
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThreadExecutor() {
        return mMainThreadExecutor;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}

