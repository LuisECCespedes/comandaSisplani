package com.example.fluc.siservis_comanda.data.modelo;

import java.io.Serializable;

/**
 * Created by luis on 29/11/2017.
 */

public class Descuento implements Serializable{
    private int tipo;
    private Double cantidad;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Descuento(int tipo, Double cantidad, Double precio, Double descuento, Double total) {

        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.total = total;
    }

    public Descuento() {

    }

    private Double precio;
    private Double descuento;
    private Double total;

}
