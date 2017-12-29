package com.example.fluc.siservis_comanda.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fluc on 21/11/2017.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getComanda(String base_url) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
