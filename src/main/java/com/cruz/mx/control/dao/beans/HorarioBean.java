/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.dao.beans;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author acruzb
 */
@Document(collection = "HORARIO")
public class HorarioBean {
    
    private int idHorario;
    private String entrada;
    private String comida;
    private String regreso;
    private String salida;

    public HorarioBean() {
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getComida() {
        return comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public String getRegreso() {
        return regreso;
    }

    public void setRegreso(String regreso) {
        this.regreso = regreso;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }
    
    
}
