package com.dean.tothefutureme.push;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dean.android.framework.convenient.database.util.DatabaseUtil;
import com.dean.android.framework.convenient.json.JSONUtil;
import com.dean.android.framework.convenient.notification.NotificationUtil;
import com.dean.android.framework.convenient.notification.exception.NotificationLackIconException;
import com.dean.android.framework.convenient.toast.ToastUtil;
import com.dean.tothefutureme.R;
import com.dean.tothefutureme.config.AppConfig;
import com.dean.tothefutureme.home.HomeActivity;
import com.dean.tothefutureme.letter.model.LetterModel;
import com.dean.tothefutureme.utils.DateTimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.yunba.android.manager.YunBaManager;

/**
 * App 推送广播监听器
 * <p>
 * Created by dean on 2017/12/16.
 */
public class TTFMPushReceiver extends BroadcastReceiver {

    private int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

            Log.d(AppConfig.TAG_YUN_BA, "count = " + count++);

            String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
            String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);

            Log.d(AppConfig.TAG_YUN_BA, "[onReceive] -> topic=" + topic + " msg=" + msg);

            try {
                JSONObject json = new JSONObject(msg);
                LetterModel letterModel = JSONUtil.json2Object(json, LetterModel.class);

                // 保存到数据库
                new Thread(() -> DatabaseUtil.saveOrUpdate(letterModel)).start();

                // 显示通知栏
                showNotification(context);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(AppConfig.TAG_YUN_BA, "解析msg->LetterModel 异常！");
            }

            // debug
            debug(context, topic, msg);
        }
    }

    /**
     * 显示通知栏
     *
     * @param context
     */
    private void showNotification(Context context) {
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, HomeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationUtil.sendNotification(context, R.mipmap.ic_launcher_round, null, DateTimeUtils.getDateString(new Date()),
                    context.getResources().getString(R.string.app_name), "您收到一条传送信件哟！", pendingIntent);
        } catch (NotificationLackIconException e) {
            e.printStackTrace();
            Log.e(AppConfig.TAG_YUN_BA, "显示通知栏 异常！");
        }
    }

    /**
     * 调试代码
     *
     * @param context
     * @param topic
     * @param msg
     */
    private void debug(Context context, String topic, String msg) {
        StringBuilder showMsg = new StringBuilder();
        showMsg.append("Received message from server: ")
                .append(YunBaManager.MQTT_TOPIC)
                .append(" = ")
                .append(topic)
                .append(" ")
                .append(YunBaManager.MQTT_MSG)
                .append(" = ")
                .append(msg);

        ToastUtil.showToast(context, showMsg.toString(), Toast.LENGTH_LONG);
    }

}
