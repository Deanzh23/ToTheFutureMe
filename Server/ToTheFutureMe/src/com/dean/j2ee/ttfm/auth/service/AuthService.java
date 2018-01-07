package com.dean.j2ee.ttfm.auth.service;

import com.dean.j2ee.framework.json.JSONUtil;
import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.framework.utils.TextUils;
import com.dean.j2ee.framework.utils.email.EMailUtils;
import com.dean.j2ee.ttfm.auth.bean.AuthEntity;
import com.dean.j2ee.ttfm.auth.bean.VerificationCodeEntity;
import com.dean.j2ee.ttfm.auth.db.AuthDao;
import com.dean.j2ee.ttfm.token.bean.TokenEntity;
import com.dean.j2ee.ttfm.token.db.TokenDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 账号Service
 */
@Service
public class AuthService extends ConvenientService {

    // 检查账号可用性-->账号已被使用
    private static final String REGISTER_USERNAME_FAILURE_EXIST = "400";
    // 登陆失败-->账号或密码错误
    private static final String LOGIN_FAILURE_NOT_CONFORM = "400";
    // 登陆失败-->账号不存在
    private static final String LOGIN_FAILURE_ACCOUNT_DOES_NOT_EXIST = "401";
    // 登陆失败-->验证码失效
    private static final String LOGIN_FAILURE_VERIFICATION_CODE_TIMEOUT = "402";

    @Autowired
    private AuthDao authDao;
    @Autowired
    private TokenDao tokenDao;

    /**
     * 检查用户名是否可用
     *
     * @param body
     * @return
     */
    public Object checkUsername(String body) {
        JSONObject request = new JSONObject(body);

        System.out.println("[checkUsername] -> " + request.toString());

        try {
            String username = URLDecoder.decode(request.getString("username"), "utf-8");

            boolean isEmpty = authDao.isEmpty(username);
            if (isEmpty)
                // 邮箱可以使用
                return sendVerificationCode(username);
            else
                // 邮箱已被占用
                return getResponseJSON(REGISTER_USERNAME_FAILURE_EXIST).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return getResponseJSON(REGISTER_USERNAME_FAILURE_EXIST).toString();
        }
    }

    /**
     * 重新发送验证码
     *
     * @param body
     * @return
     */
    public Object sendVerificationCodeAgain(String body) {
        JSONObject request = new JSONObject(body);

        try {
            String username = URLDecoder.decode(request.getString("username"), "utf-8");

            return sendVerificationCode(username);
        } catch (Exception e) {
            e.printStackTrace();
            return getResponseJSON(RESPONSE_PARAMETER_ERROR).toString();
        }
    }

    /**
     * 发送验证码
     *
     * @param username
     * @return
     */
    private Object sendVerificationCode(String username) {
        // 查找此用户的验证码
        VerificationCodeEntity verificationCodeEntity;
        // 获取6位数字验证码
        String verificationCode = EMailUtils.getVerificationCode();
        // 发送验证码到指定邮箱
//        try {
//            EMailUtils.sendEMail(Config.APP_NAME, username, Config.APP_EMAIL, Config.APP_EMAIL_PASSWORD, "您本次的注册验证码为：" + verificationCode);

            // 这里需要将验证码跟username关联，并存储到临时表里，注册后将其从临时表中删除
            verificationCodeEntity = new VerificationCodeEntity();
            verificationCodeEntity.setUsername(username);
            verificationCodeEntity.setVerificationCode(verificationCode);
            verificationCodeEntity.setTime(System.currentTimeMillis());

            authDao.saveOrUpdate(verificationCodeEntity);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            return getResponseJSON(REGISTER_USERNAME_FAILURE_EXIST).toString();
//        }

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

    /**
     * 用户注册
     *
     * @param body
     * @return
     */
    public Object register(String body) {
        JSONObject request = new JSONObject(body);
        AuthEntity requestAuthEntity = new AuthEntity();
        JSONUtil.json2Object(request, requestAuthEntity);

        // 查找此用户的验证码
        VerificationCodeEntity verificationCodeEntity = authDao.findVerificationCodeEntity(requestAuthEntity.getUsername());
        if (verificationCodeEntity == null || EMailUtils.getDiffer(verificationCodeEntity.getTime(), System.currentTimeMillis()) > 60 * 30)
            return getResponseJSON(LOGIN_FAILURE_VERIFICATION_CODE_TIMEOUT).toString();

        // 保存用户信息->db
        authDao.saveOrUpdate(requestAuthEntity);

        // 删除用户注册验证码
        authDao.deleteVerificationCodeEntityByUsername(requestAuthEntity.getUsername());

        // 应答
        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @return
     */

    public Object login(String username, String password) {
        System.out.println("[login] -> " + username + " " + password);

        // 检查用户名密码是否为空
        if (TextUils.isEmpty(username) || TextUils.isEmpty(password))
            return getResponseJSON(RESPONSE_PARAMETER_ERROR).toString();

        // 检查账号是否存在
        boolean isEmpty = authDao.isEmpty(username);
        if (isEmpty)
            return getResponseJSON(LOGIN_FAILURE_ACCOUNT_DOES_NOT_EXIST).toString();

        // 检查账号密码
        AuthEntity authEntity = authDao.find(username, password);
        // 用户名或密码错误
        if (authEntity == null)
            return getResponseJSON(LOGIN_FAILURE_NOT_CONFORM).toString();

        // 设置并保存token
        TokenEntity tokenEntity = tokenDao.saveToken(authEntity.getUsername());
        authEntity.setToken(tokenEntity.getToken());
        authDao.saveOrUpdate(authEntity);

        // 登陆成功
        JSONObject response = getResponseJSON(RESPONSE_SUCCESS);
        response.put("data", JSONUtil.object2Json(authEntity));
        return response.toString();
    }

    /**
     * 更新用户信息
     *
     * @param token
     * @param body
     * @return
     */
    public Object upload(String token, String body) {
        if (TextUils.isEmpty(body))
            return getResponseJSON(RESPONSE_PARAMETER_ERROR).toString();

        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        JSONObject request = new JSONObject(body);
        AuthEntity requestAuthEntity = new AuthEntity();
        JSONUtil.json2Object(request, requestAuthEntity);

        // 检查账号密码
        AuthEntity authEntity = authDao.find(requestAuthEntity.getUsername(), requestAuthEntity.getPassword());
        // 用户名或密码错误
        if (authEntity == null)
            return getResponseJSON(LOGIN_FAILURE_NOT_CONFORM).toString();

        authEntity.setNickname(requestAuthEntity.getNickname());
        authEntity.setGenderCode(requestAuthEntity.getGenderCode());
        authEntity.setBirthday(requestAuthEntity.getBirthday());

        // 保存到数据库
        authDao.saveOrUpdate(authEntity);

        JSONObject response = getResponseJSON(RESPONSE_SUCCESS);
        response.put("data", JSONUtil.object2Json(authEntity));
        return response.toString();
    }

    /**
     * 修改密码
     *
     * @param token
     * @param body
     * @return
     */
    public Object editPassword(String token, String body) {
        if (TextUils.isEmpty(body))
            return getResponseJSON(RESPONSE_PARAMETER_ERROR).toString();

        // token失效
        TokenEntity tokenEntity = tokenDao.checkToken(token);
        if (tokenEntity == null)
            return getResponseJSON(RESPONSE_TOKEN_LOSE_EFFICACY).toString();

        JSONObject request = new JSONObject(body);
        String username = request.getString("username");
        String oldPassword = request.getString("oldPassword");
        String newPassword = request.getString("newPassword");

        // 检查账号密码
        AuthEntity authEntity = authDao.find(username, oldPassword);
        // 用户名或密码错误
        if (authEntity == null)
            return getResponseJSON(LOGIN_FAILURE_NOT_CONFORM).toString();

        // 保存到数据库
        authEntity.setPassword(newPassword);
        authDao.saveOrUpdate(authEntity);

        JSONObject response = getResponseJSON(RESPONSE_SUCCESS);
        response.put("data", newPassword);
        return response.toString();
    }

}
