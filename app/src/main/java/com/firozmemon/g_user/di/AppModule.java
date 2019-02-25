package com.firozmemon.g_user.di;

import android.content.Context;

import com.firozmemon.g_user.App;

import dagger.Module;
import dagger.Provides;

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
public class AppModule {

    @Provides
    Context getApplicationContext(App app) {
        return app.getApplicationContext();
    }
}
