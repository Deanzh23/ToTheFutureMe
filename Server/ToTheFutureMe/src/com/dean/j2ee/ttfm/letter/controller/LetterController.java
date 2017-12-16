package com.dean.j2ee.ttfm.letter.controller;

import com.dean.j2ee.framework.controller.ConvenientController;
import com.dean.j2ee.ttfm.letter.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param body
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object upload(@RequestBody String body) {
        return letterService.upload(body);
    }

}
