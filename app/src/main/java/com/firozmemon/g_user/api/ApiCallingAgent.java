package com.firozmemon.g_user.api;

import com.firozmemon.g_user.api.retrofit.ApiClient;
import com.firozmemon.g_user.api.retrofit.ApiInterface;
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

    /**
     * Calling Retrofit ApiInterface
     *
     * @return {@link ApiInterface}
     */
    private ApiInterface getApiInterface() {
        return ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public Single<UserData> getSpecificUser(final String username) {
        return getApiInterface().getSpecificUserData(username);
    }

}
