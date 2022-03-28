package com.dsltyyz.bundle.office.pdf;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.dsltyyz.bundle.common.util.FileUtils;

import java.io.*;

/**
 * PDF文件工具类
 * @author dsltyyz
 */
public class PdfUtils {

    public static InputStream convertPdf(InputStream inputStream){
        //输出流
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        convertPdf(inputStream, bs);
        return new ByteArrayInputStream(bs.toByteArray());
    }

    /**
     * 将输入流转为pdf输出流
     * @return
     */
    public static void convertPdf(InputStream inputStream, OutputStream outputStream){
        //aspose require FileInputStream
        File temp = null;
        try{
            if(!(inputStream instanceof FileInputStream)){
                temp = FileUtils.inputStreamToTempFile(inputStream);
                inputStream = new FileInputStream(temp);
            }
            Document document = new Document(inputStream);
            document.save(outputStream, SaveFormat.PDF);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //有临时文件 直接删除
            if(temp!=null){
                temp.delete();
            }
        }
    }

}
