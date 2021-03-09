package com.dsltyyz.bundle.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Description:
 * 二维码工具类
 *
 * @author: dsltyyz
 * @since: 2019-11-12
 */
public class QRCodeUtils {
    /**
     * 二维码编码字符集
     */
    private static final String CHARSET = "UTF-8";
    /**
     * 二维码输出格式
     */
    private static final String FORMAT = "JPG";
    /**
     * 二维码尺寸
     */
    private static final int QRCODE_SIZE = 300;
    /**
     *  LOGO尺寸
     */
    private static final int LOGO_SIZE = 100;

    /**
     * 根据内容生成二维码（带LOGO）
     *
     * @param content 内容
     * @return
     */
    public static InputStream encodeWithLogo(String content, InputStream logoInputStream) {
        return encode(content, QRCODE_SIZE, QRCODE_SIZE, logoInputStream);
    }

    /**
     * 根据内容生成二维码
     *
     * @param content 内容
     * @return
     */
    public static InputStream encode(String content) {
        return encode(content, QRCODE_SIZE, QRCODE_SIZE, null);
    }

    /**
     * 根据内容生成二维码
     *
     * @param content         内容
     * @param width           二维码宽度
     * @param height          二维码高度
     * @param logoInputStream logo输入流
     * @return
     */
    public static InputStream encode(String content, int width, int height, InputStream logoInputStream) {
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        // 纠错等级
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hintMap.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 二维码两边空白区域大小
        hintMap.put(EncodeHintType.MARGIN, 1);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hintMap);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            //设置下偏移量,如果不加偏移量，有时会导致出错。
            int pixoff = 2;
            graphics.setColor(Color.BLACK);
            // 使用比特矩阵画并保存图像
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i + pixoff, j + pixoff, 3, 3);
                    }
                }
            }
            //二维码是否插入logo
            if (null != logoInputStream) {
                BufferedImage logoBufferedImage = ImageIO.read(logoInputStream);
                logoBufferedImage = compressImage(logoBufferedImage, LOGO_SIZE, LOGO_SIZE);
                int logoWidth = logoBufferedImage.getWidth(null);
                int logoHeight = logoBufferedImage.getHeight(null);
                //将logo嵌入二维码中
                graphics.drawImage(logoBufferedImage, (width - logoWidth) / 2, (height - logoHeight) / 2, logoWidth, logoHeight, null);
            }
            graphics.dispose();
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            //将字节输出流接收图片
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            //ImageIO.write(image, FORMAT, new File("D:/qrcode.jpg"));
            ImageIO.write(image, FORMAT, imOut);
            //将输出流转为输入流
            return new ByteArrayInputStream(bs.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 压缩图片
     * @param source
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage compressImage(BufferedImage source, Integer width, Integer height){
        int sourceWidth = source.getWidth(null);
        int sourceHeight = source.getHeight(null);
        //超出标准的
        if (sourceWidth > width) {
            sourceWidth = width;
        }
        if (sourceHeight > height) {
            sourceHeight = height;
        }
        Image image = source.getScaledInstance(sourceWidth, sourceHeight, Image.SCALE_SMOOTH);
        BufferedImage target = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = target.getGraphics();
        g.drawImage(image, 0, 0, null);
        // 释放占有的资源
        g.dispose();
        return target;
    }

    /**
     * 解析二维码
     * @param inputStream 二维码输入流
     * @return
     */
    public static String decode(InputStream inputStream){
        try {
            BufferedImage qrcodeImage = ImageIO.read(inputStream);
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(qrcodeImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            HashMap hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
            Result result = new MultiFormatReader().decode(bitmap, hints);
            String resultStr = result.getText();
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args) throws FileNotFoundException {
        //生成不带LOGO二维码
        QRCodeUtils.encode("http://www.baidu.com");

        //生成带LOGO二维码
        File file = new File("d:/dota2logo.jpg");
        QRCodeUtils.encodeWithLogo("http://www.baidu.com", new FileInputStream(file));

        File  qrcodeFile = new File("d:/qrcode.jpg");
        System.out.println(QRCodeUtils.decode( new FileInputStream(qrcodeFile)));
    }*/

}
