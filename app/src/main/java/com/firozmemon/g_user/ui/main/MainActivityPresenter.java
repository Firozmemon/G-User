package com.firozmemon.g_user.ui.main;

import com.firozmemon.g_user.api.ApiRepository;
import com.firozmemon.g_user.model.UserData;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firoz on 5/8/17.
 */
public class MainActivityPresenter implements MainActivityContract.Presenter {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainActivityContract.View view;
    private ApiRepository apiRepository;
    private Scheduler mainScheduler;

    public MainActivityPresenter(MainActivityContract.View view, ApiRepository apiRepository, Scheduler mainScheduler) {
        this.view = view;
        this.apiRepository = apiRepository;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void getUserData(String userName) {
        compositeDisposable.add(apiRepository.getSpecificUser(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<UserData>() {
                    @Override
                    public void onSuccess(@NonNull UserData userData) {
                        if (userData.getItems().isEmpty() || userData.getItems().size() < 1)
                            view.displayError("No Data Found");
                        else
                            view.displaySuccess(userData);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.displayError(e.getMessage());
                    }
                }));
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }
}
