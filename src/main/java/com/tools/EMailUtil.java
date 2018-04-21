package com.tools;

import com.sun.mail.util.MailSSLSocketFactory;
import com.zcurd.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Created by Bossli on 2017/6/8.
 */
public class EMailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EMailUtil.class);
    public static final String FROM = "duns@ishugui.com";
    public static final String FROM2 = "apple@ishugui.com";
    private static final String HOST = "smtp.exmail.qq.com";
    private static final String PAD = "Meiyoumima123";


    /**
     * 发送邮件配置处理
     *
     * @param toList
     * @param title
     * @param content
     */
    public static boolean sendMail(List<Object> toList, String title, String content) {
        return sendMail(toList, title, content, null);
    }

    /**
     * 发送邮件配置处理(重载)
     *
     * @param toList
     * @param title
     * @param content
     * @param from    为空(null..),则以this.FROM为发件人
     */
    public static boolean sendMail(List<Object> toList, String title, String content, String from) {

        MailConfig mailConfig = new MailConfig();
        // 参数处理
        if (StringUtil.isEmpty(from)) {
            from = FROM;
        }
        // 发件人
        mailConfig.setFrom(from);
        mailConfig.setUser(from);
        // 发送正文
        mailConfig.setContent(content);
        // 邮件服务器
        mailConfig.setHost(HOST);
        // 发件人密码
        mailConfig.setPwd(PAD);
        // 邮件主题
        mailConfig.setTitle(title);
        return send(toList, mailConfig);
    }


    /**
     * 发送邮件配置处理(重载)
     *
     * @param toList
     * @param title
     * @param content
     * @param from    为空(null..),则以this.FROM为发件人
     */
    public static boolean sendMail(List<Object> toList, String title, String content, String from, String fileName) {

        MailConfig mailConfig = new MailConfig();
        // 参数处理
        if (StringUtil.isEmpty(from)) {
            from = FROM;
        }
        // 发件人
        mailConfig.setFrom(from);
        mailConfig.setUser(from);
        // 发送正文
        mailConfig.setContent(content);
        // 邮件服务器
        mailConfig.setHost(HOST);
        // 发件人密码
        mailConfig.setPwd(PAD);
        // 邮件附件
        mailConfig.setFilename(fileName);
        // 邮件主题
        mailConfig.setTitle(title);
        return send(toList, mailConfig);
    }

    /**
     * 发送实现
     *
     * @param config
     * @return
     */
    public static boolean send(List<Object> toList, MailConfig config) {
        if (null == config) {
            logger.info("[mailconfig为空,无法发送邮件]");
            return false;
        }
        boolean ret = false;
        final MailConfig mailConfig = config;
        long start = System.currentTimeMillis();
        try {
            logger.info("[当前需要发送邮件数:{}]", toList.size());

            Properties prop = new Properties();
            //协议
            prop.setProperty("mail.transport.protocol", "smtp");
            //服务器
            prop.setProperty("mail.smtp.host", mailConfig.getHost());
            //端口
            prop.setProperty("mail.smtp.port", "465");
            //使用smtp身份验证
            prop.setProperty("mail.smtp.auth", "true");
            //使用SSL，企业邮箱必需！
            //开启安全协议
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException e1) {
                logger.info("邮件SSL配置异常:" + e1.getMessage(), e1);
            }
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);
            //
            //获取Session对象
            Session s = Session.getDefaultInstance(prop, new Authenticator() {
                //此访求返回用户和密码的对象
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    PasswordAuthentication pa = new PasswordAuthentication(mailConfig.getFrom(), mailConfig.getPwd());
                    return pa;
                }
            });
            //设置session的调试模式，发布时取消
            s.setDebug(false);
            Random random = new Random();
            // 给所有收件人发送邮件
            for (Object to : toList) {
                try {
                    MimeMessage mimeMessage = new MimeMessage(s);
                    mimeMessage.setFrom(new InternetAddress(mailConfig.getFrom()));
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(String.valueOf(to)));
                    //设置主题
                    mimeMessage.setSubject(mailConfig.getTitle());
                    mimeMessage.setSentDate(new Date());
                    // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
                    Multipart multipart = new MimeMultipart();

                    // 设置邮件的文本内容
                    BodyPart contentPart = new MimeBodyPart();
                    if (!StringUtil.isEmpty(mailConfig.getFilename())) {
                        FileDataSource file = new FileDataSource(mailConfig.getFilename());
                        contentPart.setDataHandler(new DataHandler(file));
                        contentPart.setFileName(MimeUtility.encodeText(mailConfig.getFilename().substring(mailConfig.getFilename().lastIndexOf(File.separator))));
                    }
                    contentPart.setContent(mailConfig.getContent(), "text/html; charset=utf-8");
                    multipart.addBodyPart(contentPart);
                    // 将multipart对象放到message中
                    mimeMessage.setContent(multipart);
                    // 保存邮件
                    mimeMessage.saveChanges();
                    boolean isSuccess;
                    int cnt = 0;
                    // 发送邮件失败,3次重试
                    do {
                        try {
                            //发送
                            Transport.send(mimeMessage);
                            logger.info("[邮件->{}][OK]", to);
                            isSuccess = true;
                        } catch (Exception e) {
                            logger.info("邮件发送给{},第{}次异常:" + e.getMessage(), to, cnt, e);
                            isSuccess = false;
                            cnt++;
                        }
                    } while (!isSuccess && cnt <= 3);
//                    Thread.sleep(random.nextInt(1500));
                } catch (Exception e) {
                    logger.info("邮件发送给{}异常:" + e.getMessage(), to, e);
                    continue;
                }
            }
            ret = true;
        } catch (Throwable e) {
            logger.info("邮件发送异常:" + e.getMessage(), e);
            ret = false;
        } finally {
            logger.info("本次邮件发送完成,用时{}s", (System.currentTimeMillis() - start) / 1000);
        }
        return ret;
    }

    public static void main(String[] args) {
        try {
            ArrayList<Object> list = new ArrayList<>();
            list.add("lichuan@ishugui.com");
            sendMail(list, "测试", "测试邮件");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
