package com.dean.j2ee.ttfm.token.db;

import com.dean.j2ee.framework.db.ConvenientDBUtil;
import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.ttfm.token.bean.TokenEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Token DB
 * <p>
 * Created by Dean on 2016/11/30.
 */
@Repository
public class TokenDao extends ConvenientDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 检查token时间
     */
    public TokenEntity checkToken(String token) {
        TokenEntity tokenEntity = findByToken(token);

        // token不存在（需要重新登陆）
        if (tokenEntity == null)
            return null;
        else {
            Timestamp timestamp = tokenEntity.getTime();
            long tokenTime = timestamp.getTime();
            Date date = new Date();
            long nowTime = date.getTime();

            if (nowTime - tokenTime > 1000 * 1000 * 60 * 10) {
                // token失效（需要重新登陆）
                return null;
            } else {
                refreshTime(tokenEntity);
                return tokenEntity;
            }
        }
    }

    public void saveOrUpdate(TokenEntity tokenEntity) {
        saveOrUpdate(sessionFactory, tokenEntity);
    }

    /**
     * 查询token
     *
     * @param userId
     * @return
     */
    public TokenEntity find(String userId) {
        Map<String, Object> params = getParamMap();
        params.put("userId", userId);
        TokenEntity tokenEntity = super.find(sessionFactory, TokenEntity.class, params);

        return tokenEntity;
    }

    /**
     * 通过token查询Token实例
     *
     * @param token
     * @return
     */
    public TokenEntity findByToken(String token) {
        Map<String, Object> params = getParamMap();
        params.put("token", token);
        TokenEntity tokenEntity = ConvenientDBUtil.find(sessionFactory, TokenEntity.class, params);

        return tokenEntity;
    }

    /**
     * 刷新token时间
     *
     * @param tokenEntity
     */
    public void refreshTime(TokenEntity tokenEntity) {
        tokenEntity.setTime(new Timestamp(new Date().getTime()));
        saveOrUpdate(sessionFactory, tokenEntity);
    }

    /**
     * 保存token
     *
     * @param userId
     */
    protected void saveToken(String userId) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(UUID.randomUUID().toString());
        tokenEntity.setTime(new Timestamp(new Date().getTime()));

        saveOrUpdate(sessionFactory, tokenEntity);
    }

    /**
     * 删除token
     *
     * @param userId
     */
    protected void deleteToken(String userId) {
        Map<String, Object> params = getParamMap();
        params.put("userId", userId);

        super.delete(sessionFactory, TokenEntity.class, params);
    }

}
