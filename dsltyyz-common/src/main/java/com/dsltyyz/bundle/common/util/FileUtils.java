package com.dsltyyz.bundle.common.util;

import java.io.*;

/**
 * Description:
 * 文件工具类
 *
 * @author: dsltyyz
 * @since: 2019-3-18
 */
public class FileUtils {

    /**
     * 检查文件是否存在 并创建
     * @param file
     * @throws IOException
     */
    public static void checkFileExists(File file) throws IOException {
        //当前文件不存在
        if(!file.exists()){
            //上一级不存在
            if(!file.getParentFile().exists()){
                //创建上一级文件夹
                file.getParentFile().mkdirs();
            }
            //创建文件
            file.createNewFile();
        }
    }

    /**
     * 检查文件路径是否存在 并创建
     * @param filePath
     * @return
     * @throws IOException
     */
    public static File checkFileExists(String filePath) throws IOException {
        File file = new File(filePath);
        checkFileExists(file);
        return file;
    }

    /**
     * 读取文件
     * @param filePath
     * @return
     */
    public static String readFile(String filePath){
        String str = "";
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(FileUtils.class.getResourceAsStream("/"+filePath), "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                str += temp;
            }
            reader.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入流转临时文件
     * @param inputStream
     * @return
     */
    public static File inputStreamToTempFile(InputStream inputStream) {
        try {
            File tempFile = File.createTempFile("temp", "tmp");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
