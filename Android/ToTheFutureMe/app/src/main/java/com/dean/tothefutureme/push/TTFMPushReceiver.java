package com.dean.tothefutureme.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dean.android.framework.convenient.toast.ToastUtil;

import io.yunba.android.manager.YunBaManager;

/**
 * App 推送广播监听器
 * <p>
 * Created by dean on 2017/12/16.
 */
public class TTFMPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("YunBa SDK", "[onReceive]");

        if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

            String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
            String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);

            //在这里处理从服务器发布下来的消息， 比如显示通知栏， 打开 Activity 等等
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

}
