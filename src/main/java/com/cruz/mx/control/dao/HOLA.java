/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.dao;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author acruzb
 */
@Component
public class HOLA {
    
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mt;
    
//    @PostConstruct
    public void esri(){
        HashMap myMap = new HashMap<String, String>();
        myMap.put("a", "b");
        myMap.put("c", "d");
        mt.insert(myMap, "PRUEBA");
    }
    
}
