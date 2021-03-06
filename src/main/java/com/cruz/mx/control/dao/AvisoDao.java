/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.dao;

import com.cruz.mx.control.dao.beans.AvisoBean;
import com.cruz.mx.control.utils.FechaUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author acruzb
 */
@Repository
public class AvisoDao {
    
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mt;
    
    public List<AvisoBean> consultarAvisosIndividual(String clave, String fecha) {
        Query query = new Query();
        query.addCriteria(
            Criteria.where("clave").is(clave)
                .andOperator(
                    Criteria.where("inicio").lte(fecha),
                    Criteria.where("fin").gte(fecha)
                )
        );
        query.with(new Sort(Sort.Direction.ASC, "inicio"));
        return mt.find(query, AvisoBean.class);
    }
    
    public List<AvisoBean> consultarAvisosGeneral(String fecha) {
        Query query = new Query();
        query.addCriteria(
            Criteria.where("tipo").is(AvisoBean.TIPO_GENERAL)
                .andOperator(
                    Criteria.where("inicio").lte(fecha),
                    Criteria.where("fin").gte(fecha)
                )
        );
        query.with(new Sort(Sort.Direction.ASC, "inicio"));
        return mt.find(query, AvisoBean.class);
    }
    
    public void agregarAviso(AvisoBean aviso){
        mt.insert(aviso);
    }
    
    public List<AvisoBean> consultarTodo(){
        Query query = new Query();
        query.addCriteria(
            new Criteria().andOperator(
                Criteria.where("inicio").lte(FechaUtils.getFechaHoy()),
                Criteria.where("fin").gte(FechaUtils.getFechaHoy())
            )
        );
        query.with(new Sort(Sort.Direction.ASC, "inicio"));
        System.out.println(query);
        return mt.find(query, AvisoBean.class);
    }
    
    public void eliminarAviso(AvisoBean aviso){
        Criteria c = Criteria.where("_id").is(aviso.getId());
        Query query = new Query(c);
        mt.remove(query, AvisoBean.class);
    }
    
}
