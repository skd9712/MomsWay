package com.momsway.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDSLConf {
    private EntityManager em;
    @Autowired
    public QueryDSLConf(EntityManager em){
        this.em=em;
    }
    @Bean
    public JPAQueryFactory queryFactory(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory;
    }
}
