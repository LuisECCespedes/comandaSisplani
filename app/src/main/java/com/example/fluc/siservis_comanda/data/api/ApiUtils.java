package com.example.fluc.siservis_comanda.data.api;

import android.content.Context;

import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

/**
 * Created by fluc on 21/11/2017.
 */

public class ApiUtils {
    //private static Context cont = null;
    //public static final String BASE_URL = SessionPreferences.get(cont).getUrlApiApp();
    //"http://192.168.0.2/sistema_pedidos/public/api/";
    private ApiUtils() {};

    public static ComandaApi getComandaApi(String _BASE_URL) {
        //cont = _c;
        //BASE_URL = SessionPreferences.get(cont).getUrlApiApp();
        return  RetrofitClient.getComanda(_BASE_URL).create(ComandaApi.class);
    }
}
