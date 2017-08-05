package com.firozmemon.g_user.api;

import com.firozmemon.g_user.model.UserData;

import io.reactivex.Single;

/**
 * Created by firoz on 5/8/17.
 */

/**
 * All Api calls will be made from this class
 */
public class ApiCallingAgent implements ApiRepository {

    private static ApiCallingAgent apiCallingAgent;

    public static ApiCallingAgent getInstance() {
        if (apiCallingAgent == null)
            apiCallingAgent = new ApiCallingAgent();

        return apiCallingAgent;
    }

    @Override
    public Single<UserData> getDefaultUserData() {
        return null;
    }

    @Override
    public Single<UserData> getSpecificUser(String username) {
        return null;
    }
}
