package com.example.decrypt.demos.web;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.util.Properties;

public class QqMailPuller {
    // QQ邮箱IMAP配置
    private static final String IMAP_HOST = "imap.qq.com";
    private static final int IMAP_PORT = 993; // SSL端口
    private static final String USERNAME = "3067586738@qq.com";
    private static final String AUTH_CODE = "tiaijgvsozpzdgab"; // 步骤2生成的授权码

    // 拉取未读邮件并解析
    public static void pullUnreadMails() throws MessagingException {
        // 1. 配置IMAP参数
        Properties props = new Properties();
        props.put("mail.imap.host", IMAP_HOST);
        props.put("mail.imap.port", IMAP_PORT);
        props.put("mail.imap.ssl.enable", "true"); // 启用SSL
        props.put("mail.imap.auth", "true"); // 启用认证

        // 2. 创建认证器
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, AUTH_CODE);
            }
        };

        // 3. 获取IMAP Session
        Session session = Session.getInstance(props, auth);
        session.setDebug(true); // 调试模式，可查看交互日志

        // 4. 连接IMAP服务器，打开收件箱
        Store store = session.getStore("imap");
        store.connect();
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE); // 读写模式（可标记已读）

        // 5. 筛选未读邮件
        Message[] unreadMessages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        System.out.println("找到未读邮件数：" + unreadMessages.length);

        // 6. 解析每封邮件
        for (Message msg : unreadMessages) {
            String from = msg.getFrom()[0].toString();
            String subject = msg.getSubject();
            String content = getMailContent(msg); // 解析正文

            // 核心：将邮件信息传入自研系统业务逻辑
            System.out.println("===== 新邮件 =====");
            System.out.println("发件人：" + from);
            System.out.println("主题：" + subject);
            System.out.println("正文：" + content);

            // 标记为已读（避免重复拉取）
            msg.setFlag(Flags.Flag.SEEN, true);
        }

        // 7. 关闭连接
        inbox.close(false);
        store.close();
    }

    // 解析邮件正文（兼容纯文本/HTML）
    private static String getMailContent(Message msg) throws MessagingException {
        try {
            Object content = msg.getContent();
            if (content instanceof String) {
                return (String) content;
            } else if (content instanceof MimeMultipart) {
                MimeMultipart multipart = (MimeMultipart) content;
                // 优先取纯文本内容（第1部分通常是text/plain）
                return multipart.getBodyPart(0).getContent().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "解析失败";
    }

    // 测试：定时拉取可结合Timer/Quartz实现
    public static void main(String[] args) {
        try {
            pullUnreadMails(); // 单次拉取
            // 若需定时拉取，可添加：new Timer().scheduleAtFixedRate(new TimerTask() {
            //     @Override
            //     public void run() {
            //         try { pullUnreadMails(); } catch (MessagingException e) { e.printStackTrace(); }
            //     }
            // }, 0, 60 * 1000); // 每1分钟拉取一次
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
