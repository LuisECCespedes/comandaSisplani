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
import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

import java.util.List;

/**
 * Created by fluc on 25/11/2017.
 */

public class ComandaProductosGridAdapter extends ArrayAdapter<ComandaProductos> {

    public ComandaProductosGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ComandaProductos> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            // objeto con el control
            ComandaProductos objMesa = getItem(position);

            //comprobamos que el View este vacio
            if (convertView == null)
            {
                //tomaremos los controles de nuestro item y inflaremos al convertView
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comanda_productos,parent,false);
            }
            //tomamos los controles y los asignamos a variables para poder alterar sus valores
            TextView tvDescri =convertView.findViewById(R.id.tvProductoComanda);
            TextView tvPrecio =convertView.findViewById(R.id.tvProductoPrecio);
            LinearLayout llDescri =convertView.findViewById(R.id.llDescri);
            LinearLayout llPrecio =convertView.findViewById(R.id.llPrecio);

            Float nSize = SessionPreferences.get(parent.getContext()).getLetraSize();

            //altermos los valores de las propiedades , tomando el objMesa
            tvDescri.setTextSize(nSize);
            tvPrecio.setTextSize(nSize);
            tvDescri.setText(objMesa.getProd_descri());
            tvPrecio.setText("S/." + objMesa.getProd_precio()+"");

            //darle color a LinearLayout
        /*el estado en 9 --> indica que los productos de la comanda seran reimpresos
        */
            llDescri.setBackground(ContextCompat.getDrawable(getContext(),
                    objMesa.getComa_cab_crea_est() == 9 ? R.color.ColorComandaReimprimir :
                    objMesa.getComa_cab_crea_est() == 7 ? R.color.ColorComandaCancelar:
                    objMesa.getComa_cab_crea_est() == 2 ? ((objMesa.getComa_cab_id()%2) == 0 ? R.color.ColorComanda1 : R.color.ColorComanda2 ):
                    R.color.white));

            llPrecio.setBackground(ContextCompat.getDrawable(getContext(),
                    objMesa.getComa_cab_crea_est() == 9 ? R.color.ColorComandaReimprimir :
                    objMesa.getComa_cab_crea_est() == 7 ? R.color.ColorComandaCancelar:
                    objMesa.getComa_cab_crea_est() == 2 ? ((objMesa.getComa_cab_id()%2) == 0 ? R.color.ColorComanda1 : R.color.ColorComanda2 ):
                    R.color.white));
        } catch (Exception e) {

        }

        return convertView;
    }
}
