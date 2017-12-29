package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.fluc.siservis_comanda.adapter.EspecificacionesGridAdapter;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.Especificaciones;
import com.example.fluc.siservis_comanda.data.modelo.Productos;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.ConvertirJson;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EspecificacionesActivity extends AppCompatActivity {

    private List<Especificaciones> listaEspecificaciones= new ArrayList<>();;
    ComandaApi apiEspe;
    String esquema ;

    EspecificacionesGridAdapter adaptador;
    GridView gvListaEso;
    EditText especExtra;
    Productos objProducto;
    ComandaProductos objcomanda;
    private boolean bconsulta = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especificaciones);

        especExtra = (EditText) findViewById(R.id.etActivEspIng);

        esquema = SessionPreferences.get(EspecificacionesActivity.this).getEsquemaApp();
        apiEspe = ApiUtils.getComandaApi(esquema);

        gvListaEso = (GridView) findViewById(R.id.gvActivEsp);

        if (getIntent().hasExtra("itemComanda"))
        {
            objcomanda = (ComandaProductos) getIntent().getSerializableExtra("itemComanda");
        }

        if (getIntent().hasExtra("itemProducto"))
        {
            objProducto = (Productos) getIntent().getSerializableExtra("itemProducto");
        }
        this.setTitle(objProducto.getProd_descri());

        //endregion
        if (bconsulta) {
            cargarEspecificaciones();
        } else {
            cargarDataGridView(listaEspecificaciones);
        }

        gvListaEso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (!InternerConnection.isConnectedWifi(getApplicationContext()))
            {
                Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            }else {
                //al darle click , modificar el valor del indicador para que cambie de color
                listaEspecificaciones.get(i).setEspec_ind(listaEspecificaciones.get(i).getEspec_ind() == 0 ? 1 : 0);
                cargarDataGridView(listaEspecificaciones);
            }
            }
        });
    }

    private void cargarEspecificaciones()
    {
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
        }else {
            final Dialog dialogActivity = Mensajes.mensajeDialogo(EspecificacionesActivity.this,getLayoutInflater(),"Cargando Especificaciones","Espere");
            dialogActivity.show();

            try {
                Call<List<Especificaciones>> listarEsp = apiEspe.listarEspecificaciones(esquema, objProducto.getProd_id());
                listarEsp.enqueue(new Callback<List<Especificaciones>>() {
                    @Override
                    public void onResponse(Call<List<Especificaciones>> call, Response<List<Especificaciones>> response) {
                        listaEspecificaciones = response.body();

                        cargarDataGridView(listaEspecificaciones);

                        dialogActivity.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<Especificaciones>> call, Throwable t) {
                        Mensajes.mensajeToas(EspecificacionesActivity.this, "Error " + t.getMessage());
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(EspecificacionesActivity.this, "Error " + e.getMessage());
                dialogActivity.cancel();
            } finally {
            }
        }
    }

    private void cargarDataGridView(List<Especificaciones> _lista) {
        // cargamos al adaptador con la estructura
        adaptador = new EspecificacionesGridAdapter(EspecificacionesActivity.this,0,listaEspecificaciones);

        // cargamos la gria , con el adaptador
        gvListaEso.setAdapter(adaptador);
    }


    //region Manejo de menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_aceptar,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuAceptar:
                aceptar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void aceptar() {
        Intent intProductos = new Intent(getApplicationContext(),AgregarCancelarActivity.class);
        intProductos.putExtra("itemProducto",objProducto);
        intProductos.putExtra("itemComanda",objcomanda);

        List<Especificaciones> objEspeci = new ArrayList<Especificaciones>();

        for(Especificaciones item : listaEspecificaciones)
        {
            item.setEspec_text(especExtra.getText().toString());
            if (item.getEspec_ind()==1) {
                objEspeci.add(item);
            }
        }
        intProductos.putExtra("itemsEspecificaciones", ConvertirJson.convertirList(objEspeci));

        startActivity(intProductos);
    }
}
