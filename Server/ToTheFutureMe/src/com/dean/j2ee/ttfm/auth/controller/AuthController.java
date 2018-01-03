package com.dean.j2ee.ttfm.auth.controller;

import com.dean.j2ee.framework.controller.ConvenientController;
import com.dean.j2ee.ttfm.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 账号控制器
 */
@Controller
@RequestMapping(value = "auth")
public class AuthController extends ConvenientController {

    @Autowired
    private AuthService authService;

    /**
     * 检查用户名是否可用
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object checkUsername(@RequestBody String body) {
        return authService.checkUsername(body);
    }

    /**
     * 重新发送验证码
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/verification", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object sendVerificationCodeAgain(@RequestBody String body) {
        return authService.sendVerificationCodeAgain(body);
    }

    /**
     * 用户注册
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object register(@RequestBody String body) {
        return authService.register(body);
    }

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object login(@PathVariable String username, @PathVariable String password) {
        return authService.login(username, password);
    }

    /**
     * 更新用户信息
     *
     * @param token
     * @param body
     * @return
     */
    @RequestMapping(value = "/upload/{token}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object upload(@PathVariable String token, @RequestBody String body) {
        return authService.upload(token, body);
    }

    /**
     * 修改密码
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/editPassword/{token}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object editPassword(@PathVariable String token, @RequestBody String body) {
        return authService.editPassword(token, body);
    }

}
