package com.dean.j2ee.ttfm.auth.db;

import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.ttfm.auth.bean.AuthEntity;
import com.dean.j2ee.ttfm.auth.bean.VerificationCodeEntity;
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
     * 查找指定username的验证码信息
     *
     * @param username
     * @return
     */
    public VerificationCodeEntity findVerificationCodeEntity(String username) {
        Map<String, Object> params = getParamMap();
        params.put("username", username);

        return super.find(sessionFactory, VerificationCodeEntity.class, params);
    }

    /**
     * 保存／更新 验证码信息
     *
     * @param verificationCodeEntity
     */
    public void saveOrUpdate(VerificationCodeEntity verificationCodeEntity) {
        saveOrUpdate(sessionFactory, verificationCodeEntity);
    }

    /**
     * 保存/更新 用户信息
     *
     * @param authEntity
     */
    public void saveOrUpdate(AuthEntity authEntity) {
        saveOrUpdate(authEntity);
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
