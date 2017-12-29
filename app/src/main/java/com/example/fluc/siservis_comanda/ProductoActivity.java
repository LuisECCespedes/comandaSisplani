package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.Intent;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fluc.siservis_comanda.adapter.ProductosGridAdapter;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.Productos;
import com.example.fluc.siservis_comanda.data.modelo.TipoProducto;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoActivity extends AppCompatActivity {

    private GridView gvProductos;
    private ProductosGridAdapter adaptador;
    private List<Productos> listaProductos;
    private ComandaApi utilApp;
    private String esquema;
    private ComandaProductos objComanda;
    private TipoProducto objTipoProducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        esquema = SessionPreferences.get(getApplicationContext()).getEsquemaApp();
        utilApp = ApiUtils.getComandaApi(esquema);

        gvProductos= (GridView)findViewById(R.id.gvActivProducto);

        if (getIntent().hasExtra("itemComanda"))
        {
            objComanda = (ComandaProductos) getIntent().getSerializableExtra("itemComanda");
        }

        if (getIntent().hasExtra("itemTipoProducto"))
        {
            objTipoProducto = (TipoProducto) getIntent().getSerializableExtra("itemTipoProducto");
        }

        this.setTitle(objTipoProducto.getProd_tipo_descri());

        cargarListaProductos();

        gvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!InternerConnection.isConnectedWifi(getApplicationContext()))
                {
                    Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
                }else {
                    if (listaProductos.get(i).getProd_indicador() != 2) {
                        Intent intProductos = new Intent(getApplicationContext(),
                                listaProductos.get(i).getProd_especif_ind() == 0 ? AgregarCancelarActivity.class :
                                        EspecificacionesActivity.class);

                        intProductos.putExtra("itemProducto", listaProductos.get(i));
                        intProductos.putExtra("itemComanda", objComanda);
                        startActivity(intProductos);
                    }
                }
            }
        });
    }
    private void cargarListaProductos() {
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
        }else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ProductoActivity.this,getLayoutInflater(),"Cargando Productos","Espere");
            dialogActivity.show();
            try {
                final Call<List<Productos>> listaProd = utilApp.listarProductos(esquema, objTipoProducto.getProd_tipo_id(), "*");

                listaProd.enqueue(new Callback<List<Productos>>() {
                    @Override
                    public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                        listaProductos = response.body();
                        buscarEnLista("");
                        dialogActivity.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<Productos>> call, Throwable t) {
                        Mensajes.mensajeToas(ProductoActivity.this, "Error" + t.getMessage());
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(ProductoActivity.this,e.getMessage());
                dialogActivity.cancel();
            }
        }
    }

    private void buscarEnLista(String _busqueda) {
        // cargamos nuestro array
        List<Productos> listaTipoBuscador = new ArrayList<>();;
        if (_busqueda.length() > 0 )
        {
            for (Productos item : listaProductos)
            {
                String cadena = item.getProd_descri().substring(0,_busqueda.length()).toLowerCase();

                if (cadena.equals(_busqueda.toLowerCase()))
                {
                    listaTipoBuscador.add(item);
                }
            }
        }else
        {
            listaTipoBuscador = listaProductos;
        }

        // cargamos al adaptador con la estructura
        adaptador = new ProductosGridAdapter(ProductoActivity.this,0,listaTipoBuscador);

        // cargamos la gria , con el adaptador
        gvProductos.setAdapter(adaptador);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflamos el menu , para que se posicione en la pantalla
        getMenuInflater().inflate(R.menu.menu_busqueda,menu);

        // creamos las variables que se encargaran del texto de busqueda
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =(SearchView) searchItem.getActionView();

        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getText(R.string.hintProducto));

        // los eventos : submit --> al darle buscar || change --> cada tecla
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(ProductoActivity.this, R.string.cerrarSession, Toast.LENGTH_SHORT).show();
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

}
