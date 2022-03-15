package com.zaimus.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.zaimus.api.IVkcApis.DOMAIN_VKC;

public class RetrofitAPI {

    private static Retrofit retrofit = null;
    public static ApiInterface getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(DOMAIN_VKC)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //Creating object for our interface
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api; // return the APIInterface object
    }
}
