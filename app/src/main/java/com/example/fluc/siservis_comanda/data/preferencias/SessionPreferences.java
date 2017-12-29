package com.example.fluc.siservis_comanda.data.preferencias;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fluc.siservis_comanda.data.modelo.User;

/**
 * Created by fluc on 21/11/2017.
 */

public class SessionPreferences {

    //region constantes Preferencias
    public static final String PREFS_NAME       = "DATA_SESSION";
    public static final String PREF_USER_ID     = "PREF_USER_ID";
    public static final String PREF_USER_NAME   = "PREF_USER_NAME";
    public static final String PREF_USER_SESSION = "PREF_USER_SESSION";
    public static final String PREF_USER_LETRA_SIZE = "PREF_USER_LETRA_SIZE";

    public static final int PREF_VOLVER_LOGIN = 0;
    public static final int PREF_PIDE_MOTIVO = 0;
    public static final int PREF_CANCELAR = 0;
    public static final int PREF_REINTEGRO_STOCK = 0;



    public static final String PREF_ESQUEMA = "PREF_ESQUEMA";
    public static final String PREF_ACTUALIZAR = "PREF_ACTUALIZAR";
    public static final String PREF_URL_API = "PREF_URL_API";
    public static final String PREF_MESA_ACTUAL = "PREF_MESA_ACTUAL";

    //endregion

    //region Base Preferencia
    // variable y constantes necesarias para el manejo de preferencia
    private final SharedPreferences mPrefs;
    private static SessionPreferences INSTANCE;
    private boolean mIsLoggedIn = false;

    public static SessionPreferences get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPreferences(context);
        }
        return INSTANCE;
    }

    private SessionPreferences(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    //endregion

    //region metodos para el login
    // comprobar si la session se encutra activa
    public boolean isLoggedIn() {
        if (mPrefs.getBoolean(PREF_USER_SESSION, false)) {
            mIsLoggedIn = true;
        } else {
            mIsLoggedIn = false;
        }

        return mIsLoggedIn;
    }

    public void setLetraSize(Float _size) {
        // este metodo se encargara de guardar el esquema de trabajo
        SharedPreferences.Editor objEditor = mPrefs.edit();
        objEditor.putFloat(PREF_USER_LETRA_SIZE,_size);
        objEditor.apply();
    }

    public Float getLetraSize() {
        //este metodo retornara
        return mPrefs.getFloat(PREF_USER_LETRA_SIZE, Float.parseFloat("35"));
    }

    public void setRefrescar(boolean _refrescar) {
        // este metodo se encargara de guardar el esquema de trabajo
        SharedPreferences.Editor objEditor = mPrefs.edit();
        objEditor.putBoolean(PREF_ACTUALIZAR,_refrescar);
        objEditor.apply();
    }

    public boolean getRefrescar() {
        //este metodo retornara
        return mPrefs.getBoolean(PREF_ACTUALIZAR, false);

    }

    public void setMesa(String _mesa) {
        // este metodo se encargara de guardar el esquema de trabajo
        SharedPreferences.Editor objEditor = mPrefs.edit();
        objEditor.putString(PREF_MESA_ACTUAL,_mesa);
        objEditor.apply();
    }

    public String getMesa() {
        //este metodo retornara
        return mPrefs.getString(PREF_MESA_ACTUAL, "0");
    }

    //region Esquema , url del API y tipo de login
    public void setEsquemaApp(String _esquema) {
        // este metodo se encargara de guardar el esquema de trabajo
        SharedPreferences.Editor objEditor = mPrefs.edit();
        objEditor.putString(PREF_ESQUEMA,_esquema);
        objEditor.apply();
    }

    public String getEsquemaApp() {
        //este metodo retornara
        return mPrefs.getString(PREF_ESQUEMA, "modelo");
    }

    public void setUrlApiApp(String _UrlApi) {
        // este metodo se encargara de guardar el esquema de trabajo
        SharedPreferences.Editor objEditor = mPrefs.edit();
        objEditor.putString(PREF_URL_API,"http://" + _UrlApi + "/comanda/public/api/");
        objEditor.apply();

    }

    public String getUrlApiApp() {
        //este metodo retornara
        return mPrefs.getString(PREF_URL_API, "http://localhost/comanda/public/api/");
    }
    //endregion

    // retornar los datos en forma de objeto , con los datos del login
    public User getUsuario(){
        User _usuario = new User();
        _usuario.setUser_id(mPrefs.getString(PREF_USER_ID, ""));
        _usuario.setUser_nombre(mPrefs.getString(PREF_USER_NAME,""));

        _usuario.setVolver_login(mPrefs.getInt("PREF_VOLVER_LOGIN",0));
        _usuario.setPide_motivo(mPrefs.getInt("PREF_PIDE_MOTIVO",0));
        _usuario.setCancelar(mPrefs.getInt("PREF_CANCELAR",0));
        _usuario.setReintegro_stock(mPrefs.getInt("PREF_REINTEGRO_STOCK",0));

        return _usuario;
    }
    // guardar los datos del loggin
    public void loginSave(User _user) {
        if (_user != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_USER_ID, _user.getUser_id());
            editor.putString(PREF_USER_NAME, _user.getUser_nombre());
            editor.putBoolean(PREF_USER_SESSION, true);
            editor.putInt("PREF_VOLVER_LOGIN", _user.getVolver_login());
            editor.putInt("PREF_PIDE_MOTIVO", _user.getPide_motivo());
            editor.putInt("PREF_CANCELAR", _user.getCancelar());
            editor.putInt("PREF_REINTEGRO_STOCK", _user.getReintegro_stock());

            editor.apply();
        }

    }
    // cerrar sesion
    public void loginClose(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_USER_ID, null);
        editor.putString(PREF_USER_NAME, null);
        editor.putBoolean(PREF_USER_SESSION, false);
        editor.apply();
    }
    //endregion
}
