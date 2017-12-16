package com.dean.j2ee.ttfm.push;

import com.dean.j2ee.framework.http.ConvenientHttpUtil;
import com.dean.j2ee.framework.json.JSONUtil;
import com.dean.j2ee.framework.utils.EncodingUtils;
import com.dean.j2ee.ttfm.config.Config;
import com.dean.j2ee.ttfm.letter.bean.LetterEntity;
import com.dean.j2ee.ttfm.letter.db.LetterDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * 推送定时任务
 */
@Component
public class PushTimingTask {

    @Autowired
    private LetterDao letterDao;

    /**
     * 轮询检查到时信件
     * <p>
     * 每天上午8点执行
     */
    @Scheduled(cron = "0 0 8 * * ?")
    private void checkLetters() {
        long startDateTime = getTime(0, 0, 0);
        long endDateTime = getTime(23, 59, 59);

        // 查询所有当天应该发送推送的信件实例
        List<LetterEntity> letterEntities = letterDao.findAllLetterBySendTime(startDateTime, endDateTime);

        if (letterEntities == null || letterEntities.size() == 0)
            return;

        // 遍历信件实例集，封装推送实例
        for (LetterEntity letterEntity : letterEntities)
            new Thread(() -> sendLetterPush(letterEntity)).start();
    }

    /**
     * 发送信件推送
     *
     * @param letterEntity
     */
    private void sendLetterPush(LetterEntity letterEntity) {
        JSONObject bodyJSONObject = new JSONObject();
        bodyJSONObject.put("method", "publish");
        bodyJSONObject.put("appkey", Config.PUSH_APP_KEY);
        bodyJSONObject.put("seckey", Config.PUSH_APP_SECRET);
        bodyJSONObject.put("topic", EncodingUtils.md5Encode(letterEntity.getUserId()));
        bodyJSONObject.put("msg", JSONUtil.object2Json(letterEntity));

        try {
            ConvenientHttpUtil.sendHttp(ConvenientHttpUtil.METHOD_POST, Config.PUSH_URL, null, bodyJSONObject);
            System.out.println("[发送信件推送] -> topic=" + letterEntity.getUserId() + " msg=" + letterEntity.getLetterId());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("[发送信件推送 失败!] -> topic=" + letterEntity.getUserId() + " msg=" + letterEntity.getLetterId());
        }
    }

    /**
     * 获取指定日期毫秒值
     *
     * @param hours
     * @param minutes
     * @param seconds
     * @return
     */
    private long getTime(int hours, int minutes, int seconds) {
        Date date = new Date();
        date.setHours(hours);
        date.setMinutes(minutes);
        date.setSeconds(seconds);

        return date.getTime();
    }

}
