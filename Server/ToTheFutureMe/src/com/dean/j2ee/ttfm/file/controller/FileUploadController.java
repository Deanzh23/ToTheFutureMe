package com.dean.j2ee.ttfm.file.controller;

import com.dean.j2ee.framework.controller.ConvenientController;
import com.dean.j2ee.ttfm.file.service.FileService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 文件上传 控制器
 * <p>
 * Created by Dean on 2016/12/6.
 */
@Controller
@RequestMapping(value = "file")
public class FileUploadController extends ConvenientController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传接口
     *
     * @param type    业务分类()
     * @param file    上传的文件
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload/{type}", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@PathVariable int type, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        JSONObject responseJSONObject;

        try {
            responseJSONObject = fileService.appUploadFile(type, file, request);
        } catch (IOException e) {
            e.printStackTrace();

            responseJSONObject = getResponseJSON(RESPONSE_UN_KNOW_ERROR);
        }

        return responseJSONObject.toString();
    }

    /**
     * 管理端 文件上传接口
     *
     * @param type
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/manager/upload/{type}", method = RequestMethod.POST)
    @ResponseBody
    public String managerUpload(@PathVariable int type, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        JSONObject responseJSONObject;

        try {
            responseJSONObject = fileService.managerUploadFile(type, file, request);
        } catch (IOException e) {
            e.printStackTrace();

            responseJSONObject = getResponseJSON(RESPONSE_UN_KNOW_ERROR);
        }

        return responseJSONObject.toString();
    }

}
