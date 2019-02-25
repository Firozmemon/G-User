package com.firozmemon.g_user.ui.main.di;

import com.firozmemon.g_user.ui.main.MainActivity;
import com.firozmemon.g_user.ui.main.MainActivityContract;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainActivityViewModule {

    @Binds
    abstract MainActivityContract.View getMainActivityView(MainActivity mainActivity);
}
