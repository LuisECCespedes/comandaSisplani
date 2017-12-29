package com.example.fluc.siservis_comanda.data.modelo;

/**
 * Created by luis on 02/12/2017.
 */

public class MotivosAnulacion {
    private int an_comadet_mot_id ;
    private String an_comadet_mot_descri;

    public MotivosAnulacion(int an_comadet_mot_id, String an_comadet_mot_descri) {
        this.an_comadet_mot_id = an_comadet_mot_id;
        this.an_comadet_mot_descri = an_comadet_mot_descri;
    }

    public MotivosAnulacion() {

    }

    public int getAn_comadet_mot_id() {

        return an_comadet_mot_id;
    }

    public void setAn_comadet_mot_id(int an_comadet_mot_id) {
        this.an_comadet_mot_id = an_comadet_mot_id;
    }

    public String getAn_comadet_mot_descri() {
        return an_comadet_mot_descri;
    }

    public void setAn_comadet_mot_descri(String an_comadet_mot_descri) {
        this.an_comadet_mot_descri = an_comadet_mot_descri;
    }
}
