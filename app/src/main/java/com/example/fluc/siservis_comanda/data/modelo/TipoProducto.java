package com.example.fluc.siservis_comanda.data.modelo;

import java.io.Serializable;

/**
 * Created by fluc on 25/11/2017.
 */

public class TipoProducto implements Serializable{
    private String prod_tipo_id;
    private String prod_tipo_descri;

    public TipoProducto(String prod_tipo_id, String prod_tipo_descri) {
        this.prod_tipo_id = prod_tipo_id;
        this.prod_tipo_descri = prod_tipo_descri;
    }

    public TipoProducto() {

    }

    public String getProd_tipo_id() {
        return prod_tipo_id;
    }

    public void setProd_tipo_id(String prod_tipo_id) {
        this.prod_tipo_id = prod_tipo_id;
    }

    public String getProd_tipo_descri() {
        return prod_tipo_descri;
    }

    public void setProd_tipo_descri(String prod_tipo_descri) {
        this.prod_tipo_descri = prod_tipo_descri;
    }
}
