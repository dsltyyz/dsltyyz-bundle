package com.dsltyyz.bundle.office.pdf;

import com.dsltyyz.bundle.common.constant.ImageType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * PDF工具类
 *
 * @author: dsltyyz
 * @date: 2019/06/18
 */
public class PDFUtils {

    /**
     * 将pdf文件流转图片文件流
     *
     * @param pdf
     * @return
     */
    public static List<InputStream> pdfToImages(InputStream pdf) {
        return pdfToImages(pdf, ImageType.JPG);
    }

    /**
     * 将pdf文件流转图片文件流
     *
     * @param pdf
     * @param imageSuffix
     * @return
     */
    public static List<InputStream> pdfToImages(InputStream pdf, String imageSuffix) {
        List<InputStream> list = new ArrayList<>();
        try {
            PDDocument pdDocument = PDDocument.load(pdf);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pages = pdDocument.getNumberOfPages();

            for (int i = 0; i < pages; i++) {
                //dpi越大转换后越清晰，相对转换速度越慢
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 100);
                //定义字节输出流
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                //将字节输出流接收图片
                ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
                ImageIO.write(bufferedImage, imageSuffix, imOut);
                //将输出流转为输入流
                list.add(new ByteArrayInputStream(bs.toByteArray()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
