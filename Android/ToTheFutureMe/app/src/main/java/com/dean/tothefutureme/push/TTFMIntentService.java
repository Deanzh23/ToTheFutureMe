package com.dean.tothefutureme.push;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class TTFMIntentService extends GTIntentService {

    public TTFMIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TTFMIntentService.class.getSimpleName(), "[onReceiveServicePid] -> pid = " + pid);
    }

    /**
     * 接收透传消息
     *
     * @param context
     * @param msg
     */
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        Log.d(TTFMIntentService.class.getSimpleName(), "[onReceiveServicePid] -> msg = " + msg.getMessageId());
    }

    @Override
    public void onReceiveClientId(Context context, String clientId) {
        Log.d(TAG, "[onReceiveClientId] -> " + "clientId = " + clientId);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TTFMIntentService.class.getSimpleName(), "[onReceiveServicePid] -> online = " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d(TTFMIntentService.class.getSimpleName(), "[onReceiveServicePid] -> cmdMessage = " + cmdMessage.getClientId());
    }

}
