package com.dean.j2ee.ttfm.auth.db;

import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.ttfm.auth.bean.AuthEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 账号DB
 */
@Repository
public class AuthDao extends ConvenientDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 检查用户名是否可用
     *
     * @param username
     * @return
     */
    public boolean isEmpty(String username) {
        Map<String, Object> params = getParamMap();
        params.put("username", username);

        AuthEntity authEntity = find(sessionFactory, AuthEntity.class, params);

        return authEntity == null;
    }

    /**
     * 查找一个指定用户名、密码的账号
     *
     * @param username
     * @param password
     * @return
     */
    public AuthEntity find(String username, String password) {
        Map<String, Object> params = getParamMap();
        params.put("username", username);
        params.put("password", password);

        AuthEntity authEntity = find(sessionFactory, AuthEntity.class, params);

        return authEntity;
    }

}
