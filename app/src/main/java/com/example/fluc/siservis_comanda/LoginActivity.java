package com.example.fluc.siservis_comanda;

import android.accessibilityservice.GestureDescription;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fluc.siservis_comanda.customfonts.MyEditText;
import com.example.fluc.siservis_comanda.data.api.ApiError;
import com.example.fluc.siservis_comanda.data.api.ApiUtils;
import com.example.fluc.siservis_comanda.data.api.ComandaApi;
import com.example.fluc.siservis_comanda.data.modelo.User;
import com.example.fluc.siservis_comanda.data.preferencias.SessionPreferences;
import com.example.fluc.siservis_comanda.data.util.InternerConnection;
import com.example.fluc.siservis_comanda.data.util.Mensajes;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText pass;
    //private MyEditText pass;
    private RelativeLayout rlUser;
    private Button btnLogin,btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnLimpiar;
    private ComandaApi objComandaApi;
    private String texto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambiar el nomnbre de la actividad
        this.setTitle(R.string.nameBarLogin);
        setContentView(R.layout.activity_login);

        // tomamos el control y lo asignamos a la variable

        // pass    = (MyEditText)findViewById(R.id.password_edittext);
        pass    = (EditText) findViewById(R.id.password_edittext);

        // tomamos el control y lo asignamos a la variable
        btnLogin = (Button)findViewById(R.id.login_button);

        btnLimpiar = (Button)findViewById(R.id.btnActLogLimpiar);
        btn0 = (Button)findViewById(R.id.btnActLogNum0);
        btn1 = (Button)findViewById(R.id.btnActLogNum1);
        btn2 = (Button)findViewById(R.id.btnActLogNum2);
        btn3 = (Button)findViewById(R.id.btnActLogNum3);
        btn4 = (Button)findViewById(R.id.btnActLogNum4);
        btn5 = (Button)findViewById(R.id.btnActLogNum5);
        btn6 = (Button)findViewById(R.id.btnActLogNum6);
        btn7 = (Button)findViewById(R.id.btnActLogNum7);
        btn8 = (Button)findViewById(R.id.btnActLogNum8);
        btn9 = (Button)findViewById(R.id.btnActLogNum9);

        // tomamos el control y lo asignamos a la variable
        rlUser = (RelativeLayout) findViewById(R.id.email_layout);

        // mencionamos que utilizaremos los botones
        btnLogin.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // controlador de botones
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //boton Login
            case R.id.login_button:
                boolean ok = true;
                if (pass.getText().toString().toLowerCase().equals("5847"))
                {
                    Intent intentoParam = new Intent(LoginActivity.this,LoginParametrosActivity.class);
                    pass.setText("");
                    finish();
                    startActivity(intentoParam);
                    ok = false;
                }
                if (InternerConnection.isConnectedWifi(LoginActivity.this)) {
                    // pass
                    if (ok && pass.getText().length() == 0) {
                        Mensajes.mensajeToas(LoginActivity.this,"Falta Contraseña");
                        pass.requestFocus();
                        ok = false;
                    }
                    if (ok) login(pass.getText().toString());
                } else {
                    Mensajes.mensajeToas(LoginActivity.this, "No esta conectado a un red Wifi");
                }

                break;
            case R.id.btnActLogLimpiar:
                pass.setText("");
                texto ="";
                break;

            case R.id.btnActLogNum0:
                texto = pass.getText()+"0";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum1:
                texto = pass.getText()+"1";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum2:
                texto = pass.getText()+"2";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum3:
                texto = pass.getText()+"3";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum4:
                texto = pass.getText()+"4";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum5:
                texto = pass.getText()+"5";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum6:
                texto = pass.getText()+"6";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum7:
                texto = pass.getText()+"7";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum8:
                texto = pass.getText()+"8";
                pass.setText(texto);
                break;

            case R.id.btnActLogNum9:
                texto = pass.getText()+"9";
                pass.setText(texto);
                break;
        }
    }

    public void login(String password)
    {
        final Dialog dialogActivity = Mensajes.mensajeDialogo(LoginActivity.this,getLayoutInflater(),"Verificando Contraseña","Espere");
        dialogActivity.show();
        try {
            // cargamos el api
            objComandaApi = ApiUtils.getComandaApi(SessionPreferences.get(getApplicationContext()).getUrlApiApp());
            Call<User> callLogin = objComandaApi.login(new User("1",password,SessionPreferences.get(getApplicationContext()).getEsquemaApp(),0));

            //metodo de consulta
            callLogin.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    // si no algo ha fallado, retornar el error

                    if (!response.isSuccessful())
                    {
                        String error = response.message();//"Usuario/Contraseña incorrectos";
                        try{
                            if (response.errorBody()
                                    .contentType()
                                    .subtype()
                                    .equals("json")) {
                                ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                                error = apiError.getMensaje();
                                Log.d("LoginActivity", apiError.getMensaje());
                            } else {
                                try {
                                    // Reportar causas de error no relacionado con la API
                                    Log.d("LoginActivity", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    error = e.getMessage();
                                }
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            error = e.getMessage();
                        }
                        dialogActivity.cancel();
                        Mensajes.mensajeToas(LoginActivity.this,error.toString());

                        return;
                    }
                    // Guardar user en preferencias
                    if (response.body().getCodigo() == 1) {
                        //cargamos la preferencia
                        SessionPreferences.get(getApplicationContext()).loginSave(response.body());
                        dialogActivity.cancel();
                        Intent intentoNew = new Intent(getApplicationContext(), MesasActivity.class);
                        intentoNew.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intentoNew);
                        finish();
                    } else {
                        // errorr retornado de la bd
                        Mensajes.mensajeToas(LoginActivity.this,response.body().getMensaje().toString());
                        dialogActivity.cancel();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    //Mensajes.mensajeToas(LoginActivity.this,t);
                    Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    dialogActivity.cancel();
                }
            });
        } catch (Exception e) {
            Log.e("Error al desconocido", "Regrese a la pantalla anterior", e);
            dialogActivity.cancel();
        } finally {
        }
    }
}
