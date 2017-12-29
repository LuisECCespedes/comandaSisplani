package com.example.fluc.siservis_comanda.data.modelo;

import java.io.Serializable;

/**
 * Created by fluc on 25/11/2017.
 */

public class ComandaProductos implements Serializable{
    private int srv_id ;
    private int coma_cab_id;
    private int coma_det_id ;
    private int coma_cab_crea_est ;
    private int kdx_id ;
    private int prod_st_control ;
    private int prod_prepa_rap ;
    private int pedido_enviar_ind ;
    private String vend_user_id ;
    private String vend_user_nombre ;
    private String prod_id ;
    private String prod_descri ;
    private Double prod_precio;

    public int getSrv_id() {
        return srv_id;
    }

    public void setSrv_id(int srv_id) {
        this.srv_id = srv_id;
    }

    public int getComa_cab_id() {
        return coma_cab_id;
    }

    public void setComa_cab_id(int coma_cab_id) {
        this.coma_cab_id = coma_cab_id;
    }

    public int getComa_det_id() {
        return coma_det_id;
    }

    public void setComa_det_id(int coma_det_id) {
        this.coma_det_id = coma_det_id;
    }

    public int getComa_cab_crea_est() {
        return coma_cab_crea_est;
    }

    public void setComa_cab_crea_est(int coma_cab_crea_est) {
        this.coma_cab_crea_est = coma_cab_crea_est;
    }

    public int getKdx_id() {
        return kdx_id;
    }

    public void setKdx_id(int kdx_id) {
        this.kdx_id = kdx_id;
    }

    public int getProd_st_control() {
        return prod_st_control;
    }

    public void setProd_st_control(int prod_st_control) {
        this.prod_st_control = prod_st_control;
    }

    public int getProd_prepa_rap() {
        return prod_prepa_rap;
    }

    public void setProd_prepa_rap(int prod_prepa_rap) {
        this.prod_prepa_rap = prod_prepa_rap;
    }

    public int getPedido_enviar_ind() {
        return pedido_enviar_ind;
    }

    public void setPedido_enviar_ind(int pedido_enviar_ind) {
        this.pedido_enviar_ind = pedido_enviar_ind;
    }

    public String getVend_user_id() {
        return vend_user_id;
    }

    public void setVend_user_id(String vend_user_id) {
        this.vend_user_id = vend_user_id;
    }

    public String getVend_user_nombre() {
        return vend_user_nombre;
    }

    public void setVend_user_nombre(String vend_user_nombre) {
        this.vend_user_nombre = vend_user_nombre;
    }

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

    public Double getProd_precio() {
        return prod_precio;
    }

    public void setProd_precio(Double prod_precio) {
        this.prod_precio = prod_precio;
    }

    public ComandaProductos(int srv_id, int coma_cab_id, int coma_det_id, int coma_cab_crea_est, int kdx_id, int prod_st_control, int prod_prepa_rap, int pedido_enviar_ind, String vend_user_id, String vend_user_nombre, String prod_id, String prod_descri, Double prod_precio) {

        this.srv_id = srv_id;
        this.coma_cab_id = coma_cab_id;
        this.coma_det_id = coma_det_id;
        this.coma_cab_crea_est = coma_cab_crea_est;
        this.kdx_id = kdx_id;
        this.prod_st_control = prod_st_control;
        this.prod_prepa_rap = prod_prepa_rap;
        this.pedido_enviar_ind = pedido_enviar_ind;
        this.vend_user_id = vend_user_id;
        this.vend_user_nombre = vend_user_nombre;
        this.prod_id = prod_id;
        this.prod_descri = prod_descri;
        this.prod_precio = prod_precio;
    }

    public ComandaProductos() {

    }
}