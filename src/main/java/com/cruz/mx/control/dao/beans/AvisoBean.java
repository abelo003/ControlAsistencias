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
@Document(collection = "AVISO")
public class AvisoBean {
    
    public final static String TIPO_INDIVIDUAL = "INDIVIDUAL";
    public final static String TIPO_GENERAL = "GENERAL";
    
    private String _id;
    private String tipo;
    private String clave;
    private String inicio;
    private String fin;
    private String contenido;

    public AvisoBean() {
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
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
