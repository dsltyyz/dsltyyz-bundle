package com.dsltyyz.bundle.common.util;

import io.jsonwebtoken.lang.Assert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * 文件工具类
 *
 * @author: dsltyyz
 * @date: 2019-3-18
 */
public class FileUtils {

    /**
     * 检查文件是否存在 并创建
     *
     * @param file
     * @throws IOException
     */
    public static void checkFileExists(File file) throws IOException {
        //当前文件不存在
        if (!file.exists()) {
            //上一级不存在
            if (!file.getParentFile().exists()) {
                //创建上一级文件夹
                file.getParentFile().mkdirs();
            }
            //创建文件
            file.createNewFile();
        }
    }

    /**
     * 检查文件路径是否存在 并创建
     *
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
     * 获取文件夹下文件列表
     *
     * @param dirFile
     * @param includeSubDir
     * @return
     * @throws IOException
     */
    private static List<File> getDirFileList(File dirFile, boolean includeSubDir) throws IOException {
        Assert.notNull(dirFile, "该文件夹不能为空");
        Assert.isTrue(dirFile.isDirectory(), "该文件不是文件夹");
        List<File> list = new ArrayList<>();
        File[] files = dirFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //文件夹 是否处理
                if (includeSubDir) {
                    list.addAll(getDirAllFileList(file));
                }
            } else {
                //文件直接添加
                list.add(file);
            }
        }

        return list;
    }

    /**
     * 获取文件夹下所有文件列表
     *
     * @param dirFile
     * @return
     * @throws IOException
     */
    public static List<File> getDirAllFileList(File dirFile) throws IOException {
        return getDirFileList(dirFile, true);
    }

    /**
     * 获取文件夹下当前文件列表
     *
     * @param dirFile
     * @return
     * @throws IOException
     */
    public static List<File> getDirCurrentFileList(File dirFile) throws IOException {
        return getDirFileList(dirFile, false);
    }

    /**
     * 将文件移到目标文件夹下
     *
     * @param file
     * @param targetDirFile
     */
    public static void moveFile(File file, File targetDirFile) throws IOException {
        Assert.notNull(file, "该文件不能为空");
        Assert.notNull(targetDirFile, "该文件夹不能为空");
        //检查当前文件
        Assert.isTrue(file.exists(), "该文件不存在");
        //检查目标文件夹 不存在就创建
        if (!targetDirFile.exists()) {
            targetDirFile.mkdirs();
        }
        File newFile = new File(targetDirFile, file.getName());
        Files.move(Paths.get(file.getAbsolutePath()), Paths.get(newFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
        //WINDOW可以 LINUX不行
        //file.renameTo(newFile);
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        String str = "";
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(FileUtils.class.getResourceAsStream("/" + filePath), "UTF-8");
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
     *
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
