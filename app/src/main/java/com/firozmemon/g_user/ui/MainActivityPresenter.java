package com.firozmemon.g_user.ui;

import com.firozmemon.g_user.api.ApiRepository;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by firoz on 5/8/17.
 */

public class MainActivityPresenter {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainActivityView view;
    private ApiRepository apiRepository;
    private Scheduler mainScheduler;

    public MainActivityPresenter(MainActivityView view, ApiRepository apiRepository, Scheduler mainScheduler) {
        this.view = view;
        this.apiRepository = apiRepository;
        this.mainScheduler = mainScheduler;
    }
}
