package com.dean.j2ee.ttfm.letter.service;

import com.dean.j2ee.framework.json.JSONUtil;
import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.framework.utils.TextUils;
import com.dean.j2ee.ttfm.letter.bean.LetterEntity;
import com.dean.j2ee.ttfm.letter.db.LetterDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 信件服务
 */
@Service
public class LetterService extends ConvenientService {

    @Autowired
    private LetterDao letterDao;

    /**
     * 上传信件
     *
     * @param body
     * @return
     */
    public Object upload(String body) {
        // 参数错误
        if (TextUils.isEmpty(body))
            return getResponseJSON(RESPONSE_PARAMETER_ERROR).toString();

        // 构造信件实例
        LetterEntity letterEntity = new LetterEntity();
        JSONUtil.json2Object(new JSONObject(body), letterEntity);
        // 保存到数据库
        letterDao.saveOrUpdate(letterEntity);

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

}
