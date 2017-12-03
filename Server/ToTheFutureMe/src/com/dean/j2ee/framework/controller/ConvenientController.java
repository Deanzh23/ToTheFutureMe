package com.dean.j2ee.framework.controller;

import org.json.JSONObject;

/**
 * Created by Dean on 2016/11/29.
 */
public class ConvenientController {

    // 应答"成功"
    protected static final String RESPONSE_SUCCESS = "200";

    // 应答"参数错误"
    protected static final String RESPONSE_PARAMETER_ERROR = "404";
    // 应答"token失效"
    protected static final String RESPONSE_TOKEN_LOSE_EFFICACY = "900";
    // 应答"未知错误"
    protected static final String RESPONSE_UN_KNOW_ERROR = "501";
    // 应答"用户个人限制访问"
    protected static final String RESPONSE_RESTRICT_ACCESS_BY_USER = "10700";

    /**
     * 获取 应答的JSONObject
     *
     * @param code
     * @return
     */
    protected JSONObject getResponseJSON(String code) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("code", code);

        return responseJSON;
    }

}
