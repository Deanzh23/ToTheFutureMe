package com.dean.j2ee.ttfm.auth.controller;

import com.dean.j2ee.framework.controller.ConvenientController;
import com.dean.j2ee.ttfm.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param username
     * @return
     */
    @RequestMapping(value = "/check/{username}")
    @ResponseBody
    public Object checkUsername(@PathVariable String username) {
        return authService.checkUsername(username);
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
    @RequestMapping(value = "/register/{username}/{password}/{avatarUrl}/{nickname}/{genderCode}/{birthday}", method = RequestMethod.POST)
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
    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@PathVariable String username, @PathVariable String password) {
        return authService.login(username, password);
    }

}
