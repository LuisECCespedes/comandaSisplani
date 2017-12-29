package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.fluc.siservis_comanda.adapter.MesaItemGridAdapter;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.Mesa;
import com.example.fluc.siservis_comanda.data.modelo.User;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesasActivity extends AppCompatActivity {
    GridView gvMesas;
    List<Mesa> listaMesas = new ArrayList<>();
    MesaItemGridAdapter adaptador;
    private ComandaApi objComandaApi;

    // para el refresco de las mesas
    private static final long DURATION = 30000;
    private Timer timer = null;
    private boolean bTimerActivo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        this.setTitle(R.string.nameBarMesas );

        User _us = SessionPreferences.get(MesasActivity.this).getUsuario();

        //gridview
        gvMesas = (GridView)findViewById(R.id.gvLista);

        //api
        objComandaApi = ApiUtils.getComandaApi(SessionPreferences.get(getApplicationContext()).getUrlApiApp());

        // cargar las mesas
        cargarMesas();

        if (SessionPreferences.get(MesasActivity.this).getRefrescar()) {
            startRefresh();
            this.setTitle("Mesas - Actualizado cada 30s");
        }

        gvMesas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                try {
                    if (!InternerConnection.isConnectedWifi(getApplicationContext()))
                    {
                        Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
                        cerrarSession();
                    } else {
                        //dialog.show();
                        User prefUser = SessionPreferences.get(getApplicationContext()).getUsuario();

                        //if (prefUser.getUser_nombre().isEmpty() || prefUser.getUser_nombre().equals(listaMesas.get(i).getUsuario()))
                        //{
                        // creamos el intento para llamar a la actividad y enviar los datos a la activida
                        Intent intento = new Intent(MesasActivity.this, ComandaActivity.class);

                        // cargamos el intento con el valore encontrado en el objeto
                        SessionPreferences.get(MesasActivity.this).setMesa(listaMesas.get(i).getCodigo());

                        if (bTimerActivo) {timer.cancel();}

                        //llamamos a la actividad
                        startActivity(intento);
                        finish();

                    }
                    //}
                    //dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startRefresh() {
        bTimerActivo = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                runOnUiThread(new Runnable() {
                    public void run() {
                        cargarMesas();
                    }
                });
            }

        }, 0, DURATION);
    }

    //region Manejo de menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cerrar_session,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cerrarSession:
                cerrarSession();
                return true;

            case R.id.recargar:
                cargarMesas();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    private void cerrarSession() {
        SessionPreferences.get(getApplicationContext()).loginClose();
        Intent intento = new Intent(MesasActivity.this, LoginActivity.class );
        //limpiar pila de actividades
        intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        SessionPreferences.get(MesasActivity.this).setMesa("0");
        if (bTimerActivo) {
            timer.cancel();
        }
        startActivity(intento);
        finish();
    }

    private void cargarMesas()
    {
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            listaMesas.clear();
            cerrarSession();

        }else {
            final Dialog dialogMesas = Mensajes.mensajeDialogo(MesasActivity.this,getLayoutInflater(),"Cargando Mesas","Espera");
            dialogMesas.show();
            try {

                //consultar a la base de datos
                final Call<List<Mesa>> llamarMesas = objComandaApi.listarMesas(SessionPreferences.get(getApplicationContext()).getEsquemaApp());

                //metodo enqueque
                llamarMesas.enqueue(new Callback<List<Mesa>>() {
                    @Override
                    public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                        listaMesas = response.body();
                        cargarGria();
                        dialogMesas.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<Mesa>> call, Throwable t) {
                        //toast , realizara la muestra del mensaje
                        //Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                        Mensajes.mensajeToas(MesasActivity.this,t.getMessage());
                        cerrarSession();
                        dialogMesas.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(MesasActivity.this,e.getMessage());
                cerrarSession();
                dialogMesas.cancel();
            } finally {
            }
        }
    }
    private void cargarGria()
    {
        try {
            if (listaMesas.size() >= 0 )
            {
                adaptador = new MesaItemGridAdapter(MesasActivity.this, 0, listaMesas);

                gvMesas.setAdapter(adaptador);

            }else{
                Mensajes.mensajeToas(MesasActivity.this,"Error en Desconocido. Vuelva a loguearse");
                cerrarSession();
            }
        } catch (Exception e) {
            //Log.e("Error al desconocido", "Refresque manualmente o cierre y vuelva a abrirla", e);
            Mensajes.mensajeToas(MesasActivity.this,e.getMessage());
            cerrarSession();
        }
    }

}
