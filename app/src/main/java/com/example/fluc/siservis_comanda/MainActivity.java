package com.example.fluc.siservis_comanda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fluc.siservis_comanda.data.modelo.User;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

public class MainActivity extends AppCompatActivity {
    private User us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //llamar a la actividad login
        // si no esta logueado dirigir
        us = SessionPreferences.get(getApplicationContext()).getUsuario();
        if (us.getUser_id()=="") {
            startActivity(new Intent(getApplicationContext(),
                    SessionPreferences.get(MainActivity.this).getMesa().equals("0") ? LoginActivity.class : ComandaActivity.class));
        }else{
            startActivity(new Intent(getApplicationContext(),
                    SessionPreferences.get(MainActivity.this).getMesa().equals("0") ? MesasActivity.class : ComandaActivity.class));
//                    MesasActivity.class));
        }
        finish();
    }
}
