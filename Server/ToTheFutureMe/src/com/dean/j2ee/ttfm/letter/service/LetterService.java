package com.dean.j2ee.ttfm.letter.service;

import com.dean.j2ee.framework.json.JSONUtil;
import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.framework.utils.TextUils;
import com.dean.j2ee.ttfm.auth.bean.AuthEntity;
import com.dean.j2ee.ttfm.auth.db.AuthDao;
import com.dean.j2ee.ttfm.letter.bean.LetterEntity;
import com.dean.j2ee.ttfm.letter.db.LetterDao;
import com.dean.j2ee.ttfm.token.bean.TokenEntity;
import com.dean.j2ee.ttfm.token.db.TokenDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 信件服务
 */
@Service
public class LetterService extends ConvenientService {

    @Autowired
    private LetterDao letterDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private TokenDao tokenDao;

    /**
     * 上传信件
     *
     * @param token
     * @param body
     * @return
     */
    public Object upload(String token, String body) {
        // 参数错误
        if (TextUils.isEmpty(body))
            return getResponseJSON(RESPONSE_PARAMETER_ERROR).toString();

        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        // 构造信件实例
        LetterEntity letterEntity = new LetterEntity();
        JSONUtil.json2Object(new JSONObject(body), letterEntity);
        letterEntity.setLetterId(UUID.randomUUID().toString());

        // 保存到数据库
        letterDao.saveOrUpdate(letterEntity);

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

    /**
     * 读取信件
     *
     * @param token
     * @param username   收件人用户名
     * @param startIndex 起始下标
     * @param count      信件数量
     * @return
     */
    public Object loadLetters(String token, String username, int startIndex, int count) {
        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        List<LetterEntity> letterEntities = letterDao.findAllLetterByUsername(username, startIndex, count);

        // 拼入用户信息
        if (letterEntities != null && letterEntities.size() > 0) {
            for (LetterEntity letterEntity : letterEntities) {
                AuthEntity authEntity = authDao.find(letterEntity.getSenderUserId());

                if (authEntity != null) {
                    letterEntity.setSenderUserNickName(authEntity.getNickname());
                    letterEntity.setSenderAvatarUrl(authEntity.getAvatarUrl());
                }
            }
        }

        JSONObject response = getResponseJSON(RESPONSE_SUCCESS);
        response.put("data", JSONUtil.list2JSONArray(letterEntities));

        return response.toString();
    }

    /**
     * 设置指定信件已读
     *
     * @param token
     * @param letterId
     * @return
     */
    public Object readLetter(String token, String letterId) {
        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        LetterEntity letterEntity = letterDao.findByLetterId(letterId);
        letterEntity.setIsRead(1);
        letterDao.saveOrUpdate(letterEntity);

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

}
