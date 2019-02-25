package com.firozmemon.g_user.di.builder;

import com.firozmemon.g_user.ui.main.MainActivity;
import com.firozmemon.g_user.ui.main.di.MainActivityModule;
import com.firozmemon.g_user.ui.main.di.MainActivityViewModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Binds all sub-components within the app.
 */
@Module
public abstract class BuilderModule {

    @ContributesAndroidInjector(modules = {MainActivityViewModule.class, MainActivityModule.class})
    abstract MainActivity mainActivity();
}
