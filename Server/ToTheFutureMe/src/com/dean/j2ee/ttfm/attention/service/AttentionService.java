package com.dean.j2ee.ttfm.attention.service;

import com.dean.j2ee.framework.json.JSONUtil;
import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.ttfm.attention.bean.FriendEntity;
import com.dean.j2ee.ttfm.attention.db.AttentionDao;
import com.dean.j2ee.ttfm.token.bean.TokenEntity;
import com.dean.j2ee.ttfm.token.db.TokenDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Service
public class AttentionService extends ConvenientService {

    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private AttentionDao attentionDao;

    /**
     * 获取指定用户的全部好友
     *
     * @param token
     * @param body
     * @return
     */
    public Object loadAttention(String token, String body) {
        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        List<FriendEntity> friendEntities = null;

        JSONObject request = new JSONObject(body);
        try {
            String username = URLDecoder.decode(request.getString("username"), "utf-8");
            friendEntities = attentionDao.findAll(username);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject response = getResponseJSON(RESPONSE_SUCCESS);
        response.put("data", JSONUtil.list2JSONArray(friendEntities));

        return response.toString();
    }

    /**
     * 通过指定用户名搜索用户
     *
     * @param token
     * @param body
     * @return
     */
    public Object searchAuthByUsername(String token, String body) {
        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        FriendEntity friendEntity = null;

        JSONObject request = new JSONObject(body);
        try {
            String username = URLDecoder.decode(request.getString("username"), "utf-8");
            friendEntity = attentionDao.findByUsername(username);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject response = getResponseJSON(RESPONSE_SUCCESS);
        response.put("hasData", friendEntity != null);
        if (friendEntity != null)
            response.put("data", JSONUtil.object2Json(friendEntity));

        return response.toString();
    }

    /**
     * 添加好友关系
     *
     * @param token
     * @param body
     * @return
     */
    public Object addAttention(String token, String body) {
        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        JSONObject request = new JSONObject(body);
        FriendEntity friendEntity = new FriendEntity();
        JSONUtil.json2Object(request, friendEntity);

        attentionDao.saveOrUpdate(friendEntity);

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

}
