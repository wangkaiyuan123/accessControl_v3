package com.dhht.dao;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dhht.common.DaoUtil;
import com.dhht.common.MapUtil;


/**
 * @author zhaoxl
 *
 * @param <T>
 */
/**
 * @author zhaoxl
 * 
 * @param <T>
 */
@Repository
public class BaseDao<T> {
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获取当前session
     * 
     * @return
     */
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存一个对象
     * 
     * @param o
     */
    public void save(T o) {
        getCurrentSession().save(o);
    }

    /**
     * 删除一个对象
     * 
     * @param o
     */
    public void delete(T o) {
        this.getCurrentSession().delete(o);
    }

    /**
     * 删除一个对象
     * 
     * @param c
     * @param id
     */
    public void delete(Class<T> c, String id) {
        delete(get(c, id));
    }

    /**
     * 更新一个对象
     * 
     * @param o
     */
    public void update(T o) {
        this.getCurrentSession().update(o);
    }

    /**
     * 保存或更新一个对象
     * 
     * @param o
     */
    public void saveOrUpdate(T o) {
        this.getCurrentSession().saveOrUpdate(o);
    }

    /**
     * 按hql查询
     * 
     * @param hql
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> find(String hql) {
        return createQuery(hql).list();
    }

    /**
     * 按hql分页查询
     * 
     * @param hql
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, Integer page, Integer rows) {
        Query q = createQuery(hql);
        setPageInfo(q, page, rows);
        return q.list();
    }

    /**
     * 按hql带参数查询
     * 
     * @param hql
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, Map<String, Object> param) {
        return createQueryAndSetParam(hql, param).list();
    }

    /**
     * 按hql带参数分页查询
     * 
     * @param hql
     * @param param
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, Map<String, Object> param, Integer page,
                    Integer rows) {
        Query q = createQueryAndSetParam(hql, param);
        setPageInfo(q, page, rows);
        return q.list();
    }

    /**
     * 获取一个对象
     * 
     * @param c
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get(Class<T> c, String id) {
        return (T) this.getCurrentSession().get(c, id);
    }
    
    /**
     * 通过ids指定的id集合获取对象集
     * @param c
     * @param ids
     * @return
     */
    public Set<T> findByIds(Class<T> c, Set<String> ids) {
        Set<T> set = new HashSet<T>();
        if(ids != null && ids.size() > 0) {
            String hql = DaoUtil.getFindPrefix(c) + " where id in (" + DaoUtil.generateInStr(ids) + ")";
            set.addAll(find(hql));
        }
        return set;
    }

    /**
     * 按hql获取一个对象
     * 
     * @param hql
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get(String hql) {
        return (T) createQuery(hql).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public T getBySql(String sql, Class<T> c) {
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.addEntity(c);
        return (T) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
	public T getBySql(String sql, Class<T> c,Map<String,Object> params) {
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        setQueryParameter(query, params);
        query.addEntity(c);
        return (T) query.uniqueResult();
    }
    
    /**
     * 原生sql查询，返回List集合
     * @param sql
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findBySql(String sql, Class<T> c) {
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.addEntity(c);
        return (List<T>) query.list();
    }
    
    /**
     * 原生sql查询，返回List集合
     * @param sql
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findBySql(String sql,Map<String,Object> params, Class<T> c) {
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        setQueryParameter(query, params);
        query.addEntity(c);
        return (List<T>) query.list();
    }
    
    /**
     * 原生sql 带分页查询,参数化注值
     * @param sql
     * @param c
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findBySql(String sql,Map<String,Object> params, Class<T> c,  Integer page,
            Integer rows) {
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        setQueryParameter(query, params);
        query.addEntity(c);
        setPageInfo(query, page, rows);
        return (List<T>) query.list();
    }
    /**
     * 原生sql 带分页查询
     * @param sql
     * @param c
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findBySql(String sql, Class<T> c,  Integer page,
            Integer rows) {
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.addEntity(c);
        setPageInfo(query, page, rows);
        return (List<T>) query.list();
    }
    
    /**
     * 按hql及查询参数获取一个对象
     * 
     * @param hql
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get(String hql, Map<String, Object> param) {
        return (T) createQueryAndSetParam(hql, param).uniqueResult();
    }

    /**
     * 按hql统计记录数
     * 
     * @param hql
     * @return
     */
    public Long count(String hql) {
        return (Long) createQuery(hql).uniqueResult();
    }

    /**
     * 按hql及查询参数统计记录数
     * 
     * @param hql
     * @param param
     * @return
     */
    public Long count(String hql, Map<String, Object> param) {
        return (Long) createQueryAndSetParam(hql, param).uniqueResult();
    }

    /**
     * 执行hql
     * 
     * @param hql
     * @return
     */
    public Integer executeHql(String hql) {
        return createQuery(hql).executeUpdate();
    }

    /**
     * 执行带参数的hql
     * 
     * @param hql
     * @param param
     * @return
     */
    public Integer executeHql(String hql, Map<String, Object> param) {
        return createQueryAndSetParam(hql, param).executeUpdate();
    }

    /**
     * 创建Query对象
     * 
     * @param hql
     * @return 根据hql创建的query对象
     */
    private Query createQuery(String hql) {
        return this.getCurrentSession().createQuery(hql);
    }

    /**
     * 给Query对象设置查询参数
     * 
     * @param q
     * @param param
     */
    @SuppressWarnings("rawtypes")
	private void setQueryParameter(Query q, Map<String, Object> param) {
        if(MapUtil.isEmpty(param)) {
            return;
        }
        for (String key : param.keySet()) {
        	Object object = param.get(key);
        	if(object instanceof Collection){
        		q.setParameterList(key, (Collection)object);
        	}else{
        		q.setParameter(key, param.get(key));
        	}
        }
    }

    /**
     * 创建Query对象并设置查询参数
     * 
     * @param hql
     * @param param
     * @return 设置好查询参数的Query对象
     */
    private Query createQueryAndSetParam(String hql, Map<String, Object> param) {
        Query q = createQuery(hql);
        setQueryParameter(q, param);
        return q;
    }

    /**
     * 设置分页参数
     * 
     * @param q
     * @param page
     * @param rows
     */
    public void setPageInfo(Query q, Integer page, Integer rows) {
        if(page == null || page < 1) {
            page = 1;
        }
        if(rows == null || rows < 1) {
            rows = 20;
        }
        q.setFirstResult((page - 1) * rows).setMaxResults(rows);
    }
    
    
    /**
     * 原生SQL查询
     * 
     * @param hql
     * @return 根据hql创建的query对象
     */
//    @SuppressWarnings("unchecked")
//    public List<Object[]> createSQLQuery(String sql,int page,int rows,Object o1,Object o2){
//        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
//        query.addEntity(o1.getClass());
//        query.addEntity(o2.getClass());
//        setPageInfo(query, page, rows);
//        return query.list();
//    }
    
    /**
     * 原生SQL查询
     * 
     * @param hql
     * @return 根据hql创建的query对象
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> createSQLQuery(String sql, Map<String, Object> params, int page, int rows, Object... args){
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        setQueryParameter(query, params);
        for (int i = 0; i < args.length; i++) {
            query.addEntity(args[i].getClass());
        }
        setPageInfo(query, page, rows);
        return query.list();
    }
    
    public Integer executeSql(String sql, Map<String, Object> params){
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }
    /**
     * 原生SQL查询
     * 
     * @param hql
     * @return 根据hql创建的query对象
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> createSQLQuery(String sql,int page,int rows,Object... args){
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        for (int i = 0; i < args.length; i++) {
            query.addEntity(args[i].getClass());
        }
        setPageInfo(query, page, rows);
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> createSQLQuery(String sql, Map<String, Object> params, Object... args){
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        setQueryParameter(query, params);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                query.addEntity(args[i].getClass());
            }
        }
        return query.list();
    }
    
    /**
     * 按sql统计记录数
     * 
     * @param sql
     * @return
     */
    public Integer countSQLQuery(String sql) {
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        query.setMaxResults(1);
        BigInteger count = (BigInteger)query.uniqueResult();
        return count.intValue();
    }
    
    /**
     * 按sql统计记录数
     * 
     * @param sql
     * @return
     */
    public Integer countSQLQuery(String sql, Map<String, Object> params) {
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        setQueryParameter(query, params);
        query.setMaxResults(1);
        BigInteger count = (BigInteger)query.uniqueResult();
        return count.intValue();
    }
    
	/**
	 * 用SQL文批量删除数据集
	 * 
	 * @param hql
	 *            删除sql语句
	 */
	public void deleteBySql(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	/**
	 * 用sql文批量更新
	 */
	public void updateBySql(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
}