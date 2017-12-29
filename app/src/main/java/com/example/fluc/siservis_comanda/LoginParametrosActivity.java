package com.example.fluc.siservis_comanda;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.fluc.siservis_comanda.customfonts.MyEditText;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

public class LoginParametrosActivity extends AppCompatActivity implements View.OnClickListener {
    //private MyEditText email, pass , url ,esquema;
    private EditText email, pass , url ,esquema;
    private EditText txtTam , txtSize;
    private Button btnGuardar,btnMas,btnMenos;
    private CheckBox chkRefresca;
    private boolean bRefrecar;
    private Float nSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_parametros);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bRefrecar = SessionPreferences.get(getApplicationContext()).getRefrescar();

        // tomamos el control y lo asignamos a la variable
        url     = (EditText) findViewById(R.id.txtUrl);
        //url     = (MyEditText)findViewById(R.id.txtUrl);
        //esquema = (MyEditText)findViewById(R.id.txtEsquema);
        esquema = (EditText) findViewById(R.id.txtEsquema);
        txtTam = (EditText)findViewById(R.id.txtTextoPrueba);
        txtSize = (EditText)findViewById(R.id.etAcLogParTam);

        nSize = SessionPreferences.get(getApplicationContext()).getLetraSize();
        txtTam.setTextSize(nSize);
        txtSize.setText(""+nSize);

        // tomamos el control y lo asignamos a la variable
        btnGuardar= (Button)findViewById(R.id.btnActLogParGuardar);
        btnMenos= (Button)findViewById(R.id.btnMas);
        btnMas= (Button)findViewById(R.id.btnMenos);

        //check
        chkRefresca = (CheckBox)findViewById(R.id.chkRefresca);

        // cargamos las variables
        chkRefresca.setChecked(bRefrecar);

        // mostramos la ruta ip , desfraccionada
        String varUrl= SessionPreferences.get(getApplicationContext()).getUrlApiApp();
        varUrl = varUrl.substring(7,varUrl.indexOf("comanda")-1);
        url.setText(varUrl);
        esquema.setText(SessionPreferences.get(getApplicationContext()).getEsquemaApp());

        // mencionamos que utilizaremos los botones
        btnGuardar.setOnClickListener(this);
        btnMas.setOnClickListener(this);
        btnMenos.setOnClickListener(this);

    }

    // controlador de botones
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //boton Login
            case R.id.btnActLogParGuardar:
                // mostrar mensaje
                final Dialog dialogActivity = Mensajes.mensajeDialogo(LoginParametrosActivity.this,getLayoutInflater(),"Guardando Parametros","Espere");
                dialogActivity.show();
                SessionPreferences.get(getApplicationContext()).setRefrescar(chkRefresca.isChecked());
                SessionPreferences.get(getApplicationContext()).setEsquemaApp(esquema.getText().toString());
                SessionPreferences.get(getApplicationContext()).setUrlApiApp(url.getText().toString());
                SessionPreferences.get(getApplicationContext()).setLetraSize(nSize);
                dialogActivity.cancel();
                Intent intLogin = new Intent(LoginParametrosActivity.this,LoginActivity.class);
                intLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(intLogin);
                break;

            case R.id.btnMas :
                if (nSize<51) {
                    nSize++;
                    txtTam.setTextSize(nSize);
                    txtSize.setText(""+nSize);
                }
                break;

            case R.id.btnMenos :

                if (nSize>14) {
                    nSize--;
                    txtTam.setTextSize(nSize);
                    txtSize.setText(""+nSize);
                }
                break;
        }
    }

}
