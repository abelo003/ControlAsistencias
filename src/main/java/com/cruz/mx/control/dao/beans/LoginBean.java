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
@Document(collection = "USUARIO")
public class LoginBean {
    
    private String nombreUsuario;
    private String pwd;

    public LoginBean() {
    }

    public LoginBean(String nombreUsuario, String pwd) {
        this.nombreUsuario = nombreUsuario;
        this.pwd = pwd;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
}
