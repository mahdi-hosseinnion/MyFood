package com.example.myfood;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    private static AppExecutors instance;
    private final ScheduledExecutorService mNetWordIO = Executors.newScheduledThreadPool(3);


    public static AppExecutors getInstance() {
        if (instance == null)
            instance = new AppExecutors();
        return instance;
    }

    private AppExecutors() {
    }

    public ScheduledExecutorService getNetWordIO() {
        return mNetWordIO;
    }
}

