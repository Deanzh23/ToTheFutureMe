package com.dean.j2ee.ttfm.push;

import com.dean.j2ee.ttfm.letter.bean.LetterEntity;
import com.dean.j2ee.ttfm.letter.db.LetterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 推送定时任务
 */
@Component
public class PushTimingTask {

    @Autowired
    private LetterDao letterDao;

//    private List<IGtPush> iGtPushes;

    /**
     * 轮询检查到时信件
     * <p>
     * 每天上午8点执行
     */
    @Scheduled(cron = "0 0 8 * * ?")
    private void checkLetters() {
//        if (iGtPushes == null)
//            iGtPushes = new ArrayList<>();
//        else
//            iGtPushes.clear();
//
        long startDateTime = getTime(0, 0, 0);
        long endDateTime = getTime(23, 59, 59);

        // 查询所有当天应该发送推送的信件实例
        List<LetterEntity> letterEntities = letterDao.findAllLetterBySendTime(startDateTime, endDateTime);

        if (letterEntities == null || letterEntities.size() == 0)
            return;

        // 遍历信件实例集，封装推送实例
        for (LetterEntity letterEntity : letterEntities) {
            
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
