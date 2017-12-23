package com.dean.j2ee.ttfm.auth.db;

import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.framework.utils.TextUils;
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
     * 删除指定用户的注册验证码
     *
     * @param username
     */
    public void deleteVerificationCodeEntityByUsername(String username) {
        Map<String, Object> params = getParamMap();
        params.put("username", username);

        super.delete(sessionFactory, VerificationCodeEntity.class, params);
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
        saveOrUpdate(sessionFactory, authEntity);
    }

    /**
     * 查找一个指定用户名、密码的账号
     *
     * @param params
     * @return
     */
    public AuthEntity find(String... params) {
        String username = null;
        String password = null;
        try {
            username = params[0];
            password = params[2];
        } catch (Exception e) {
        }

        Map<String, Object> dbParams = getParamMap();
        dbParams.put("username", username);
        if (!TextUils.isEmpty(password))
            dbParams.put("password", password);

        AuthEntity authEntity = find(sessionFactory, AuthEntity.class, dbParams);

        return authEntity;
    }

}
