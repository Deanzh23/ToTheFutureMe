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
     * @param username
     * @param password
     * @param nickname
     * @param genderCode
     * @param birthday
     * @return
     */
    @RequestMapping(value = "/register/{username}/{password}/{avatarUrl}/{nickname}/{genderCode}/{birthday}", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object register(@PathVariable String username, @PathVariable String password, @PathVariable String avatarUrl, @PathVariable String nickname,
                           @PathVariable int genderCode, @PathVariable long birthday) {
        return authService.register(username, password, avatarUrl, nickname, genderCode, birthday);
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

}
