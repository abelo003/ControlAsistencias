/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.dao.beans;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author acruzb
 */
@Document(collection = "CHEQUEO")
public class ChequeoBean {

    private String clave;
    private String fecha;
    private String entrada;
    private String comida;
    private String regreso;
    private String salida;
    private String comentario;

    public ChequeoBean() {
        comida = "00:00";
        regreso = "00:00";
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (IOException jpe) {
            return "";
        }
    }

}
