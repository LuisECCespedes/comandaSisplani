package com.example.fluc.siservis_comanda.data.modelo;

import java.io.Serializable;

/**
 * Created by fluc on 25/11/2017.
 */

public class Productos implements Serializable{

    private String prod_id;
    private String prod_descri;
    private String stock;
    private String texto_lista;
    private int prod_especs_cant;
    private int prod_st_control;
    private int prod_precio_mod_ind;
    private int prod_especif_ind ;
    private int prod_indicador;
    private int prod_prepa_ind;
    private int insum_id ;
    private Double prod_precio;

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_descri() {
        return prod_descri;
    }

    public void setProd_descri(String prod_descri) {
        this.prod_descri = prod_descri;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTexto_lista() {
        return texto_lista;
    }

    public void setTexto_lista(String texto_lista) {
        this.texto_lista = texto_lista;
    }

    public int getProd_especs_cant() {
        return prod_especs_cant;
    }

    public void setProd_especs_cant(int prod_especs_cant) {
        this.prod_especs_cant = prod_especs_cant;
    }

    public int getProd_st_control() {
        return prod_st_control;
    }

    public void setProd_st_control(int prod_st_control) {
        this.prod_st_control = prod_st_control;
    }

    public int getProd_precio_mod_ind() {
        return prod_precio_mod_ind;
    }

    public void setProd_precio_mod_ind(int prod_precio_mod_ind) {
        this.prod_precio_mod_ind = prod_precio_mod_ind;
    }

    public int getProd_especif_ind() {
        return prod_especif_ind;
    }

    public void setProd_especif_ind(int prod_especif_ind) {
        this.prod_especif_ind = prod_especif_ind;
    }

    public int getProd_indicador() {
        return prod_indicador;
    }

    public void setProd_indicador(int prod_indicador) {
        this.prod_indicador = prod_indicador;
    }

    public int getProd_prepa_ind() {
        return prod_prepa_ind;
    }

    public void setProd_prepa_ind(int prod_prepa_ind) {
        this.prod_prepa_ind = prod_prepa_ind;
    }

    public int getInsum_id() {
        return insum_id;
    }

    public void setInsum_id(int insum_id) {
        this.insum_id = insum_id;
    }

    public Double getProd_precio() {
        return prod_precio;
    }

    public void setProd_precio(Double prod_precio) {
        this.prod_precio = prod_precio;
    }

    public Productos() {
    }

    public Productos(String prod_id, String prod_descri, String stock, String texto_lista, int prod_especs_cant, int prod_st_control, int prod_precio_mod_ind, int prod_especif_ind, int prod_indicador, int prod_prepa_ind, int insum_id, Double prod_precio) {
        this.prod_id = prod_id;
        this.prod_descri = prod_descri;
        this.stock = stock;
        this.texto_lista = texto_lista;
        this.prod_especs_cant = prod_especs_cant;
        this.prod_st_control = prod_st_control;
        this.prod_precio_mod_ind = prod_precio_mod_ind;
        this.prod_especif_ind = prod_especif_ind;
        this.prod_indicador = prod_indicador;
        this.prod_prepa_ind = prod_prepa_ind;
        this.insum_id = insum_id;
        this.prod_precio = prod_precio;
    }
}
