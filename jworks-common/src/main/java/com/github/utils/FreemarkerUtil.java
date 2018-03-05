package com.github.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by jinwei.li on 2017/1/2.
 * velocity工具类
 */
public class FreemarkerUtil {

    private final String templateDir = "generateTemplate";
    private Configuration cfg;

    private FreemarkerUtil() {
        cfg = new Configuration(Configuration.getVersion());
        try {
            String dir = this.getClass().getClassLoader().getResource(templateDir).getPath();
            cfg.setDirectoryForTemplateLoading(new File(dir));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param vmPath
     * @param context
     * @param dest
     */
    public void generateSource(String vmPath, Object context, String dest) throws IOException, TemplateException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), Charset.forName("UTF-8")));
        Template t = cfg.getTemplate(vmPath);
        t.process(context, writer);
        writer.close();
    }

    /**
     * @param vmPath
     * @param context
     */
    public String generateSource(String vmPath, Map<String, Object> context) throws IOException, TemplateException {
        Template t = cfg.getTemplate(vmPath);
        StringWriter writer = new StringWriter();
        t.process(context, writer);
        writer.close();
        return writer.toString();
    }

    /**
     * 静态内部类实现延迟加载
     */
    private static class SingletonHolder {
        private final static FreemarkerUtil instance = new FreemarkerUtil();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static FreemarkerUtil getInstance() {
        return SingletonHolder.instance;
    }
}
