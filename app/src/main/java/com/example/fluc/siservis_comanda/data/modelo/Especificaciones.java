package com.example.fluc.siservis_comanda.data.modelo;

/**
 * Created by fluc on 25/11/2017.
 */

public class Especificaciones {
    private String espec_id ;
    private String espec_descri ;
    private String espec_text;
    private Double espec_precio ;
    private int espec_ind;

    public Especificaciones(String espec_id, String espec_descri, String espec_text, Double espec_precio, int espec_ind) {
        this.espec_id = espec_id;
        this.espec_descri = espec_descri;
        this.espec_text = espec_text;
        this.espec_precio = espec_precio;
        this.espec_ind = espec_ind;
    }

    public Especificaciones() {
    }

    public String getEspec_id() {
        return espec_id;
    }

    public void setEspec_id(String espec_id) {
        this.espec_id = espec_id;
    }

    public String getEspec_descri() {
        return espec_descri;
    }

    public void setEspec_descri(String espec_descri) {
        this.espec_descri = espec_descri;
    }

    public String getEspec_text() {
        return espec_text;
    }

    public void setEspec_text(String espec_text) {
        this.espec_text = espec_text;
    }

    public Double getEspec_precio() {
        return espec_precio;
    }

    public void setEspec_precio(Double espec_precio) {
        this.espec_precio = espec_precio;
    }

    public int getEspec_ind() {
        return espec_ind;
    }

    public void setEspec_ind(int espec_ind) {
        this.espec_ind = espec_ind;
    }
}
