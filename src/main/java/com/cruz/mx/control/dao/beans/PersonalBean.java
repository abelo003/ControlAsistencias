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
@Document(collection = "PERSONAL")
public class PersonalBean implements Comparable<PersonalBean>{
    
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private String RL;
    private String clave;
    private String idHorario;
    private String foto;

    public PersonalBean() {
    }

    public PersonalBean(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public String getRL() {
        return RL;
    }

    public void setRL(String RL) {
        this.RL = RL;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(String idHorario) {
        this.idHorario = idHorario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (IOException jpe) {
            return "";
        }
    }

    @Override
    public int compareTo(PersonalBean o) {
        return this.getClave().compareTo(o.getClave());
    }
    
}
