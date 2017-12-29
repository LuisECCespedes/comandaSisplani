package com.example.fluc.siservis_comanda.data.modelo;

/**
 * Created by fluc on 21/11/2017.
 */

public class User {
    private int codigo ;
    private int tipoLog;
    private String user_id;
    private String user_pass;
    private String user_nombre ;
    private String mensaje ;
    private String esquema;
    private int volver_login ;
    private int pide_motivo ;
    private int cancelar ;
    private int reintegro_stock;

    public User(String user_id, String user_pass, String esquema, int tipoLog) {
        this.tipoLog = tipoLog;
        this.user_id = user_id;
        this.user_pass = user_pass;
        this.esquema = esquema;
    }

    public User(int codigo, int tipoLog, String user_id, String user_pass, String user_nombre, String mensaje, String esquema, int volver_login, int pide_motivo, int cancelar, int reintegro_stock) {
        this.codigo = codigo;
        this.tipoLog = tipoLog;
        this.user_id = user_id;
        this.user_pass = user_pass;
        this.user_nombre = user_nombre;
        this.mensaje = mensaje;
        this.esquema = esquema;
        this.volver_login = volver_login;
        this.pide_motivo = pide_motivo;
        this.cancelar = cancelar;
        this.reintegro_stock = reintegro_stock;
    }

    public User() {

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getTipoLog() {
        return tipoLog;
    }

    public void setTipoLog(int tipoLog) {
        this.tipoLog = tipoLog;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_nombre() {
        return user_nombre;
    }

    public void setUser_nombre(String user_nombre) {
        this.user_nombre = user_nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }

    public int getVolver_login() {
        return volver_login;
    }

    public void setVolver_login(int volver_login) {
        this.volver_login = volver_login;
    }

    public int getPide_motivo() {
        return pide_motivo;
    }

    public void setPide_motivo(int pide_motivo) {
        this.pide_motivo = pide_motivo;
    }

    public int getCancelar() {
        return cancelar;
    }

    public void setCancelar(int cancelar) {
        this.cancelar = cancelar;
    }

    public int getReintegro_stock() {
        return reintegro_stock;
    }

    public void setReintegro_stock(int reintegro_stock) {
        this.reintegro_stock = reintegro_stock;
    }
}
