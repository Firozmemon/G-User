package com.firozmemon.g_user.ui.main;

/**
 * Created by firoz on 5/8/17.
 */
public interface MainActivityContract {

    interface View {
        void displaySuccess(Object obj);

        void displayError(String message);
    }

    interface Presenter {
        void getUserData(String userName);

        void unsubscribe();
    }
}
