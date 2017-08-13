package com.firozmemon.g_user.ui;

/**
 * Created by firoz on 13/8/17.
 */


import com.firozmemon.g_user.model.UserData;

/**
 * This class helps in notifying presenter
 * about the call made via WebService
 */
public interface PresenterRepository {

    void executionSuccessful(UserData userData);

    void executionFailed(String message);
}
