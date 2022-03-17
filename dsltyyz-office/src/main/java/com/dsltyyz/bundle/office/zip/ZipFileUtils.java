package com.dsltyyz.bundle.office.zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip文件工具类
 * @author dsltyyz
 */
public class ZipFileUtils {

    /**
     * 处理docx模板
     * @param sourceInputStream
     * @param data
     * @return
     */
    public static InputStream dealTemplate(InputStream sourceInputStream, Map<String, InputStream> data){
        //输出流
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        dealTemplate(sourceInputStream, bs, data);
        return new ByteArrayInputStream(bs.toByteArray());
    }

    /**
     * 处理docx模板
     * @param sourceInputStream
     * @param outputStream
     * @param data
     */
    public static void dealTemplate(InputStream sourceInputStream, OutputStream outputStream, Map<String, InputStream> data){
        //ZIP输入流
        ZipInputStream zipInputStream = new ZipInputStream(sourceInputStream);
        //ZIP节点
        ZipEntry zipEntry = null;
        //ZIP输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        try {
            int len = -1;
            byte[] buffer = new byte[1024];
            //遍历ZIP输入流节点
            while ((zipEntry = zipInputStream.getNextEntry())!=null){
                zipOutputStream.putNextEntry(new ZipEntry(zipEntry.getName()));
                if (data.keySet().contains(zipEntry.getName())) {
                    //需要替换的文件
                    InputStream is = data.get(zipEntry.getName());
                    while ((len = is.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    is.close();
                }else{
                    //存储原来的节点
                    while ((len = zipInputStream.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                }
            }
            zipInputStream.close();
            zipOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取压缩文件名称列表
     * @param sourceInputStream
     * @return
     */
    public static List<String> getTemplateFileList(InputStream sourceInputStream){
        List<String> list  = new ArrayList<>();
        //ZIP输入流
        ZipInputStream zipInputStream = new ZipInputStream(sourceInputStream);
        //ZIP节点
        ZipEntry zipEntry = null;
        try {
            while ((zipEntry = zipInputStream.getNextEntry())!=null){
                if(!zipEntry.getName().endsWith("/")){
                    list.add(zipEntry.getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
