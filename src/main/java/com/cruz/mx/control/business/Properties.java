/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.business;

import com.cruz.mx.control.vistas.Principal;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author acruzb
 */
public class Properties {
    
    private static final Logger LOGGER = Logger.getLogger(Properties.class);
    public static String VERSION_SISTEMA;
    public static int TIEMPO_DIALOG_CONFIRMACION;
    
    static{
        java.util.Properties prop = new java.util.Properties();
    	InputStream input = null;
    	try {
            String filename = "properties/file.properties";
            input = Principal.class.getClassLoader().getResourceAsStream(filename);
            if(input == null){
                LOGGER.info("Lo sentimos, No se ha encontrado el archivos de propiedades: " + filename);
            }
            //load a properties file from class path, inside static method
            prop.load(input);
            //get the property value and print it out
            LOGGER.info("Se leen el archivo de propiedades de paths: " + prop.getProperty("version"));
            LOGGER.info("Se leen el archivo de propiedades de paths");
            TIEMPO_DIALOG_CONFIRMACION = Integer.parseInt(prop.getProperty("tiempo.dialog.confirmacion"));
    	} catch (IOException ex) {
            LOGGER.info("No se pudo cargar el archivo de propiedades de paths");
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {}
            }
        }
    }
    
}
