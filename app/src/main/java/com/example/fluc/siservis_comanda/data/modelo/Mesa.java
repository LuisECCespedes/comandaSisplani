package com.example.fluc.siservis_comanda.data.modelo;

/**
 * Created by fluc on 23/11/2017.
 */

public class Mesa {
    private String mesa ;
    private String usuario ;
    private String codigo ;
    private String estado;

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Mesa(String mesa, String usuario, String codigo, String estado) {
        this.mesa = mesa;
        this.usuario = usuario;
        this.codigo = codigo;
        this.estado = estado;
    }

    public Mesa() {

    }
}
