package com.example.fluc.siservis_comanda.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fluc.siservis_comanda.R;
import com.example.fluc.siservis_comanda.data.modelo.TipoProducto;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

import java.util.List;

/**
 * Created by fluc on 25/11/2017.
 */

public class TipoProductosGridAdapter extends ArrayAdapter<TipoProducto> {

    public TipoProductosGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TipoProducto> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
        /*
        Para la carga primero creamos un objeto con el objeto pasado por parametro
        Cargamos el view inflandolo con el layout personalizado
        Personzalizamos los controles
        */
            TipoProducto objTipProd = getItem(position);
            if (convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tipo_productos,parent,false);
            }
            TextView tvTipProd = convertView.findViewById(R.id.tvItemTipProdDescri);
            Float nSize = SessionPreferences.get(parent.getContext()).getLetraSize();
            //tamaño de las letras
            tvTipProd.setTextSize(nSize);

            tvTipProd.setText(objTipProd.getProd_tipo_descri());
        } catch (Exception e) {

        }

        return convertView;
    }
}
