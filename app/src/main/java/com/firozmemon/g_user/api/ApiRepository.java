package com.firozmemon.g_user.api;

import com.firozmemon.g_user.model.UserData;

import io.reactivex.Single;

/**
 * Created by firoz on 5/8/17.
 */

public interface ApiRepository {

    Single<UserData> getDefaultUserData();

    Single<UserData> getSpecificUser(String username);
}
