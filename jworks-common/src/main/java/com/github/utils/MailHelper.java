package com.github.utils;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电子邮件工具类
 */
public class MailHelper {

    private static final Logger logger = LoggerFactory.getLogger(MailHelper.class);
    private static String from;
    private static String fromName;
    private static String charSet = "utf-8";
    private static String username;
    private static String password;

    static {
        from = PropertiesHelper.getValue("mail.from");
        fromName = PropertiesHelper.getValue("mail.from.name");
        username = PropertiesHelper.getValue("mail.username");
        password = PropertiesHelper.getValue("mail.password");
    }

    private static Map<String, String> hostMap = ImmutableMap.copyOf(new HashMap() {{
        // 126
        put("smtp.126", "smtp.126.com");
        // qq
        put("smtp.qq", "smtp.qq.com");
        put("smtp.wanjiedata", "smtp.exmail.qq.com");
        put("smtp.ssl.port.wanjiedata", "465");
        // 163
        put("smtp.163", "smtp.163.com");

        // sina
        put("smtp.sina", "smtp.sina.com.cn");

        // tom
        put("smtp.tom", "smtp.tom.com");

        // 263
        put("smtp.263", "smtp.263.net");

        // yahoo
        put("smtp.yahoo", "smtp.mail.yahoo.com");

        // hotmail
        put("smtp.hotmail", "smtp.live.com");

        // gmail
        put("smtp.gmail", "smtp.gmail.com");
        put("smtp.port.gmail", "465");
    }});

    public static String getHost(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp." + matcher.group(1);
        }
        if (hostMap.containsKey(key)) {
            return hostMap.get(key);
        } else {
            throw new Exception("unSupportEmail");
        }
    }

    public static int getSmtpPort(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp.port." + matcher.group(1);
        }
        if (hostMap.containsKey(key)) {
            return Integer.parseInt(hostMap.get(key));
        } else {
            return 25;
        }
    }

    public static String getSmtpSSLPort(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp.ssl.port." + matcher.group(1);
        }
        if (hostMap.containsKey(key)) {
            return hostMap.get(key);
        } else {
            return null;
        }
    }

    /**
     * 发送模板邮件
     *
     * @param toMailAddr   收信人地址
     * @param subject      email主题
     * @param templatePath 模板地址
     * @param map          模板map
     */
    public static void sendFtlMail(String[] toMailAddr, String subject,
                                   String templatePath, Map<String, Object> map) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(getHost(from));
            if (getSmtpSSLPort(from) == null)
                hemail.setSmtpPort(getSmtpPort(from));
            else {
                hemail.setSslSmtpPort(getSmtpSSLPort(from));
                hemail.setSSLOnConnect(true);
            }
            hemail.setCharset(charSet);
            hemail.addTo(toMailAddr);
            hemail.setFrom(from, fromName);
            hemail.setAuthentication(username, password);
            hemail.setSubject(subject);
            // 模板内容转换为string
            String htmlText = FreemarkerUtil.getInstance().generateSource(templatePath, map);
            logger.debug("邮件模板：\t" + htmlText);
            hemail.setMsg(htmlText);
            hemail.send();
            logger.debug("email send true!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("email send error!");
        }
    }

    /**
     * 发送模板邮件
     *
     * @param toMailAddr   收信人地址
     * @param subject      email主题
     * @param templatePath 模板地址
     * @param map          模板map
     */
    public static void sendFtlMailWithFile(String[] toMailAddr, String subject,
                                           String templatePath, Map<String, Object> map, File... files) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(getHost(from));
            if (getSmtpSSLPort(from) == null)
                hemail.setSmtpPort(getSmtpPort(from));
            else {
                hemail.setSslSmtpPort(getSmtpSSLPort(from));
                hemail.setSSLOnConnect(true);
            }
            hemail.setCharset(charSet);
            hemail.addTo(toMailAddr);
            hemail.setFrom(from, fromName);
            hemail.setAuthentication(username, password);
            hemail.setSubject(subject);
            // 模板内容转换为string
            String htmlText = FreemarkerUtil.getInstance().generateSource(templatePath, map);
            logger.debug("邮件模板：\t" + htmlText);
            hemail.setMsg(htmlText);
            for (File file : files)
                hemail.attach(file);
            hemail.send();
            logger.debug("email send true!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("email send error!");
        }
    }

    /**
     * 发送普通邮件
     *
     * @param toMailAddr 收信人地址
     * @param subject    email主题
     * @param message    发送email信息
     */
    public static void sendCommonMail(String toMailAddr, String subject,
                                      String message) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(getHost(from));
            if (getSmtpSSLPort(from) == null)
                hemail.setSmtpPort(getSmtpPort(from));
            else {
                hemail.setSslSmtpPort(getSmtpSSLPort(from));
                hemail.setSSLOnConnect(true);
            }
            hemail.setCharset(charSet);
            hemail.addTo(toMailAddr);
            hemail.setFrom(from, fromName);
            hemail.setAuthentication(username, password);
            hemail.setSubject(subject);
            hemail.setMsg(message);
            hemail.send();
            logger.debug("email send true!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("email send error!");
        }

    }

    /**
     * 发送普通邮件
     * 带附件
     *
     * @param toMailAddr 收信人地址
     * @param subject    email主题
     * @param message    发送email信息
     */
    public static void sendMailWithFile(String[] toMailAddr, String subject,
                                        String message, File... files) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(getHost(from));
            if (getSmtpSSLPort(from) == null)
                hemail.setSmtpPort(getSmtpPort(from));
            else {
                hemail.setSslSmtpPort(getSmtpSSLPort(from));
                hemail.setSSLOnConnect(true);
            }
            hemail.setCharset(charSet);
            hemail.addTo(toMailAddr);
            hemail.setFrom(from, fromName);
            hemail.setAuthentication(username, password);
            hemail.setSubject(subject);
            hemail.setMsg(message);
            for (File file : files)
                hemail.attach(file);
            hemail.send();
            logger.debug("email send true!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("email send error!");
        }

    }

}