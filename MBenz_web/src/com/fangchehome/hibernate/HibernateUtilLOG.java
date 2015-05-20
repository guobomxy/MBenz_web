package com.fangchehome.hibernate;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;

import com.fangchehome.exception.StatException;
import com.fangchehome.util.Page;


public class HibernateUtilLOG {
	private static final SessionFactory sessionFactory;

	static {
		try {
			sessionFactory = new Configuration().configure("hibernate.cfg.log.xml").buildSessionFactory();

		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getSession() {

		return sessionFactory.getCurrentSession();
	}
	
	public static Session openSession() {

		return sessionFactory.openSession();
	}

	/**
	 * 指定Key-Value,先组装好HashMap键值传入，防止SQL注入
	 * @param sql
	 * @param where
	 * @return
	 */
	public static List getSQL2MapList(String sql, HashMap where) {
		Session session = null;
		List list = null;
		try {
			session = HibernateUtilLOG.getSession();
			session.beginTransaction();
			SQLQuery q = session.createSQLQuery(sql);
			if (where != null) {
				Iterator it = where.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					q.setParameter(key, where.get(key));
				}
			}
			q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);			
			list = q.list();
			session.getTransaction().commit();
		} catch (JDBCException ex) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			throw new StatException(ex.getSQLException());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	/**
	 * 不指定Key-Value,首页
	 * @param sql
	 * @return
	 */
	public static List getSQL2MapList(String sql) {
		Session session = null;
		List list = null;
		try {
			session = HibernateUtilLOG.getSession();
			session.beginTransaction();
			SQLQuery q = session.createSQLQuery(sql);			
			q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = q.list();
			session.getTransaction().commit();
		} catch (JDBCException ex) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			throw new StatException(ex.getSQLException());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	/**
	 * 不指定Key-Value，分页
	 * @param sql
	 * @param pageindex
	 * @param pagesize
	 * @return
	 */
	public static Page getSQL2MapPage(String sql, int pageindex, int pagesize) {
		return getSQL2MapPage(sql, null, pageindex, pagesize);
	}

	/**
	 * 数据库查询SQL-Hibernate
	 * @param sql
	 * @param where
	 * @param pageindex
	 * @param pagesize
	 * @return
	 */
	public static Page getSQL2MapPage(String sql, HashMap where, int pageindex,	int pagesize) {
		Session session = null;
		List list = null;
		int rowcount = 0;
		try {
			session = HibernateUtilLOG.getSession();
			session.beginTransaction();
			SQLQuery q = session.createSQLQuery(sql);
			if (where != null) {
				Iterator it = where.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					q.setParameter(key, where.get(key));
				}
			}
			q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
			rowcount = q.list().size();
			if (pagesize > 0) {
				q.setFirstResult((pageindex - 1) * pagesize);
				q.setMaxResults(pagesize);
			}
			list = q.list();
			session.getTransaction().commit();
		} catch (JDBCException ex) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			throw new StatException(ex.getSQLException());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return new Page(list, pageindex, pagesize, rowcount);
	}
	
	/**
	 * 不指定Key-Value
	 * @param sql
	 * @return
	 */
	public static int sqlupdate(String sql) {
		return sqlupdate(sql,null);
	}
	
	/**
	 * 数据库添加修改删除等更新操作SQL-Hibernate
	 * @param sql
	 * @param where
	 * @return
	 */
	public static int sqlupdate(String sql, HashMap where) {
		int flag = -1;
		Session session = null;
		try {
			session = HibernateUtilLOG.getSession();
			session.beginTransaction();
			Query q = session.createSQLQuery(sql);
			if (where != null) {
				Iterator it = where.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					q.setParameter(key, where.get(key));
				}
			}
			flag = q.executeUpdate();
			session.getTransaction().commit();
		} catch (JDBCException ex) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			throw new StatException(ex.getSQLException());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return flag;
	}	
}