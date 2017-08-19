package com.firozmemon.g_user.api.retrofit;

import com.firozmemon.g_user.model.UserData;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by firoz on 13/8/17.
 */

public interface ApiInterface {

    @GET("search/users")
    Single<UserData> getSpecificUserData(@Query("q") String userName);


    // For Pagination
//    Call<UserData> getSpecificUserData(@Query("q") String userName, @Query("per_page") int per_page, @Query("page") int page);
}
