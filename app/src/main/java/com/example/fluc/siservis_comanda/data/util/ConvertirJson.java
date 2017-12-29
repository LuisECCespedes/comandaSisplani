package com.example.fluc.siservis_comanda.data.util;

import com.example.fluc.siservis_comanda.data.modelo.Especificaciones;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luis on 28/11/2017.
 */

public class ConvertirJson {

    public  static String convertirList(List<Especificaciones> _lista)
    {   //convertir un List a formato Json
        Gson gson = new Gson();
        return  gson.toJson(_lista);
    }

    public  static List<Especificaciones> convertirList(String _gson)
    {   //convertir un array en un List
        Gson gson = new Gson();
        Type lista = new TypeToken<List<Especificaciones>>(){}.getType();
        return  gson.fromJson(_gson,lista);
    }
}
