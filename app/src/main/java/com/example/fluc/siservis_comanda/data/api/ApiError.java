package com.example.fluc.siservis_comanda.data.api;

import java.io.IOException;
import com.google.gson.Gson;
import okhttp3.ResponseBody;

/**
 * Created by fluc on 21/11/2017.
 */

public class ApiError {
    private int codigo;
    private String mensaje;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ApiError() {
    }

    public ApiError(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public static ApiError fromResponseBody(ResponseBody responseBody) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(responseBody.string(), ApiError.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
