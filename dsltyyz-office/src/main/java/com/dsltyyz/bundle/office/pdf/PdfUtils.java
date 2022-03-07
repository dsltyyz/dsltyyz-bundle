package com.dsltyyz.bundle.office.pdf;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.dsltyyz.bundle.common.util.StreamUtils;

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
        if(!(inputStream instanceof FileInputStream)){
            inputStream = StreamUtils.inputStreamToFileInputStream(inputStream);
        }
        try{
            Document document = new Document(inputStream);
            document.save(outputStream, SaveFormat.PDF);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws FileNotFoundException {
//        convertPdf(new FileInputStream(new File("D://template.docx")), new FileOutputStream(new File("D://template.pdf")) );
//    }
}
