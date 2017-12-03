package com.dean.j2ee.framework.db;

import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dean on 2017/1/27.
 */
public class ConvenientDao {

    /**
     * 保存数据
     *
     * @param sessionFactory
     * @param object
     */
    public synchronized static void saveOrUpdate(SessionFactory sessionFactory, Object object) {
        ConvenientDBUtil.saveOrUpdate(sessionFactory, object);
    }

    /**
     * 查找一条满足条件的数据
     *
     * @param sessionFactory
     * @param ormClass
     * @param params
     * @param <T>
     * @return
     */
    public synchronized <T> T find(SessionFactory sessionFactory, Class<? extends T> ormClass, Map<String, Object> params) {
        return ConvenientDBUtil.find(sessionFactory, ormClass, params);
    }

    /**
     * 查找全部满足条件的数据
     *
     * @param sessionFactory
     * @param ormClass
     * @param params
     * @param <T>
     * @return
     */
    public synchronized <T> List<T> findAll(SessionFactory sessionFactory, Class<? extends T> ormClass, Map<String, Object> params) {
        return ConvenientDBUtil.findAll(sessionFactory, ormClass, params);
    }

    /**
     * 删除一条数据
     *
     * @param sessionFactory
     * @param object
     */
    public synchronized void delete(SessionFactory sessionFactory, Object object) {
        ConvenientDBUtil.delete(sessionFactory, object);
    }

    /**
     * 删除满足指定条件的数据
     *
     * @param sessionFactory
     * @param ormClass
     * @param where
     * @param <T>
     */
    public synchronized <T> void delete(SessionFactory sessionFactory, Class<? extends T> ormClass, Map<String, Object> where) {
        ConvenientDBUtil.delete(sessionFactory, ormClass, where);
    }

    /**
     * 获取填充参数的Map集合
     *
     * @return
     */
    public static Map<String, Object> getParamMap() {
        return new HashMap<>();
    }

}
