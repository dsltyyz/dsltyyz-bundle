package com.dsltyyz.bundle.common.util;

import java.io.*;

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
     *
     * @param outputStream
     * @return
     */
    public static InputStream outputStreamToInputStream(OutputStream outputStream) {
        if (null == outputStream) {
            throw new IllegalArgumentException("null==outputStream");
        }
        ByteArrayOutputStream out = (ByteArrayOutputStream) outputStream;
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * 输入流转输出流
     *
     * @param inputStream
     * @return
     */
    public static OutputStream inputStreamToOutputStream(InputStream inputStream) {
        if (null == inputStream) {
            throw new IllegalArgumentException("null==inputStream");
        }
        ByteArrayInputStream in = (ByteArrayInputStream) inputStream;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int temp = 0;
        while ((temp = in.read()) != -1) {
            out.write(temp);
        }
        return out;
    }

    /**
     * 从输入流获取字符串
     *
     * @param inputStream
     * @return
     */
    public static String inputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        String oneLine = "";
        try {
            while ((oneLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(oneLine);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileInputStream inputStreamToFileInputStream(InputStream inputStream){
        try {
            File tempFile = File.createTempFile("temp", "tmp");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer,0, len);
            }
            fileOutputStream.close();
            return new FileInputStream(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BASE64转输入流
     * @param base64
     * @return
     */
    public static InputStream base64ToInputStream(String base64){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        base64ToOutputStream(base64, out);
        return  new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * BASE64转输出流
     * @param base64
     * @param outputStream
     */
    public static void base64ToOutputStream(String base64, OutputStream outputStream){
        byte[] decode = Base64Utils.decode(base64);
        try {
            outputStream.write(decode);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
