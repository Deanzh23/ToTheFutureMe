package com.dean.j2ee.ttfm.token.db;

import com.dean.j2ee.framework.db.ConvenientDBUtil;
import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.framework.utils.DateTimeUtils;
import com.dean.j2ee.ttfm.token.bean.TokenEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
            long tokenTime = tokenEntity.getTime();
            Date tokenDate = new Date(tokenTime);
            Date nowDate = new Date();

            // TODO 为了调试代码，暂时设置相差1天即为token失效
            if (DateTimeUtils.differentDaysByMillisecond(tokenDate, nowDate) > 1) {
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
        tokenEntity.setTime(new Date().getTime());
        saveOrUpdate(sessionFactory, tokenEntity);
    }

    /**
     * 保存token
     *
     * @param userId
     */
    public TokenEntity saveToken(String userId) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(userId);
        tokenEntity.setToken(UUID.randomUUID().toString());
        tokenEntity.setTime(new Date().getTime());

        saveOrUpdate(sessionFactory, tokenEntity);

        return tokenEntity;
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
