package com.dean.j2ee.framework.service;

import org.json.JSONObject;

/**
 * Created by dean on 2017/1/27.
 */
public class ConvenientService {

    // 应答"成功"
    protected static final String RESPONSE_SUCCESS = "200";
    // 应答"参数错误"
    protected static final String RESPONSE_PARAMETER_ERROR = "404";
    // 应答"token失效"
    protected static final String RESPONSE_TOKEN_LOSE_EFFICACY = "9004";
    // 应答"未知错误"
    protected static final String RESPONSE_UN_KNOW_ERROR = "501";
    // 应答"用户个人限制访问"
    protected static final String RESPONSE_RESTRICT_ACCESS_BY_USER = "700";

    /**
     * 获取 应答的JSONObject
     *
     * @param code
     * @return
     */
    protected JSONObject getResponseJSON(String code) {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("code", code);
        responseJSON.put("message", getResponseMessage(code));

        return responseJSON;
    }

    /**
     * 获取应答信息
     *
     * @param code
     * @return
     */
    private String getResponseMessage(String code) {
//        if (RESPONSE_SUCCESS.equals(code))
//            return EncodingUtils.encodeStr("成功");
//        else if (RESPONSE_PARAMETER_ERROR.equals(code))
//            return EncodingUtils.encodeStr("参数错误");
//        else if (RESPONSE_TOKEN_LOSE_EFFICACY.equals(code))
//            return EncodingUtils.encodeStr("token失效");
//        else if (RESPONSE_UN_KNOW_ERROR.equals(code))
//            return EncodingUtils.encodeStr("未知错误");
//        else
        return "";
    }

}
