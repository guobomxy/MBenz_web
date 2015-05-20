package com.fangchehome.util;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQLServerDialect extends MySQL5InnoDBDialect{
	public MySQLServerDialect()   
    {   
        super();   
        //very important, mapping char(n) to String   
        registerHibernateType(Types.CHAR, Hibernate.STRING.getName());   
    }   

}
