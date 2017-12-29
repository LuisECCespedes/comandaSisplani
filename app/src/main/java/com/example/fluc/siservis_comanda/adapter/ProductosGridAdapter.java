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
import com.example.fluc.siservis_comanda.data.modelo.Productos;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;

import java.util.List;

/**
 * Created by fluc on 25/11/2017.
 */

public class ProductosGridAdapter extends ArrayAdapter<Productos>{
    public ProductosGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Productos> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            //creamos un objeto y tomamos lo cargamos con el objeto enviado por parametro

            Productos objProducto = getItem(position);

            if (convertView == null)
            {   // inflamos el layout, nos hubicamos en el contexto actual , cargamos el item
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_productos,parent,false);
            }
            // controles del layout
            TextView tvProducto  = convertView.findViewById(R.id.tvItemProdDescri);
            LinearLayout llLinea = convertView.findViewById(R.id.llItemProd);

            tvProducto.setText(objProducto.getTexto_lista());
            Float nSize = SessionPreferences.get(parent.getContext()).getLetraSize();
            //tamaño de las letras
            tvProducto.setTextSize(nSize);

            llLinea .setBackground(ContextCompat.getDrawable(
                    getContext(),objProducto.getProd_indicador() < 2 ? R.color.productoDis :
                            objProducto.getProd_indicador() == 2 ? R.color.productoStockCero :
                            objProducto.getProd_indicador() == 3 ? R.color.productoStockMenosMin :
                                    R.color.productoStockInsumo));
        } catch (Exception e) {

        }

        return convertView;
    }
}
