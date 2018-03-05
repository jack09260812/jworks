package com.github.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by d on 2016/9/20.
 */
public class ImgHelper {

    //jpg格式
    public static final String JPG_FORMAT = "jpg";
    //gif格式
    public static final String GIF_FORMAT = "gif";

    /**
     * 图片压缩
     * jpg/gif canWriteCompressed为true，可以压缩
     *
     * @param image   源图片
     * @param out     目标流
     * @param format
     * @param quality
     */
    public static void compression(BufferedImage image, OutputStream out, String format, float quality) throws IOException {
        ImageOutputStream outputStream = ImageIO.createImageOutputStream(out);
        if (format == null || !format.equals(ImgHelper.JPG_FORMAT) || !format.equals(ImgHelper.GIF_FORMAT)) {
            format = ImgHelper.JPG_FORMAT;
        }
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName(format).next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(quality);
        jpgWriter.setOutput(outputStream);
        jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
        jpgWriter.dispose();
    }

    /**
     * 图片压缩
     *
     * @param image   源图片
     * @param format
     * @param quality
     */
    public static byte[] compression(BufferedImage image, String format, float quality) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream outputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
        if (format == null || !format.equals(ImgHelper.JPG_FORMAT) || !format.equals(ImgHelper.GIF_FORMAT))
            format = ImgHelper.JPG_FORMAT;
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName(format).next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(quality);
        jpgWriter.setOutput(outputStream);
        jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
        jpgWriter.dispose();
        byte[] data = byteArrayOutputStream.toByteArray();
        return data;
    }

    /**
     * 转换图片格式
     *
     * @param image
     * @param out
     * @param format
     * @param width  压缩后宽度
     * @param height 压缩后高度
     * @return
     */
    public static void formatTransfer(BufferedImage image, OutputStream out, String format, int width, int height) throws IOException {
        if (height == 0)
            height = image.getHeight();
        if (width == 0)
            width = image.getWidth();
        Image _image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(_image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        ImageIO.write(tag, format, out);
    }

}
