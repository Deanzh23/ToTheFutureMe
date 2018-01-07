package com.dean.j2ee.ttfm.attention.controller;

import com.dean.j2ee.framework.controller.ConvenientController;
import com.dean.j2ee.ttfm.attention.service.AttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 好友控制器
 */
@Controller
@RequestMapping(value = "attention")
public class AttentionController extends ConvenientController {

    @Autowired
    private AttentionService attentionService;

    /**
     * 获取指定用户的全部好友
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/loadAttention/{token}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object loadAttention(@PathVariable String token, @RequestBody String body) {
        return attentionService.loadAttention(token, body);
    }

    /**
     * 通过指定用户名搜索用户
     *
     * @param token
     * @param body
     * @return
     */
    @RequestMapping(value = "/searchAuthByUsername/{token}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object searchAuthByUsername(@PathVariable String token, @RequestBody String body) {
        return attentionService.searchAuthByUsername(token, body);
    }

    /**
     * 添加好友关系
     *
     * @param token
     * @param body
     * @return
     */
    @RequestMapping(value = "/addAttention/{token}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object addAttention(@PathVariable String token, @RequestBody String body) {
        return attentionService.addAttention(token, body);
    }

}
