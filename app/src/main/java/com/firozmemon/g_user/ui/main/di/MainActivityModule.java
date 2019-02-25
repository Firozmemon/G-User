package com.firozmemon.g_user.ui.main.di;

import com.firozmemon.g_user.api.ApiCallingAgent;
import com.firozmemon.g_user.api.ApiRepository;
import com.firozmemon.g_user.ui.main.MainActivityContract;
import com.firozmemon.g_user.ui.main.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Define MainActivity-specific dependencies here.
 */
@Module
public class MainActivityModule {

    @Provides
    MainActivityContract.Presenter getMainActivityPresenter(MainActivityContract.View view,
                                                            ApiRepository apiRepository,
                                                            Scheduler mainScheduler) {
        return new MainActivityPresenter(view, apiRepository, mainScheduler);
    }

    @Provides
    ApiRepository getApiRepository() {
        return ApiCallingAgent.getInstance();
    }

    @Provides
    Scheduler getMainScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
