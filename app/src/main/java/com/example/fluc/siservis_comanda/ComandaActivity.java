package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.fluc.siservis_comanda.adapter.ComandaProductosGridAdapter;
import com.example.fluc.siservis_comanda.adapter.MesaItemGridAdapter;
import com.example.fluc.siservis_comanda.data.api.ApiError;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.Mesa;
import com.example.fluc.siservis_comanda.data.modelo.Productos;
import com.example.fluc.siservis_comanda.data.modelo.User;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.ConvertirJson;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.PreferenceChangeEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComandaActivity extends AppCompatActivity {

    FloatingActionMenu actionMenu;
    String mesaSel,mensajeOpt,esquema,codigoProducto = "0";
    Button totalPagar;
    GridView gvComanda;
    List<ComandaProductos> listaProductos = new ArrayList<>();
    ComandaProductosGridAdapter adaptador;
    ComandaProductos objComan,objComanReimprimir;
    int estadoCancelado = 0 , codigoCab = 0;
    private ComandaApi objComandaApi;
    private boolean breimprimir = false;
    private User _userActual;
    private User _usParam;

    // mostrar mensaje
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda);

        _usParam = SessionPreferences.get(ComandaActivity.this).getUsuario();

        // cargamos los menus -- botones flotantes
        actionMenu = (FloatingActionMenu) findViewById(R.id.menuButton);
        actionMenu.setClosedOnTouchOutside(true);

        //gridview
        gvComanda = (GridView) findViewById(R.id.gvListaComanda);
        totalPagar = (Button) findViewById(R.id.btACtComTotalComanda);

        //api
        objComandaApi = ApiUtils.getComandaApi(SessionPreferences.get(ComandaActivity.this).getUrlApiApp());
        esquema = SessionPreferences.get(ComandaActivity.this).getEsquemaApp();
        mesaSel = SessionPreferences.get(ComandaActivity.this).getMesa();
        _userActual =SessionPreferences.get(ComandaActivity.this).getUsuario();
        totalPagar.setTextSize(SessionPreferences.get(getApplicationContext()).getLetraSize());
        this.setTitle("Mesa " + mesaSel);

        cargarProductos();

        gvComanda.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (!InternerConnection.isConnectedWifi(getApplicationContext()))
            {
                Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
                salirComanda();
            }else {

                if (breimprimir || _usParam.getCancelar() == 1) {
                    // al darle click a un elemento de la lista.    Esta se adaptara para cancelar.
                    objComanReimprimir = listaProductos.get(i);
                    estadoCancelado = objComanReimprimir.getComa_cab_crea_est();
                    if (!breimprimir && (_usParam.getCancelar()==1) ) {
                        for (ComandaProductos item : listaProductos) {
                            if (item.getComa_det_id() == objComanReimprimir.getComa_det_id()) {
                                item.setComa_cab_crea_est(7);
                            }
                        }
                        mensajeOpt = "¿ Deseas cancelar este producto ?";
                        mostrarMensaje();
                    } else {
                        if (objComanReimprimir.getComa_cab_crea_est() == 2) {
                            for (ComandaProductos item : listaProductos) {
                                if (item.getComa_cab_id() == objComanReimprimir.getComa_cab_id()) {
                                    item.setComa_cab_crea_est(9);
                                }
                            }
                            mensajeOpt = "¿ Desea reimprimir los productos marcados ?";
                            mostrarMensaje();
                        }
                    }

                    adaptador = new ComandaProductosGridAdapter(ComandaActivity.this, 0, listaProductos);
                    gvComanda.setAdapter(adaptador);
                }

            }
            }
        });
    }

    //region Menu bar Click , mostrar mensaje
    private void mostrarMensaje()   {   // Mensaje de confirmacion , este se adapta para cancelar un producto o para reiimprimir una lista de productos
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ComandaActivity.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage(mensajeOpt);
        dialogo1.setCancelable(false);

        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // si se cancela y reimprime , restaurar los valores
                for(ComandaProductos item : listaProductos)
                {
                    if (breimprimir) {
                        if (item.getComa_cab_crea_est() == 9|| item.getComa_cab_crea_est() == 7)
                        {
                            item.setComa_cab_crea_est(2);
                        }
                    } else {

                        if (item.getComa_det_id() == objComanReimprimir.getComa_det_id())
                        {
                            item.setComa_cab_crea_est(estadoCancelado);
                        }

                    }
                }

                adaptador=new ComandaProductosGridAdapter(ComandaActivity.this,0,listaProductos);
                gvComanda.setAdapter(adaptador);
            }
        });

        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //se realizara la carga de datos a la bd. para el envio de la reimpresion
                if (breimprimir) {
                    ReImprimir();

                } else {
                    codigoProducto = "0";
                    // consultamos el esta que tuvo antes de ser señalado
                    if (estadoCancelado == 2) {
                        // si no se pide un motivo

                        if (_usParam.getPide_motivo() == 1) {
                            // desmarcar como anulado
                            for(ComandaProductos item : listaProductos)
                            {
                                if (item.getComa_det_id() == objComanReimprimir.getComa_det_id())
                                {
                                    item.setComa_cab_crea_est(estadoCancelado);
                                }
                            }
                                // si se pide un motivo de cancelación de producto // se envia un objeto con los datos del producto
                                Intent intenMotivo = new Intent(ComandaActivity.this, ComandaMotivoCancelacionActivity.class);
                                intenMotivo.putExtra("itemComanda", objComanReimprimir);
                                startActivity(intenMotivo);
                            } else {

                            // se consulta si se descontara en el stock
                                if (objComanReimprimir.getProd_st_control() == 1 && _usParam.getReintegro_stock()==1 ) {
                                    retornarStockProducto();
                                }else{
                                    cancelarProductoSinMotivo();
                                }
                            }

                    }else   {
                        cancelarProductoReintegrarStock();
                    }

                }
            }
        });
        dialogo1.show();
    }

    public void clickMenuImprimir(View view)    {
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            //funcional
            codigoCab = 0;
            comprobarStock();
        }
    }

    public void clickMenuReImprimir(View view)  {
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            // reimprimir - mostrara un mensaje y modificara la vista para la seleccion de los productos a reimprimirse
            this.setTitle("Mesa " + mesaSel + " Seleccione Comanda");
            breimprimir = true;
        }
    }

    public void clickProducto(View view)    {
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            // Producto Nuevo
            // creamos el intento para llamar a la actividad y enviar los datos a la activida
            Intent intento = new Intent(ComandaActivity.this, TipoProductoActivity.class);
            objComan = listaProductos.get(0);
            //objEnvio.putSerializable("comandaProducto",item);
            intento.putExtra("itemComanda", objComan);
            //llamamos a la actividad
            startActivity(intento);
        }
    }

    public void clickPreCuenta(View view)    {
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            boolean bimprimir = true;
            for (ComandaProductos item : listaProductos)
            {
                //si hay productos sin enviar
                if(item.getComa_cab_crea_est()==1) {
                    bimprimir = false;
                    break;
                }
            }
            if (bimprimir) {
                // Pre Cuenta
                preCuentaImprimir();
            }else {
                Mensajes.mensajeToas(ComandaActivity.this, "Hay productos sin enviar");
            }
        }
    }

    //endregion

    @Override
    public void onBackPressed() {
        Intent intentoNew = new Intent(ComandaActivity.this, MesasActivity.class );
        intentoNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //falta acondicionar si se enviara al login
        SessionPreferences.get(ComandaActivity.this).setMesa("0");
        startActivity(intentoNew);
        finish();
    }

    public void salirComanda()  {
        // reaccion al boton de retoceder

        Intent intentoNew = new Intent(ComandaActivity.this, _usParam.getVolver_login()==1 ? LoginActivity.class : MesasActivity.class );
        intentoNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //falta acondicionar si se enviara al login
        SessionPreferences.get(ComandaActivity.this).setMesa("0");

        if (_usParam.getVolver_login()==1) {
            SessionPreferences.get(getApplicationContext()).loginClose();
        }
        startActivity(intentoNew);
        finish();
    }

    //region total cuenta , recarga comanda , cargar producto en lista

    public  void cargarProductos(){
        // metodo que buscara los productos en la base de datos y cargara la lista.
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaActivity.this,getLayoutInflater(),"Cargando Comanda","Espere");
            dialogActivity.show();

            try {
                //consultar a la base de datos
                final Call<List<ComandaProductos>> llamarComanda = objComandaApi.comandaMesas(esquema, mesaSel, _userActual.getUser_id());

                //metodo enqueque
                llamarComanda.enqueue(new Callback<List<ComandaProductos>>() {
                    @Override
                    public void onResponse(Call<List<ComandaProductos>> call, Response<List<ComandaProductos>> response) {
                        listaProductos = response.body();
                        if (listaProductos.size() >= 0 ) {
                            // si solo tiene un solo registro
                            if (listaProductos.get(0).getSrv_id() != 0) {
                                adaptador = new ComandaProductosGridAdapter(ComandaActivity.this, 0, listaProductos);
                                gvComanda.setAdapter(adaptador);
                            }
                        }
                        dialogActivity.cancel();
                        totalPagar();
                    }

                    @Override
                    public void onFailure(Call<List<ComandaProductos>> call, Throwable t) {
                        //toast , realizara la muestra del mensaje
                        Mensajes.mensajeToas(ComandaActivity.this,t.getMessage());
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(ComandaActivity.this,e.getMessage());
                dialogActivity.cancel();
            }
        }
    }

    private void totalPagar() {
        //calcular la precuenta
        double monto=0;
        for (ComandaProductos item : listaProductos)
        {   //sumamos los montos
            monto +=item.getProd_precio();
        }
        String montoPago = String.format("%.2f",monto).replace(",",".");
        totalPagar.setText("S/."+montoPago);
    }

    private void recargarComanda(){
        // volver a llamar a la misma actividad
        Intent intentoNew = new Intent(ComandaActivity.this, ComandaActivity.class);
        intentoNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //llamamos a la actividad
        startActivity(intentoNew);
        finish();
    }

    //endregion

    //region Cancelar Producto

    private void retornarStockProducto() {
        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(ComandaActivity.this);
        dialogo2.setTitle("Importante");
        dialogo2.setMessage("¿Reintegrar el producto al kardex?");
        dialogo2.setCancelable(true);

        dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // si se cancela y reimprime , restaurar los valores
                for(ComandaProductos item : listaProductos)
                {
                    if (item.getComa_det_id() == objComanReimprimir.getComa_det_id())
                    {
                        item.setComa_cab_crea_est(estadoCancelado);
                    }
                }

                adaptador=new ComandaProductosGridAdapter(ComandaActivity.this,0,listaProductos);
                gvComanda.setAdapter(adaptador);
                cancelarProductoReintegrarStock();
            }
        });

        dialogo2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //se realizara el agregado del producto , en el kardex
                codigoProducto = objComanReimprimir.getProd_id();
                cancelarProductoReintegrarStock();
            }
        });
        dialogo2.show();
    }

    private void cancelarProductoReintegrarStock() {
        if (!InternerConnection.isConnectedWifi(ComandaActivity.this)) {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        } else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaActivity.this,getLayoutInflater(),"Cancelando Producto","Espere");
            dialogActivity.show();
            try {

                final Call<User> cancelarRetornarProducto = objComandaApi.comandaCancelarItemReintegroKardex(esquema,codigoProducto.equals("0") ? "0" : objComanReimprimir.getProd_id(),_userActual.getUser_id(),objComanReimprimir.getComa_det_id());

                //Mensajes.mensajeToas(ComandaActivity.this, esquema+"|"+(codigoProducto.equals("0") ? "0" : objComanReimprimir.getProd_id()) +"|"+_userActual.getUser_id()+"|"+objComanReimprimir.getComa_det_id());

                cancelarRetornarProducto.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        recargarComanda();
                        dialogActivity.cancel();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Mensajes.mensajeToas(ComandaActivity.this,t.getMessage());
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(ComandaActivity.this,e.getMessage());
                dialogActivity.cancel();
            }
        }

    }

    private void cancelarProductoSinMotivo() {
        // mostrar mensaje
        final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaActivity.this,getLayoutInflater(),"Cancelando Producto","Espere");
        dialogActivity.show();
        try {
            //dependiendo de donde venga el dato se creara uno o otro objeto
            final Call<User> cancelarItemRetorno = objComandaApi.comandaCancelarItemMotivo(esquema, objComanReimprimir.getComa_det_id(),
                    _userActual.getUser_id(),1, objComanReimprimir.getProd_id());

            cancelarItemRetorno.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    recargarComanda();
                    dialogActivity.cancel();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Mensajes.mensajeToas(ComandaActivity.this, t.getMessage());
                    dialogActivity.cancel();
                }
            });

        } catch (Exception e) {
            Mensajes.mensajeToas(ComandaActivity.this,e.getMessage());
            dialogActivity.cancel();
        }finally {

        }

    }

    //endregion

    //region Imprimir , reImprimir , comprobar stock

    private void comprobarStock()
    {   //enviar una orden a la bd , para realizar la impresion
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaActivity.this,getLayoutInflater(),"Comprobando Stock","Espere");
            dialogActivity.show();
            try {
                for (ComandaProductos item : listaProductos)
                {   //sumamos los montos
                    if (item.getComa_cab_crea_est() < 2) {
                        codigoCab = item.getComa_cab_id();
                        break;
                    }
                }

                if (codigoCab > 0) {

                    Call<ApiError> comprobarStock = objComandaApi.productoComprobarStock(esquema,codigoCab);

                    comprobarStock.enqueue(new Callback<ApiError>() {
                        @Override
                        public void onResponse(Call<ApiError> call, Response<ApiError> response) {

                            if (response.body().getCodigo() == 0) {
                                guardarImprimir();
                            } else {
                                Mensajes.mensajeToas(ComandaActivity.this,response.body().getMensaje());
                                dialogActivity.cancel();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiError> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                            dialogActivity.cancel();
                        }
                    });
                } else {
                    Mensajes.mensajeToas(ComandaActivity.this,"No hay productos para la impresión");
                    dialogActivity.cancel();
                }

            } catch (Exception e) {
                Mensajes.mensajeToas(ComandaActivity.this,e.getMessage());
                dialogActivity.cancel();
            }
        }
    }

    private void guardarImprimir()
    {   //enviar una orden a la bd , para realizar la impresion
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaActivity.this,getLayoutInflater(),"Imprimiendo Comanda","Espere");
            dialogActivity.show();
            try {

                for (ComandaProductos item : listaProductos)
                {   //sumamos los montos
                    if (item.getComa_cab_crea_est() < 2) {
                        codigoCab = item.getComa_cab_id();
                        break;
                    }
                }
                if (codigoCab > 0) {

                    Call<User> cerrarComanda = objComandaApi.comandaCerrar(esquema,codigoCab,_userActual.getUser_id());

                    cerrarComanda.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                                salirComanda();
                                dialogActivity.cancel();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                            dialogActivity.cancel();
                        }
                    });
                } else {
                    Mensajes.mensajeToas(ComandaActivity.this,"No hay productos para la impresión");
                    dialogActivity.cancel();
                }

            } catch (Exception e) {
                Mensajes.mensajeToas(ComandaActivity.this,e.getMessage());
                dialogActivity.cancel();
            }
        }
    }

    private void preCuentaImprimir()
    {   //enviar una orden a la bd , para realizar la impresion
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaActivity.this,getLayoutInflater(),"Imprimiendo Precuenta","Espere");
            dialogActivity.show();
            try {
                int codigoSrv = 0;
                for (ComandaProductos item : listaProductos)
                {
                    //buscar los estados incompletos
                    codigoSrv = item.getSrv_id();
                    break;
                }
                if (codigoSrv > 0) {

                    Call<User> comandaPrecuenta = objComandaApi.comandaImprimirPrecuenta(esquema,codigoSrv,_userActual.getUser_id());

                    comandaPrecuenta.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                                salirComanda();
                                dialogActivity.cancel();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                            dialogActivity.cancel();
                        }
                    });
                } else {
                    Mensajes.mensajeToas(ComandaActivity.this,"No hay productos para la impresión");
                    dialogActivity.cancel();
                }

            } catch (Exception e) {
                Mensajes.mensajeToas(ComandaActivity.this,e.getMessage());
                dialogActivity.cancel();
            }
        }
    }

    private void ReImprimir()
    {   //enviar una orden a la bd , para realizar la reeImpresion
        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
            salirComanda();
        }else {
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(ComandaActivity.this,getLayoutInflater(),"Reimprimiendo Comanda","Espere");
            dialogActivity.show();
            try {
                Call<User> cerrarComanda = objComandaApi.comandaReeImprimir(esquema,objComanReimprimir.getComa_cab_id(),_userActual.getUser_id());
                cerrarComanda.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        salirComanda();
                        dialogActivity.cancel();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Mensajes.mensajeToas(ComandaActivity.this,e.getMessage());
                dialogActivity.cancel();
            }
        }
        recargarComanda();
    }

    //endregion

}
