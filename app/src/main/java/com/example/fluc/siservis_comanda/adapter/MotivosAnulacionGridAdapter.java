package com.example.fluc.siservis_comanda.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fluc.siservis_comanda.R;
import com.example.fluc.siservis_comanda.data.modelo.MotivosAnulacion;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by luis on 02/12/2017.
 */
public class MotivosAnulacionGridAdapter extends ArrayAdapter<MotivosAnulacion>{
    public MotivosAnulacionGridAdapter(@NonNull Context context, int resource, @NonNull List<MotivosAnulacion> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            //objeto enviado por parametro
            MotivosAnulacion objMotivosAnulacion = getItem(position);

            if (convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_motivos_anulacion,parent,false);
            }
            TextView tvMotivo = convertView.findViewById(R.id.tvItemMovAnuDescri);
            Float nSize = SessionPreferences.get(parent.getContext()).getLetraSize();
            //tamaño de las letras
            tvMotivo.setTextSize(nSize);
            tvMotivo.setText(objMotivosAnulacion.getAn_comadet_mot_descri());

        } catch (Exception e) {
        }
        return convertView;
    }
}
