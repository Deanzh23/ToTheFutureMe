package com.dean.j2ee.framework.db;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

/**
 * 数据库操作 工具类
 * <p>
 * Created by Dean on 2016/11/30.
 */
public class ConvenientDBUtil {

    /**
     * 保存数据
     *
     * @param sessionFactory
     * @param object
     */
    public synchronized static void saveOrUpdate(SessionFactory sessionFactory, Object object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.saveOrUpdate(object);

        transaction.commit();
        session.close();
    }

    /**
     * 查找一个(符合指定条件的)
     *
     * @param sessionFactory
     * @param ormClass
     * @param wheres
     * @param <T>
     * @return
     */
    public synchronized static <T> T find(SessionFactory sessionFactory, Class<? extends T> ormClass, Map<String, Object> wheres) {
        Session session = sessionFactory.openSession();

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ormClass);

        if (wheres != null && wheres.size() > 0)
            for (Map.Entry<String, Object> entry : wheres.entrySet())
                detachedCriteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));

        Criteria cri = detachedCriteria.getExecutableCriteria(session);
        List<T> objects = cri.list();

        session.close();

        return (objects == null || objects.size() <= 0) ? null : objects.get(0);
    }

    /**
     * 查找全部(符合指定条件的)
     *
     * @param sessionFactory
     * @param ormClass
     * @param wheres
     * @param <T>
     * @return
     */
    public synchronized static <T> List<T> findAll(SessionFactory sessionFactory, Class<? extends T> ormClass, Map<String, Object> wheres) {
        Session session = sessionFactory.openSession();

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ormClass);

        if (wheres != null && wheres.size() > 0)
            for (Map.Entry<String, Object> entry : wheres.entrySet())
                detachedCriteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));

        Criteria cri = detachedCriteria.getExecutableCriteria(session);
        List<T> objects = cri.list();

        session.close();

        return objects;
    }

    /**
     * 删除一条数据
     *
     * @param sessionFactory
     * @param object
     */
    public synchronized static void delete(SessionFactory sessionFactory, Object object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.delete(object);

        transaction.commit();
        session.close();
    }

    /**
     * 删除满足指定条件的数据
     *
     * @param sessionFactory
     * @param ormClass
     * @param where
     * @param <T>
     */
    public synchronized static <T> void delete(SessionFactory sessionFactory, Class<? extends T> ormClass, Map<String, Object> where) {
        List<T> objects = findAll(sessionFactory, ormClass, where);

        if (objects != null && objects.size() > 0) {

            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            for (T object : objects)
                session.delete(object);

            transaction.commit();
            session.close();
        }
    }

}
