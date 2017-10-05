/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cruz.mx.control.dao;

import com.cruz.mx.control.dao.beans.LoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author acruzb
 */
@Repository
public class LoginDao {
    
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mt;
    
    public boolean existUser(LoginBean user){
        Criteria c = Criteria.where("nombreUsuario").is(user.getNombreUsuario());
        Query query = new Query(c);
        return mt.find(query, LoginBean.class).size() > 0;
    }
    
    public boolean credecialesOKUser(LoginBean user){
        Criteria c = Criteria.where("nombreUsuario").is(user.getNombreUsuario()).and("pwd").is(user.getPwd());
        Query query = new Query(c);
        return mt.find(query, LoginBean.class).size() > 0;
    }
    
}
