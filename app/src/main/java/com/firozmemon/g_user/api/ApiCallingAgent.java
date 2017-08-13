package com.firozmemon.g_user.api;

import com.firozmemon.g_user.api.retrofit.ApiClient;
import com.firozmemon.g_user.api.retrofit.ApiInterface;
import com.firozmemon.g_user.model.UserData;
import com.firozmemon.g_user.ui.main.presenter.PresenterRepository;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by firoz on 5/8/17.
 */

/**
 * All Api calls will be made from this class
 */
public class ApiCallingAgent implements ApiRepository {

    private static ApiCallingAgent apiCallingAgent;
    private PresenterRepository presenterRepository;

    public static ApiCallingAgent getInstance() {
        if (apiCallingAgent == null)
            apiCallingAgent = new ApiCallingAgent();

        return apiCallingAgent;
    }

    @Override
    public void setPresenterRepository(PresenterRepository presenterRepository) {
        this.presenterRepository = presenterRepository;
    }

    /**
     * Calling Retrofit ApiInterface
     * @return {@link ApiInterface}
     */
    private ApiInterface getApiInterface() {
        return ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public Single<String> getSpecificUser(final String username) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Call<UserData> userDataCall = getApiInterface().getSpecificUserData(username);
                userDataCall.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        // Update Presenter
                        presenterRepository.executionSuccessful(response.body());
                        return;
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        // Update Presenter
                        presenterRepository.executionFailed("FAIL:" + t.getMessage());
                        return;
                    }
                });
                return "";
            }
        });
    }

}
