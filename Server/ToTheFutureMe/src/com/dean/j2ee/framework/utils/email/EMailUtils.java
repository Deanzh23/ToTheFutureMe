package com.dean.j2ee.framework.utils.email;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮箱工具类
 */
public class EMailUtils {

    /**
     * 发送验证码到邮箱
     *
     * @param contentTxt
     * @throws MessagingException
     */
    public static void sendEMail(String AppName, String receiveEMail, String sendEMail, String sendEMailPassword, String contentTxt) throws MessagingException {
        Properties properties = new Properties();
        // 指定协议
        properties.put("mail.transport.protocol", "smtp");
        // 主机 smtp.qq.com
        properties.put("mail.smtp.host", "smtp.163.com");
        // 端口
        properties.put("mail.smtp.port", 25);
        // 用户密码认证
        properties.put("mail.smtp.auth", "true");
        // 调试模式
        properties.put("mail.debug", "true");

        // 创建邮件会话
        Session session = Session.getInstance(properties);
        // 创建邮件对象
        MimeMessage msg = new MimeMessage(session);
        // 设置发件人
        msg.setFrom(new InternetAddress(sendEMail));
        // 设置邮件收件人
        msg.setRecipients(Message.RecipientType.TO, receiveEMail);
        // 标题
        msg.setSubject("[" + AppName + "]");
        // 发送时间
        msg.setSentDate(new Date());
        // 发送内容
        msg.setText(contentTxt);

        // 5. 发送
        Transport trans = session.getTransport();
        trans.connect(sendEMail, sendEMailPassword);
        trans.sendMessage(msg, msg.getAllRecipients());
        trans.close();
    }

    /**
     * 生成6位随机数验证码
     *
     * @return
     */
    public static String getVerificationCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 相差n秒
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDiffer(long startTime, long endTime) {
        return (int) (endTime / 1000 - startTime / 1000);
    }

}
