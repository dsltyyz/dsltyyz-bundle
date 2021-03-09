package com.dsltyyz.bundle.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Description:
 * 流工具类
 *
 * @author: dsltyyz
 * @since: 2019-05-29
 */
public class StreamUtils {

    /**
     * 输出流转输入流
     * @param outputStream
     * @return
     */
    public static InputStream outputStreamToInputStream(OutputStream outputStream) {
        if(null==outputStream){
            throw new IllegalArgumentException("null==outputStream");
        }
        ByteArrayOutputStream out = (ByteArrayOutputStream) outputStream;
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * 输入流转输出流
     * @param inputStream
     * @return
     */
    public static OutputStream inputStreamToOutputStream(InputStream inputStream) {
        if(null==inputStream){
            throw new IllegalArgumentException("null==inputStream");
        }
        ByteArrayInputStream in = (ByteArrayInputStream) inputStream;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int temp = 0;
        while((temp = in.read()) != -1){
            out.write(temp);
        }
        return out;
    }
}
