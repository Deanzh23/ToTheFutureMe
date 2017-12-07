package com.dean.j2ee.ttfm.auth.service;

import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.framework.utils.TextUils;
import com.dean.j2ee.ttfm.auth.bean.AuthEntity;
import com.dean.j2ee.ttfm.auth.db.AuthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private AuthDao authDao;

    /**
     * 检查用户名是否可用
     *
     * @param username
     * @return
     */
    public Object checkUsername(String username) {
        boolean isEmpty = authDao.isEmpty(username);

        if (isEmpty)
            return getResponseJSON(RESPONSE_SUCCESS).toString();
        else
            return getResponseJSON(REGISTER_USERNAME_FAILURE_EXIST).toString();
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
