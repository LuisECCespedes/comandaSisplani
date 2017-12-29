package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.fluc.siservis_comanda.adapter.EspecificacionesGridAdapter;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.Descuento;
import com.example.fluc.siservis_comanda.data.modelo.Especificaciones;
import com.example.fluc.siservis_comanda.data.modelo.Productos;
import com.example.fluc.siservis_comanda.data.modelo.User;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.ConvertirJson;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class AgregarCancelarActivity extends AppCompatActivity implements View.OnClickListener {
    private EspecificacionesGridAdapter adaptador;
    private Button btnMenos , btnMas,btnAceptar,btnCancelar , btnDescuento;
    private EditText etDescri, etPrecio , etCantidad , etEspecExtra;
    private LinearLayout llPrecio;
    private String mesaSel , especExtra = "", esquema , especCodigo = "";
    private Double especMonto=0.0;
    private ComandaProductos objComanda;
    private Productos objProducto;
    private Descuento objDescuento = new Descuento(1,0.0,0.0,0.0,0.0);
    private List<Especificaciones> objListaEspec = new ArrayList<Especificaciones>();
    private GridView gvListaEspec;
    private ComandaApi utilApp;

    int mod = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cancelar);

        mesaSel = SessionPreferences.get(AgregarCancelarActivity.this).getMesa();

        this.setTitle("Mesa " + mesaSel);

        esquema = SessionPreferences.get(getApplicationContext()).getEsquemaApp();
        utilApp = ApiUtils.getComandaApi(esquema);

        btnMenos = (Button)findViewById(R.id.btnActACMenos);
        btnMas = (Button)findViewById(R.id.btnActACMas);
        btnAceptar= (Button)findViewById(R.id.btnActACAgregar);
        btnCancelar = (Button)findViewById(R.id.btnActACCancelar);
        btnDescuento = (Button)findViewById(R.id.btActivACDescuento);

        etCantidad = (EditText)findViewById(R.id.etActACCantidad);
        etDescri = (EditText)findViewById(R.id.etActACProdDescri);
        etPrecio = (EditText)findViewById(R.id.etActACProdPrecio);
        etEspecExtra = (EditText)findViewById(R.id.etActACEspcExtra);

        //llPrecio = (LinearLayout) findViewById(R.id.llActACPrecio);

        gvListaEspec = (GridView)findViewById(R.id.gvActACListaEspc);

        btnMenos.setOnClickListener(this);
        btnMas.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnDescuento.setOnClickListener(this);

        if (getIntent().hasExtra("itemComanda"))
        {
            objComanda = (ComandaProductos) getIntent().getSerializableExtra("itemComanda");
        }

        if (getIntent().hasExtra("itemProducto"))
        {
            objProducto = (Productos) getIntent().getSerializableExtra("itemProducto");
            objDescuento.setPrecio(objProducto.getProd_precio());
        }

        if (getIntent().hasExtra("itemDescuento"))
        {
            objDescuento = (Descuento) getIntent().getSerializableExtra("itemDescuento");
        }

        if (getIntent().hasExtra("itemsEspecificaciones"))
        {
            objListaEspec = ConvertirJson.convertirList(getIntent().getStringExtra("itemsEspecificaciones"));
        }

        etDescri.setText(objProducto.getProd_descri());
        etPrecio.setText(objProducto.getProd_precio().toString());
        etPrecio.setEnabled(objProducto.getProd_precio_mod_ind() > 0);
        etEspecExtra.setText(especExtra);

        for(Especificaciones item : objListaEspec )
        {
            especExtra =item.getEspec_text();
            especCodigo+=String.format("%1$-6s","" + item.getEspec_id());
            especMonto += item.getEspec_precio();
            etEspecExtra.setText(especExtra);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cargarGriaEspecificaciones();
        etCantidad.requestFocus();

        //objProducto.setProd_precio(Double.parseDouble(etPrecio.getText().toString()));
        //objDescuento.setPrecio(objProducto.getProd_precio());

        etPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                objDescuento.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });
    }

    private void cargarGriaEspecificaciones() {
        // cargamos al adaptador con la estructura
        adaptador = new EspecificacionesGridAdapter(AgregarCancelarActivity.this,0,objListaEspec);

        // cargamos la gria , con el adaptador
        gvListaEspec.setAdapter(adaptador);
    }

    @Override
    public void onClick(View view) {
        int nCantidad = Integer.parseInt(etCantidad.length() > 0 ? etCantidad.getText().toString() : "0");

        switch (view.getId())
        {
            case R.id.btnActACMas:
                nCantidad++;
                etCantidad.setText(nCantidad+"");
                break;

            case R.id.btnActACMenos:
                nCantidad--;
                if (nCantidad < 1) nCantidad = 1;
                etCantidad.setText(nCantidad+"");
                break;

            case R.id.btActivACDescuento:
                descuento();
                break;

            case R.id.btnActACCancelar:
                Intent intComanda = new Intent(AgregarCancelarActivity.this, ComandaActivity.class);
                intComanda.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intComanda);
                finish();
                break;

            default:

                if (nCantidad != 0) {
                    ingresarProducto(nCantidad);
                    break;
                } else {
                    Mensajes.mensajeToas(AgregarCancelarActivity.this,"Ingrese una cantidad");
                }
        }
    }

    private void descuento() {
        //guardar la modificacion del precio
        Intent intentoDescuento = new Intent(AgregarCancelarActivity.this,DescuentoActivity.class);
        intentoDescuento.putExtra("itemProducto",objProducto);
        intentoDescuento.putExtra("itemComanda",objComanda);
        intentoDescuento.putExtra("itemDescuento",objDescuento);
        intentoDescuento.putExtra("itemsEspecificaciones", ConvertirJson.convertirList(objListaEspec));
        startActivity(intentoDescuento);
    }

    private void ingresarProducto(int nCantidad) {

        if (!InternerConnection.isConnectedWifi(getApplicationContext()))
        {
            Mensajes.mensajeToas(getApplicationContext(), "No esta conectado a un red Wifi");
        }else{
            // mostrar mensaje
            final Dialog dialogActivity = Mensajes.mensajeDialogo(AgregarCancelarActivity.this,getLayoutInflater(),"Agregando Productos","Espere");
            dialogActivity.show();
            try {
                if (Double.parseDouble(etPrecio.getText().toString()) != objDescuento.getTotal()) {
                    objDescuento.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
                }
                final Call<User> ingresarNuevo = utilApp.comandaNuevoProducto(esquema, mesaSel, objComanda.getVend_user_id(), objProducto.getProd_id(),
                        objDescuento.getPrecio().toString(), String.valueOf(especMonto), objDescuento.getDescuento().toString(), especCodigo.length() > 0 ? especCodigo : "XXjXX",
                        especExtra.length() > 0 ? especExtra : "XXjXX",nCantidad);

                ingresarNuevo.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        // enviar al login , una ves atendido
                        Intent intComanda = new Intent(AgregarCancelarActivity.this, (false ? LoginActivity.class : ComandaActivity.class));

                        intComanda.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intComanda);
                        dialogActivity.cancel();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Mensajes.mensajeToas(AgregarCancelarActivity.this,t.getMessage());
                        dialogActivity.cancel();
                    }
                });
            } catch (Exception e) {
                Log.e("Error al desconocido", "Regrese a la pantalla anterior", e);
                dialogActivity.cancel();
            }finally {

            }
        }
    }
}
