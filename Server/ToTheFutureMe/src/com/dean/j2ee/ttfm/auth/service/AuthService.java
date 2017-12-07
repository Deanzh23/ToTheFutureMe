package com.dean.j2ee.ttfm.auth.service;

import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.framework.utils.TextUils;
import com.dean.j2ee.framework.utils.email.EMailUtils;
import com.dean.j2ee.ttfm.auth.bean.AuthEntity;
import com.dean.j2ee.ttfm.auth.bean.VerificationCodeEntity;
import com.dean.j2ee.ttfm.auth.db.AuthDao;
import com.dean.j2ee.ttfm.config.Config;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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

    /**
     * 检查用户名是否可用
     *
     * @param body
     * @return
     */
    public Object checkUsername(String body) {
        JSONObject request = new JSONObject(body);

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
     */
    public Object sendVerificationCodeAgain(String username) {
        return sendVerificationCode(username);
    }

    /**
     * 发送验证码
     *
     * @param username
     * @return
     */
    private Object sendVerificationCode(String username) {
        // 查找此用户的验证码
        VerificationCodeEntity verificationCodeEntity = authDao.findVerificationCodeEntity(username);
        if ((verificationCodeEntity != null && EMailUtils.getDiffer(verificationCodeEntity.getTime(), System.currentTimeMillis()) > 2 * 60) ||
                verificationCodeEntity == null) {
            // 获取6位数字验证码
            String verificationCode = EMailUtils.getVerificationCode();
            // 发送验证码到指定邮箱
            try {
                EMailUtils.sendEMail("给未来的自己", username, Config.APP_EMAIL, Config.APP_EMAIL_PASSWORD,
                        "您本次的注册验证码为：" + verificationCode);

                // 这里需要将验证码跟username关联，并存储到临时表里，注册后将其从临时表中删除
                verificationCodeEntity = new VerificationCodeEntity();
                verificationCodeEntity.setUsername(username);
                verificationCodeEntity.setVerificationCode(verificationCode);
                verificationCodeEntity.setTime(System.currentTimeMillis());

                authDao.saveOrUpdate(verificationCodeEntity);
            } catch (MessagingException e) {
                e.printStackTrace();
                return getResponseJSON(REGISTER_USERNAME_FAILURE_EXIST).toString();
            }
        }

        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param nickname
     * @param genderCode
     * @param birthday
     * @return
     */
    public Object register(String username, String password, String avatarUrl, String nickname, int genderCode, long birthday) {
        // 查找此用户的验证码
        VerificationCodeEntity verificationCodeEntity = authDao.findVerificationCodeEntity(username);
        if (verificationCodeEntity == null || EMailUtils.getDiffer(verificationCodeEntity.getTime(), System.currentTimeMillis()) > 60 * 30)
            return getResponseJSON(LOGIN_FAILURE_VERIFICATION_CODE_TIMEOUT).toString();

        // 保存用户信息->db
        AuthEntity authEntity = new AuthEntity();
        authEntity.setUsername(username);
        authEntity.setPassword(password);
        authEntity.setAvatarUrl(avatarUrl);
        authEntity.setNickname(nickname);
        authEntity.setGenderCode(genderCode);
        authEntity.setBirthday(birthday);
        authDao.saveOrUpdate(authEntity);

        JSONObject response = new JSONObject(RESPONSE_SUCCESS);
        return response.toString();
    }

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @return
     */

    public Object login(String username, String password) {
        // 检查用户名密码是否为空
        if (TextUils.isEmpty(username) || TextUils.isEmpty(password))
            return getResponseJSON(RESPONSE_PARAMETER_ERROR).toString();

        // 检查账号是否存在
        boolean isEmpty = authDao.isEmpty(username);
        if (!isEmpty)
            return getResponseJSON(LOGIN_FAILURE_ACCOUNT_DOES_NOT_EXIST).toString();

        // 检查账号密码
        AuthEntity authEntity = authDao.find(username, password);
        // 用户名或密码错误
        if (authEntity == null)
            return getResponseJSON(LOGIN_FAILURE_NOT_CONFORM).toString();
        // 登陆成功
        return getResponseJSON(RESPONSE_SUCCESS).toString();
    }

}
