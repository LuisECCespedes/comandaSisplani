package com.example.fluc.siservis_comanda;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.Descuento;
import com.example.fluc.siservis_comanda.data.modelo.Especificaciones;
import com.example.fluc.siservis_comanda.data.modelo.Productos;
import com.example.fluc.siservis_comanda.data.util.ConvertirJson;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DescuentoActivity extends AppCompatActivity implements View.OnClickListener{

    private ComandaProductos objComanda;
    private Productos objProducto;
    private Descuento objDescuento = new Descuento(1,0.0,0.0,0.0,0.0);
    private List<Especificaciones> objListaEspec = new ArrayList<Especificaciones>();

    private int tipoDes = 1;
    private Double monto , precio , descuento ,total;

    private Button btnSoles,btnPorc ,btnMontoCero,btnMas,btnMenos,btnAplicar,btnCancelar;
    private EditText etMonto,etPrecio,etDescuento,etTotal,etProdDescri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descuento);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("Descuentos");

        etMonto= (EditText) findViewById(R.id.etActDescMonto);
        etPrecio= (EditText) findViewById(R.id.etActDescPrecio);
        etDescuento= (EditText) findViewById(R.id.etActDescDescuento);
        etTotal= (EditText) findViewById(R.id.etActDescTotal);
        etProdDescri= (EditText) findViewById(R.id.etActDescProdDescri);

        btnSoles= (Button)findViewById(R.id.btActDescSoles);
        btnPorc  = (Button)findViewById(R.id.btActDescPr);
        btnMontoCero = (Button)findViewById(R.id.btActDescMontoCero);
        btnMas = (Button)findViewById(R.id.btActDescMas);
        btnMenos = (Button)findViewById(R.id.btActDescMenos);
        btnAplicar = (Button)findViewById(R.id.btActDescAplicar);
        btnCancelar= (Button)findViewById(R.id.btActDescCancelar);

        btnSoles.setOnClickListener(this);
        btnPorc.setOnClickListener(this);
        btnMontoCero.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnAplicar.setOnClickListener(this);
        btnMenos.setOnClickListener(this);
        btnMas.setOnClickListener(this);

        if (getIntent().hasExtra("itemComanda"))
        {
            objComanda = (ComandaProductos) getIntent().getSerializableExtra("itemComanda");
        }

        if (getIntent().hasExtra("itemProducto"))
        {
            objProducto = (Productos) getIntent().getSerializableExtra("itemProducto");
        }

        //si existe se tomas los datos para los controles
        if (getIntent().hasExtra("itemDescuento"))
        {
            objDescuento = (Descuento) getIntent().getSerializableExtra("itemDescuento");
        }

        if (getIntent().hasExtra("itemsEspecificaciones"))
        {
            objListaEspec = ConvertirJson.convertirList(getIntent().getStringExtra("itemsEspecificaciones"));
        }

        cargarParametros();

        etMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculo();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });
    }

    private void cargarParametros() {

        //cargamos los valores de los controles
        tipoDes = objDescuento.getTipo();

        etMonto.setText(objDescuento.getCantidad().toString());
        etPrecio.setText(objDescuento.getPrecio().toString());
        etProdDescri.setText(objProducto.getProd_descri());
        calculo();
        etMonto.requestFocus();
    }

    private void calculo() {
        //cargamos los valores de los controles
        btnSoles.setBackgroundColor(tipoDes == 1 ? Color.GREEN : Color.WHITE);
        btnPorc.setBackgroundColor(tipoDes == 2 ? Color.GREEN : Color.WHITE);
        btnMontoCero.setBackgroundColor(tipoDes == 3 ? Color.GREEN : Color.WHITE);

        //monto a disminuir
        monto = Double.parseDouble(etMonto.getText().length() < 1 ? "0" : etMonto.getText().toString());

        // si es porcentaje y ha superado el 100%
        monto = tipoDes == 2 && monto > 100 ? 100 : monto;

        // precio
        precio = Double.parseDouble(etPrecio.getText().toString());

        // si supera el precio
        monto = tipoDes == 1 && monto > precio ? precio : monto;

        // 1 -- Monto por cantidad , 2 -- Monto por porcentaje , 3 -- Monto Cero
        descuento = (tipoDes == 1 ? monto : (tipoDes == 2 ? precio * (monto / 100) : precio));

        // cargamos el total
        total = precio - descuento;
        String _des = String.format("%.2f",descuento).replace(",","."), _tot = String.format("%.2f",total).replace(",",".");

        // cargamos los controles con los valores determinados
        etMonto.setVisibility(tipoDes == 3 ? View.INVISIBLE : View.VISIBLE);
        etDescuento.setText(_des);
        etTotal.setText(_tot);
    }

    @Override
    public void onBackPressed() {

        enviar(false);
    }

    public  void enviar(boolean benviar)
    {
        Intent intentoDescuento = new Intent(DescuentoActivity.this,AgregarCancelarActivity.class);

        if (benviar) {
            objDescuento.setTipo(tipoDes);

            objDescuento.setTotal(Double.parseDouble(etTotal.getText().toString()));
            objDescuento.setCantidad(Double.parseDouble(etMonto.getText().toString()));
            objDescuento.setDescuento(Double.parseDouble(etDescuento.getText().toString()));
            objProducto.setProd_precio(Double.parseDouble(etTotal.getText().toString()));
        }

        intentoDescuento.putExtra("itemProducto",objProducto);
        intentoDescuento.putExtra("itemComanda",objComanda);
        intentoDescuento.putExtra("itemDescuento",objDescuento);
        intentoDescuento.putExtra("itemsEspecificaciones", ConvertirJson.convertirList(objListaEspec));
        startActivity(intentoDescuento);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btActDescMenos:
                etMonto.setText(String.valueOf(Double.parseDouble(etMonto.getText().toString()) - 1));
                if(Double.parseDouble(etMonto.getText().toString()) < 1)
                {
                    etMonto.setText("0");
                }
                calculo();
                break;

            case R.id.btActDescMas:
                etMonto.setText(String.valueOf(Double.parseDouble(etMonto.getText().toString()) + 1));
                calculo();
                break;

            case R.id.btActDescAplicar:

                enviar(true);
                break;

            case R.id.btActDescCancelar:
                // tomar todos los objetos y reenviarlos
                enviar(false);
                break;

            case R.id.btActDescSoles:
                tipoDes = 1;
                calculo();
                break;

            case R.id.btActDescPr:
                tipoDes = 2;
                calculo();
                break;

            case R.id.btActDescMontoCero:
                tipoDes = 3;
                calculo();
                break;

            default:

                break;
        }
    }

}
