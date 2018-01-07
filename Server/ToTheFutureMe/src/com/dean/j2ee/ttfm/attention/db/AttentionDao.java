package com.dean.j2ee.ttfm.attention.db;

import com.dean.j2ee.framework.db.ConvenientDao;
import com.dean.j2ee.ttfm.auth.bean.AuthEntity;
import com.dean.j2ee.ttfm.attention.bean.FriendEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AttentionDao extends ConvenientDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 查找指定用户的全部好友
     *
     * @param username
     * @return
     */
    public List<FriendEntity> findAll(String username) {
        Map<String, Object> params = getParamMap();
        params.put("whoFriend", username);

        return findAll(sessionFactory, FriendEntity.class, params);
    }

    /**
     * 通过指定用户名搜索指定好友
     *
     * @param username
     * @return
     */
    public FriendEntity findByUsername(String username) {
        Map<String, Object> params = getParamMap();
        params.put("username", username);

        FriendEntity friendEntity = null;

        AuthEntity authEntity = find(sessionFactory, AuthEntity.class, params);
        if (authEntity != null) {
            friendEntity = new FriendEntity();
            friendEntity.setUsername(authEntity.getUsername());
            friendEntity.setNickname(authEntity.getNickname());
            friendEntity.setAvatarUrl(authEntity.getAvatarUrl());
        }

        return friendEntity;
    }

    /**
     * 保存好友关系
     *
     * @param friendEntity
     */
    public void saveOrUpdate(FriendEntity friendEntity) {
        saveOrUpdate(sessionFactory, friendEntity);
    }

}
