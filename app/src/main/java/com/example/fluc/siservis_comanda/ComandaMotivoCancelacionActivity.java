package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.fluc.siservis_comanda.adapter.ComandaProductosGridAdapter;
import com.example.fluc.siservis_comanda.adapter.MotivosAnulacionGridAdapter;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.MotivosAnulacion;
import com.example.fluc.siservis_comanda.data.modelo.User;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComandaMotivoCancelacionActivity extends AppCompatActivity {

    private ComandaApi objComandaApi;
    private ComandaProductos objComan;
    private String esquema , codigoProducto="0";
    private Button btGuardarMotivo;
    private EditText eTNuevoMotivo;
    private User _userActual ;
    GridView gvMotivos;
    private boolean bClicLista =true;
    private MotivosAnulacion objMotivo;
    private List<MotivosAnulacion> listaObjMotivo = new ArrayList<>();;
    MotivosAnulacionGridAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda_motivo_cancelacion);

        //gridview
        gvMotivos = (GridView) findViewById(R.id.gvActivComMotCanLista);
        btGuardarMotivo = (Button) findViewById(R.id.btActivComMotCanGua);
        eTNuevoMotivo = (EditText) findViewById(R.id.etActivComMotCanNuevo);

        //api
        objComandaApi = ApiUtils.getComandaApi(SessionPreferences.get(ComandaMotivoCancelacionActivity.this).getUrlApiApp());
        esquema = SessionPreferences.get(ComandaMotivoCancelacionActivity.this).getEsquemaApp();
        _userActual =SessionPreferences.get(ComandaMotivoCancelacionActivity.this).getUsuario();

        if (getIntent().hasExtra("itemComanda"))
        {
            objComan = (ComandaProductos) getIntent().getSerializableExtra("itemComanda");
        }
        this.setTitle("Motivo de anulación");
        //click en la gria  setOnItemClickListener
        gvMotivos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!InternerConnection.isConnectedWifi(ComandaMotivoCancelacionActivity.this)) {
                    Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,"No esta conectado a un red Wifi");
                } else {
                    objMotivo=listaObjMotivo.get(i);
                    bClicLista = true;
                    if (objComan.getProd_st_control()==1) {
                        retornarStockProducto();
                    }else   {
                        cancelarProductoPedirMotivo();
                    }

                }
            }
        });

        //click en el boton de nuevo
        btGuardarMotivo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!InternerConnection.isConnectedWifi(ComandaMotivoCancelacionActivity.this)) {
                    Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,"No esta conectado a un red Wifi");
                } else {

                    if (eTNuevoMotivo.length()>0 ) {
                        bClicLista = false;
                        if (objComan.getProd_st_control()==1) {
                            retornarStockProducto();
                        }else   {
                            cancelarProductoPedirMotivo();
                        }
                    } else {
                        Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,"Ingrese un motivo");
                        eTNuevoMotivo.requestFocus();
                    }
                }
            }
        });
//        Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this, objComan.getComa_det_id()+"|"objComan.getProd_st_control() + "|" );
        // cargamo la lista con los motivos previamente registrados
        cargarMotivos();

    }

    private void cargarMotivos() {

        if (!InternerConnection.isConnectedWifi(ComandaMotivoCancelacionActivity.this)) {
            Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,"No esta conectado a un red Wifi");
        } else {

            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaMotivoCancelacionActivity.this,getLayoutInflater(),"Cargando Motivos","Espere");
            dialogActivity.show();

            try {
                final Call<List<MotivosAnulacion>> motivoLista = objComandaApi.listarMotivos(esquema);
                motivoLista.enqueue(new Callback<List<MotivosAnulacion>>() {
                    @Override
                    public void onResponse(Call<List<MotivosAnulacion>> call, Response<List<MotivosAnulacion>> response) {
                        listaObjMotivo = response.body();

                        adapter = new MotivosAnulacionGridAdapter(ComandaMotivoCancelacionActivity.this, 0, listaObjMotivo);

                        gvMotivos.setAdapter(adapter);

                        dialogActivity.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<MotivosAnulacion>> call, Throwable t) {
                        Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,t.getMessage());
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,e.getMessage());
                dialogActivity.cancel();
            }

        }
    }

    private void cancelarProductoPedirMotivo() {
        final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaMotivoCancelacionActivity.this,getLayoutInflater(),"Cancelando Producto","Espere");
        dialogActivity.show();

        try {
                //dialogCancelar = Mensajes.mensajeDialogo(ComandaMotivoCancelacionActivity.this);
                dialogActivity.show();
                //dependiendo de donde venga el dato se creara uno o otro objeto

                //Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,esquema + "|" + objComan.getComa_det_id() + "|" + _userActual.getUser_id() + "|" + objMotivo.getAn_comadet_mot_id() + "|" + codigoProducto);

                final Call<User> cancelarItemRetorno = bClicLista ?
                        objComandaApi.comandaCancelarItemMotivo(esquema, objComan.getComa_det_id(), _userActual.getUser_id(),objMotivo.getAn_comadet_mot_id(), codigoProducto):
                        objComandaApi.comandaCancelarItemMotivo(esquema, objComan.getComa_det_id(), _userActual.getUser_id(), eTNuevoMotivo.getText().toString(), codigoProducto);

                cancelarItemRetorno.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        cargarComanda();
                        dialogActivity.cancel();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this, t.getMessage());
                        dialogActivity.cancel();
                    }
                });

        } catch (Exception e) {
            Mensajes.mensajeToas(ComandaMotivoCancelacionActivity.this,e.getMessage());
            dialogActivity.cancel();
        }finally {

        }

    }

    private void retornarStockProducto() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ComandaMotivoCancelacionActivity.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Reintegrar el producto al kardex?");
        dialogo1.setCancelable(false);

        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //no devolvera el producto al almacen
                codigoProducto="0";
                cancelarProductoPedirMotivo();
            }
        });

        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //se realizara el agregado del producto , en el kardex
                codigoProducto=objComan.getProd_id();
                cancelarProductoPedirMotivo();

            }
        });
        dialogo1.show();
    }

    private void cargarComanda(){
        // volver a llamar a la misma actividad
        Intent intento = new Intent(ComandaMotivoCancelacionActivity.this, ComandaActivity.class);
        //llamamos a la actividad
        startActivity(intento);
        finish();
    }

}
