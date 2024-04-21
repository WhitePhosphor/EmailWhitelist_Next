package org.pykefox.p4.emailwhitelist;

import org.bukkit.configuration.file.FileConfiguration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

public class IMAPServiceUtils {

    public static String USERNAME = Config.emailAddress;    //	邮箱发送账号
    public static String PASSWORD = Config.passcode;     //邮箱平台的授权码
    public static String HOST = Config.hostAddress;     // SMTP服务器地址
    public static String PORT = Config.port;    //SMTP服务器端口
    public static String TITLE = Config.emailTitle;
    public static String CONTENT = Config.emailContent;
    public static Session session = null;

    /**
     * 创建邮件会话
     */
    public static void createSession() {

        if (session != null) return;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST); //SMTP主机名
        props.put("mail.smtp.port", PORT); //口

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }

    /**
     * 发送纯文本邮件，单人发送
     * 这里把title和content什么的都接过来
     */
    public static boolean postMessage(String toMail) {
        try {
            createSession();
            //构造邮件体
            //这里再来一步获取发件人邮箱
            //和getMail()配合食用
            MimeMessage message = new MimeMessage(session);
            message.setSubject(TITLE);
            message.setText(CONTENT);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail));
            //发送↑
            Transport.send(message);
            return true; //发送成功返回True
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //返回true代表成功
    public static boolean receiveMail() {
        try {
            Store store = session.getStore("imaps");
            store.connect(HOST, USERNAME, PASSWORD);
            Folder emailInbox = store.getFolder("INBOX");
            emailInbox.open(Folder.READ_WRITE);
            Message[] messages = emailInbox.getMessages();
            for (Message message : messages) {
                String senderAddress = message.getFrom()[0].toString();
                String content = message.getContent().toString();
                if (Objects.equals(content, Config.trigger)) {
                    //check一下数据库 加到判断里 数据库逻辑还没写
                    postMessage(senderAddress);
                    return true;
                } else {
                    System.out.println("An unrelated email was detected.");
                    message.setFlag(Flags.Flag.SEEN, true); //加个检测?
                }
            }
            emailInbox.close(false);
            store.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}

