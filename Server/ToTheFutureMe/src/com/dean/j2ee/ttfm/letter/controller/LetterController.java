package com.dean.j2ee.ttfm.letter.controller;

import com.dean.j2ee.framework.controller.ConvenientController;
import com.dean.j2ee.ttfm.letter.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 信件控制器
 */
@Controller
@RequestMapping(value = "letter")
public class LetterController extends ConvenientController {

    @Autowired
    private LetterService letterService;

    /**
     * 上传信件
     *
     * @param token
     * @param body
     * @return
     */
    @RequestMapping(value = "/upload/{token}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object upload(@PathVariable String token, @RequestBody String body) {
        return letterService.upload(token, body);
    }

    /**
     * 读取信件
     *
     * @param token
     * @param username   收件人用户名
     * @param startIndex 起始下标
     * @param count      信件数量
     * @return
     */
    @RequestMapping(value = "/load/{token}/{username}/{startIndex}/{count}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object loadLetters(@PathVariable String token, @PathVariable String username, @PathVariable int startIndex, @PathVariable int count) {
        return letterService.loadLetters(token, username, startIndex, count);
    }

    /**
     * 设置指定信件已读
     *
     * @param letterId
     * @return
     */
    @RequestMapping(value = "/readLetter/{token}/{letterId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object readLetter(@PathVariable String token, @PathVariable String letterId) {
        return letterService.readLetter(token, letterId);
    }

}
