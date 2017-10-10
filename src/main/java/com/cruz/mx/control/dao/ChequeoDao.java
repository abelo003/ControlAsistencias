/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.dao;

import com.cruz.mx.control.dao.beans.ChequeoBean;
import com.cruz.mx.control.dao.beans.PersonalBean;
import com.cruz.mx.control.enums.EstadosChequeo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 *
 * @author acruzb
 */
@Repository
public class ChequeoDao {

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mt;
    
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy,hh:mm a");

    public List<ChequeoBean> consultarChequeo(String clave, String fechaInicial, String fechaFinal) {
        Query query = new Query();
        query.addCriteria(
            Criteria.where("clave").is(clave)
                .andOperator(
                    Criteria.where("fecha").gte(fechaInicial),
                    Criteria.where("fecha").lte(fechaFinal)
                )
        );
        query.with(new Sort(Sort.Direction.ASC, "fecha"));
        return mt.find(query, ChequeoBean.class);
    }
    
    public boolean existChequeoHoy(PersonalBean personal){
        Criteria c = Criteria.where("clave").is(personal.getClave()).
            and("fecha").is(getFechaHoy());
        Query query = new Query(c);
        return mt.find(query, ChequeoBean.class).size() > 0;
    }
    
    public void checarEntrada(PersonalBean personal){
        String horaActual = getHoraActual();
        ChequeoBean chequeo = new ChequeoBean();
        chequeo.setClave(personal.getClave());
        chequeo.setFecha(getFechaHoy());
        chequeo.setEntrada(horaActual);
        chequeo.setSalida(horaActual);
        chequeo.setComentario(EstadosChequeo.DIA_NO_TERMINADO.getComentario());
        mt.insert(chequeo);
    }
    
    public void actualizarSalida(PersonalBean personal){
        Criteria c = Criteria.where("clave").is(personal.getClave()).
            and("fecha").is(getFechaHoy());
        Query query = new Query(c);
        Update update = new Update();
        update.set("salida", getHoraActual());
        mt.updateFirst(query, update, ChequeoBean.class);
    }
    
    private String getFechaHoy(){
        return SDF.format(new Date()).split(",")[0];
    }
    
    private String getHoraActual(){
        return SDF.format(new Date()).split(",")[1];
    }

}
