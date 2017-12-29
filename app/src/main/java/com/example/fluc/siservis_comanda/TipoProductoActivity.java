package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.fluc.siservis_comanda.adapter.TipoProductosGridAdapter;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.TipoProducto;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipoProductoActivity extends AppCompatActivity {

    GridView gvTipoProducto;
    List<TipoProducto> listaTipoProductos = new ArrayList<>();
    TipoProductosGridAdapter adaptador;
    private ComandaApi objComandaApi;
    ComandaProductos objComanda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_producto);

        this.setTitle(R.string.nameBarTipoProducto );

        //gridview
        gvTipoProducto = (GridView)findViewById(R.id.gvActTipoProducto);

        //api
        objComandaApi = ApiUtils.getComandaApi(SessionPreferences.get(getApplicationContext()).getUrlApiApp());

        if (getIntent().hasExtra("itemComanda"))
        {
            objComanda = (ComandaProductos) getIntent().getSerializableExtra("itemComanda");
        }

        // cargamos la lista
        cargarTipoProductos("");

        gvTipoProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (!InternerConnection.isConnectedWifi(getApplicationContext()))
            {
                Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            }else {
                Intent intProductos = new Intent(TipoProductoActivity.this, ProductoActivity.class);
                intProductos.putExtra("itemTipoProducto", listaTipoProductos.get(i));
                intProductos.putExtra("itemComanda", objComanda);
                startActivity(intProductos);
            }
            }
        });
    }

    private void cargarTipoProductos(String _busqueda) {
        //comprobar esta en una red wifi
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
        }else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(TipoProductoActivity.this,getLayoutInflater(),"Cargando Tipo Producto","Espere");
            dialogActivity.show();

            try {
                final Call<List<TipoProducto>> listaTipos = objComandaApi.listarTipoproductos(SessionPreferences.get(getApplicationContext()).getEsquemaApp(), "*" + _busqueda);
                listaTipos.enqueue(new Callback<List<TipoProducto>>() {
                    @Override
                    public void onResponse(Call<List<TipoProducto>> call, Response<List<TipoProducto>> response) {
                        // cargamos nuestro array
                        listaTipoProductos = response.body();

                        //cargamos la gria
                        buscarEnLista("");

                        // cerramos el dialogo
                        dialogActivity.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<TipoProducto>> call, Throwable t) {
                        //toast , realizara la muestra del mensaje
                        Mensajes.mensajeToas(TipoProductoActivity.this, "Error" + t.getMessage());
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(TipoProductoActivity.this, "Error" + e.getMessage());
                dialogActivity.cancel();
            } finally {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflamos el menu , para que se posicione en la pantalla
        getMenuInflater().inflate(R.menu.menu_busqueda,menu);

        // creamos las variables que se encargaran del texto de busqueda
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =(SearchView) searchItem.getActionView();

        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getText(R.string.hintBuscarTipoProducto));

        // los eventos : submit --> al darle buscar || change --> cada tecla
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(TipoProductoActivity.this, R.string.cerrarSession, Toast.LENGTH_SHORT).show();
                //buscarEnLista(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                buscarEnLista(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void buscarEnLista(String _busqueda) {
        // cargamos nuestro array
        List<TipoProducto> listaTipoBuscador = new ArrayList<>();;
        if (_busqueda.length() > 0 )
        {
            for (TipoProducto item : listaTipoProductos)
            {
                String cadena = item.getProd_tipo_descri().substring(0,_busqueda.length()).toLowerCase();

                if (cadena.equals(_busqueda.toLowerCase()))
                {
                    listaTipoBuscador.add(item);
                }
            }
        }else
        {
            listaTipoBuscador = listaTipoProductos;
        }

        // cargamos al adaptador con la estructura
        adaptador = new TipoProductosGridAdapter(TipoProductoActivity.this,0,listaTipoBuscador);

        // cargamos la gria , con el adaptador
        gvTipoProducto.setAdapter(adaptador);
    }

}
