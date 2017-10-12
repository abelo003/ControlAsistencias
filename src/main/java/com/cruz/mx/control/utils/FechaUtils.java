/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author acruzb
 */
public class FechaUtils {
    
    private final static SimpleDateFormat DATE_FORMAT;
    
    static{
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    public static String getFechaHoy(){
        return DATE_FORMAT.format(new Date());
    }
    
    public static String getFecha(UtilDateModel model){
        return String.format("%02d", model.getDay()) + "/" + String.format("%02d", (model.getMonth() + 1)) + "/" + model.getYear();
    }
    
}
