package com.example.fluc.siservis_comanda.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fluc.siservis_comanda.R;
import com.example.fluc.siservis_comanda.data.modelo.Especificaciones;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

import java.util.List;

/**
 * Created by fluc on 25/11/2017.
 */

public class EspecificacionesGridAdapter extends ArrayAdapter<Especificaciones>{
    public EspecificacionesGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Especificaciones> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            //objeto enviado por parametro
            Especificaciones objEspecificaciones = getItem(position);

            if (convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_especificaciones,parent,false);
            }
            Float nSize = SessionPreferences.get(parent.getContext()).getLetraSize();
            //altermos los valores de las propiedades , tomando el objMesa

            TextView tvEspecificacion = convertView.findViewById(R.id.tvItemEspDescri);
            LinearLayout llItemEsp = convertView.findViewById(R.id.llItemEsp);

            tvEspecificacion.setTextSize(nSize);
            tvEspecificacion.setText(objEspecificaciones.getEspec_descri() + " " + objEspecificaciones.getEspec_precio());
            llItemEsp.setBackground(ContextCompat.getDrawable(getContext(),objEspecificaciones.getEspec_ind() == 1 ? R.color.productoEnviado : R.color.white));
        } catch (Exception e) {

        }

        return convertView;
    }
}
