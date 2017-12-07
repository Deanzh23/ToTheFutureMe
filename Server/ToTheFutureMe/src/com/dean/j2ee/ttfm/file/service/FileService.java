package com.dean.j2ee.ttfm.file.service;

import com.dean.j2ee.framework.service.ConvenientService;
import com.dean.j2ee.ttfm.file.bean.UploadFileEntity;
import com.dean.j2ee.ttfm.token.db.TokenDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 文件上传
 * <p>
 * Created by dean on 2017/1/27.
 */
@Service
public class FileService extends ConvenientService {

    private static final String PATH_STATIC = "/resources";
    private static final String PATH_UPLOAD = "WEB-INF/data/upload";

    @Autowired
    private TokenDao tokenDao;

    /**
     * 客户端 上传文件
     *
     * @param type
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    public JSONObject appUploadFile(int type, MultipartFile file, HttpServletRequest request) throws IOException {
        return uploadFile(type, file, true, request);
    }

    /**
     * 管理端 上传文件
     *
     * @param type
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    public JSONObject managerUploadFile(int type, MultipartFile file, HttpServletRequest request) throws IOException {
        return uploadFile(type, file, false, request);
    }

    /**
     * 文件上传
     *
     * @param type
     * @param file
     * @param isFullPath
     * @param request
     * @return
     * @throws IOException
     */
    private JSONObject uploadFile(int type, MultipartFile file, boolean isFullPath, HttpServletRequest request) throws IOException {
        UploadFileEntity uploadFileEntity = new UploadFileEntity(request.getSession().getServletContext().getRealPath(PATH_UPLOAD), type);
        File targetFile = uploadFileEntity.getFile();

        System.out.println("path=" + targetFile.getPath() + ",fileName=" + targetFile.getName());

        file.transferTo(targetFile);

        JSONObject responseJSONObject = getResponseJSON(RESPONSE_SUCCESS);
        JSONObject urlJSONObject = new JSONObject();
        urlJSONObject.put("url", (isFullPath ? (PATH_STATIC + "/upload/" + uploadFileEntity.getTypePath() + "/") : "") + uploadFileEntity.getFileName());
        responseJSONObject.put("data", urlJSONObject);

        return responseJSONObject;
    }

}
