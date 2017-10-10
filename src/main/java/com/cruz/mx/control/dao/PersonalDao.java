/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.dao;

import com.cruz.mx.control.dao.beans.PersonalBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
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
public class PersonalDao {
    
    private static final int NUM_REG_PAG = 3;
    
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mt;
    
    public PersonalBean existPersonal(PersonalBean personal){
        Criteria c = Criteria.where("clave").is(personal.getClave());
        Query query = new Query(c);
        return mt.findOne(query, PersonalBean.class);
    }
    
    public List<PersonalBean> existPersonalNombre(PersonalBean personal){
        Criteria c = Criteria.where("nombre").regex(".*"+ personal.getNombre() +".*").
                and("aPaterno").regex(".*"+ personal.getaPaterno()+".*").
                and("aMaterno").regex(".*"+ personal.getaMaterno() +".*");
        Query query = new Query(c);
        query.limit(10);
        return mt.find(query, PersonalBean.class);
    }
    
    public List<PersonalBean> consultarTodo(int pagina){
        Query query = new Query();
        query.with(new PageRequest(pagina, NUM_REG_PAG));
        query.with(new Sort(Sort.Direction.ASC, "clave"));
        return mt.find(query, PersonalBean.class);
    }
    
    public void agregarPersonal(PersonalBean personal){
        mt.insert(personal);
    }
    
    public void eliminarPersonal(PersonalBean personal){
        Criteria c = Criteria.where("clave").is(personal.getClave());
        Query query = new Query(c);
        mt.remove(query, PersonalBean.class);
    }
    
    public void editarPersonal(PersonalBean personal){
        Criteria c = Criteria.where("clave").is(personal.getClave());
        Query query = new Query(c);
        Update update = new Update();
        update.set("nombre", personal.getNombre());
        update.set("aPaterno", personal.getaPaterno());
        update.set("aMaterno", personal.getaMaterno());
        update.set("RL", personal.getRL());
        mt.updateFirst(query, update, PersonalBean.class);
    }
    
}
