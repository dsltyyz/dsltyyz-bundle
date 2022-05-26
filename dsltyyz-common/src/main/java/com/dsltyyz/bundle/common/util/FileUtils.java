package com.dsltyyz.bundle.common.util;

import io.jsonwebtoken.lang.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
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
     * 将文件移到目标文件夹下 若目标文件夹下存在 强行覆盖
     *
     * @param sourceFile
     * @param targetDirFile
     */
    public static void moveFileDir(File sourceFile, File targetDirFile) throws IOException {
        Assert.notNull(sourceFile, "该文件不能为空");
        Assert.notNull(targetDirFile, "该文件夹不能为空");
        //检查当前文件
        Assert.isTrue(sourceFile.exists(), "该文件不存在");
        //检查目标文件夹 不存在就创建
        if (!targetDirFile.exists()) {
            targetDirFile.mkdirs();
        }
        File newFile = new File(targetDirFile, sourceFile.getName());
        Files.move(Paths.get(sourceFile.getAbsolutePath()), Paths.get(newFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 将文件移到目标文件 若目标文件下存在 强行覆盖
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void moveFile(File sourceFile, File targetFile) throws IOException {
        Assert.notNull(sourceFile, "该源文件不能为空");
        Assert.notNull(targetFile, "该目标文件不能为空");
        //检查当前文件
        Assert.isTrue(sourceFile.exists(), "该文件不存在");
        //检查目标文件上一级文件夹 不存在就创建
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        Files.move(Paths.get(sourceFile.getAbsolutePath()), Paths.get(targetFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
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
        return inputStreamToTempFile(inputStream, "temp", "tmp");
    }

    /**
     * 输入流转临时文件
     *
     * @param inputStream
     * @param prefix
     * @param suffix
     * @return
     */
    public static File inputStreamToTempFile(InputStream inputStream, String prefix, String suffix) {
        try {
            File tempFile = File.createTempFile(prefix, suffix);
            inputStreamToFile(inputStream, tempFile);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 输入流转文件
     *
     * @param inputStream
     * @param file
     */
    public static void inputStreamToFile(InputStream inputStream, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InputStream fileToInputStream(String fileUrl){
        Assert.isTrue(!StringUtils.isEmpty(fileUrl), "文件路径不能为空");
        if(fileUrl.startsWith("http")||fileUrl.startsWith("https")){
            return HttpUtils.doGetInputStream(fileUrl, null, null);
        }else if(fileUrl.startsWith("classpath:")){
            return FileUtils.class.getClassLoader().getResourceAsStream(fileUrl.substring(fileUrl.indexOf(":")+1));
        }else{
            try {
                return new FileInputStream(fileUrl);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 获取文件的MD5 HASH值
     * @param file
     * @return
     */
    public static String md5HashCode(File file) {
        try {
            return md5HashCode(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件流的MD5 HASH值
     * @param inputStream
     * @return
     */
    public static String md5HashCode(InputStream inputStream){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                md.update(buffer, 0, length);
            }
            inputStream.close();
            byte[] md5Bytes  = md.digest();
            //1代表绝对值
            BigInteger bigInt = new BigInteger(1, md5Bytes);
            //转换为16进制
            return bigInt.toString(16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
