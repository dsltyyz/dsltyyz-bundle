package com.dsltyyz.bundle.office.zip;


import com.dsltyyz.bundle.common.util.StreamUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;

/**
 * freemarker工具类
 * @author dsltyyz
 */
public class FreemarkerUtils {

    /**
     * 根据模板输入流替换数据
     * @param inputStream
     * @param data
     * @return
     */
    public static ByteArrayInputStream getFreemarkerContent(InputStream inputStream, Object data) {
        return getFreemarkerContent(StreamUtils.inputStreamToString(inputStream), data);
    }

    /**
     * 根据模板输入内容替换数据
     * @param content
     * @param data
     * @return
     */
    public static ByteArrayInputStream getFreemarkerContent(String content, Object data) {
        Template template = null;
        try {
            template = new Template("t", new StringReader(content), new Configuration(Configuration.VERSION_2_3_30));
            return getFreemarkerContent(template, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据模板替换数据
     * @param template 模板
     * @param data 数据
     * @return
     */
    public static ByteArrayInputStream getFreemarkerContent(Template template, Object data) {
        try {
            StringWriter stringWriter = new StringWriter();
            template.process(data, stringWriter);
            return new ByteArrayInputStream(stringWriter.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

