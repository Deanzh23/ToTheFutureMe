package com.dean.j2ee.ttfm.file.bean;

import com.dean.j2ee.framework.utils.TextUils;

import java.io.File;
import java.util.UUID;

/**
 * Created by Dean on 2016/12/11.
 */
public class UploadFileEntity {

    // 图片类型
    public static final int TYPE_PICTURE = 0;
    // HTML文件
    public static final int TYPE_HTML = 2;

    private File file;
    private int type;
    private String fileName;

    public UploadFileEntity(String path, int type) {
        this.type = type;

        path += "/" + getTypePath();
        if (!TextUils.isEmpty(path)) {
            fileName = getFileName(type);
            file = new File(path, fileName);

            if (!file.exists())
                file.mkdirs();
        }
    }

    private String getFileName(int type) {
        String fileName = UUID.randomUUID().toString();

        switch (type) {
            case TYPE_HTML:
                fileName += ".html";
                break;

            default:
                fileName += ".png";
        }

        return fileName;
    }

    public String getTypePath() {
        String typePath;

        switch (type) {
            case TYPE_HTML:
                typePath = "TYPE_HTML";
                break;

            default:
                typePath = "TYPE_PICTURE";
        }

        return typePath;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

}
