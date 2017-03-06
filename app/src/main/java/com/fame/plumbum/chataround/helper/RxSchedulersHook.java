package com.fame.plumbum.chataround.helper;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This class contains useful methods that rerurns {@link Scheduler} instance.
 * Created by meghal on 7/7/16.
 */
public class RxSchedulersHook {

    /**
     * This method is used to get Main thread Scheduler
     *
     * @return
     */
    public Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * This method is used to get Io Thread Scheduler for Rx Observables
     *
     * @return
     */
    public Scheduler getIOScheduler() {
        return Schedulers.io();
    }

    /**
     * This method is used to get Computational Scheduler.
     * @return
     */
    public Scheduler getComputationScheduler() {
        return Schedulers.computation();
    }
}