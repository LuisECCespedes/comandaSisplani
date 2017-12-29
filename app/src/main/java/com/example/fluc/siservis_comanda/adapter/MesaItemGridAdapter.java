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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fluc.siservis_comanda.R;
import com.example.fluc.siservis_comanda.data.modelo.Mesa;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

import java.util.List;

/**
 * Created by fluc on 23/11/2017.
 */

public class MesaItemGridAdapter extends ArrayAdapter<Mesa>{

    public MesaItemGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Mesa> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // objeto con el control
        try {
            Mesa objMesa = getItem(position);

            //comprobamos que el View este vacio
            if (convertView == null)
            {
                //tomaremos los controles de nuestro item y inflaremos al convertView
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mesas,parent,false);
            }
            //tomamos los controles y los asignamos a variables para poder alterar sus valores
            TextView tvUsuario =(TextView)convertView.findViewById(R.id.tvUsuario);
            TextView tvMesa = (TextView)convertView.findViewById(R.id.tvMesa);
            LinearLayout llMesaLista = (LinearLayout)convertView.findViewById(R.id.llMesaLista);

            //altermos los valores de las propiedades , tomando el objMesa
            tvUsuario.setText(objMesa.getUsuario());
            tvMesa.setText(objMesa.getMesa());
            Float nSize = SessionPreferences.get(parent.getContext()).getLetraSize();
            //tamaño de las letras
            //tvUsuario.setTextSize(nSize);
            tvMesa.setTextSize(nSize);

            // 	Estados de la mesa,según el proceso en que estén: DIS= disponible, OCU=ocupado, PRC=precuenta, DSH= deshabilitado, PAG=pagando
            llMesaLista .setBackground(ContextCompat.getDrawable(getContext(),objMesa.getEstado().equals("OCU") ? R.color.MesaOcupada:
                    objMesa.getEstado().equals("PRC") ? R.color.MesaPrecuenta :
                    objMesa.getEstado().equals("PAG") ? R.color.MesaPagando : R.color.MesaLibre));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
