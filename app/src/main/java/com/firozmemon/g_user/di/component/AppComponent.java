package com.firozmemon.g_user.di.component;

import com.firozmemon.g_user.App;
import com.firozmemon.g_user.di.AppModule;
import com.firozmemon.g_user.di.builder.BuilderModule;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, BuilderModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(App app);

        AppComponent build();
    }

    void inject(App app);
}
