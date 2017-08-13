package com.firozmemon.g_user.ui;

import com.firozmemon.g_user.api.ApiRepository;
import com.firozmemon.g_user.model.UserData;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 5/8/17.
 */

public class MainActivityPresenter implements PresenterRepository {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainActivityView view;
    private ApiRepository apiRepository;
    private Scheduler mainScheduler;

    public MainActivityPresenter(MainActivityView view, ApiRepository apiRepository, Scheduler mainScheduler) {
        this.view = view;
        this.apiRepository = apiRepository;
        this.mainScheduler = mainScheduler;

        this.apiRepository.setPresenterRepository(this);
    }

    public void getUserData(String userName) {
        compositeDisposable.add(apiRepository.getSpecificUser(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribe());
    }

    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void executionSuccessful(UserData userData) {
        view.displaySuccess(userData.toString());
    }

    @Override
    public void executionFailed(String message) {
        view.displayError(message);
    }
}
