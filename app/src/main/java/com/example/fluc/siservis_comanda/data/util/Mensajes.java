package com.example.fluc.siservis_comanda.data.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fluc.siservis_comanda.MesasActivity;
import com.example.fluc.siservis_comanda.R;

/**
 * Created by fluc on 25/11/2017.
 */

public class Mensajes {
    private Context context;

    public Mensajes(Context context) {
        this.context = context;
    }

    public  static  Dialog mensajeDialogo(Context cnt, LayoutInflater  inflater, String _line1, String _line2)
    {
        //LayoutInflater inflater = getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.progres_bar_login, null);

        TextView etAsunto = (TextView) dialoglayout.findViewById(R.id.etLinea1);
        TextView etMensaje = (TextView) dialoglayout.findViewById(R.id.etLinea2);

        etAsunto.setText(_line1);
        etMensaje.setText(_line2);

        AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
        builder.setView(dialoglayout);
        Dialog dialog= builder.create();
        return dialog;
    }

    public static void mensajeToas(Context cnt ,Object _mensaje)
    {
        Toast.makeText(cnt, _mensaje.toString(), Toast.LENGTH_LONG).show();
    }


}
