package com.dean.j2ee.ttfm.auth.controller;

import com.dean.j2ee.framework.controller.ConvenientController;
import com.dean.j2ee.ttfm.auth.service.AuthService;
import com.dean.j2ee.ttfm.config.Config;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        new Thread(() -> send()).start();

        return authService.login(username, password);
    }

    private void send() {
        IGtPush push = new IGtPush(Config.PUSH_URL, Config.PUSH_APP_KEY, Config.PUSH_APP_SECRET);

        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(Config.PUSH_APP_ID);
        template.setAppkey(Config.PUSH_APP_KEY);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent("请输入需要透传的内容");

        AppMessage message = new AppMessage();
        message.setData(template);
        List<String> appIds = new ArrayList<>();
        appIds.add(Config.PUSH_APP_ID);
        message.setAppIdList(appIds);

        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
    }

    /**
     * 更新用户信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object upload(@RequestBody String body) {
        return authService.upload(body);
    }

    /**
     * 修改密码
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/editPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object editPassword(@RequestBody String body) {
        return authService.editPassword(body);
    }

}
