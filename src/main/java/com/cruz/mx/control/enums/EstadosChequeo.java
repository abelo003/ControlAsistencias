/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.enums;

/**
 *
 * @author acruzb
 */
public enum EstadosChequeo {
    
    NO_LABORAL("Día no laboral"),
    DIA_NO_TERMINADO("Todavía no termina el día"),
    SALIDA_ANTES("Salida antes de horario"),
    FALTA("Falta"),
    CORRECTA("Asistencia Correcta");
    
    private final String comentario;
    
    private EstadosChequeo(String comentario){
        this.comentario = comentario;
    }
    
    public String getComentario(){
        return comentario;
    }
    
}
