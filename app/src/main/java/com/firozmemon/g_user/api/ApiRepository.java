package com.firozmemon.g_user.api;

import com.firozmemon.g_user.model.UserData;
import com.firozmemon.g_user.ui.PresenterRepository;

import io.reactivex.Single;

/**
 * Created by firoz on 5/8/17.
 */

public interface ApiRepository {

    Single<String> getSpecificUser(String username);

    void setPresenterRepository(PresenterRepository presenterRepository);
}
