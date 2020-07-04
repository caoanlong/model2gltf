package com.threedcger.utils;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ComponentSampleModel;
import java.awt.image.Raster;
import java.util.Arrays;

public class ImgUtils {
    /**
     * @param image
     * @param bandOffset 用于判断通道顺序
     * @return
     */
    private static boolean equalBandOffsetWith3Byte(BufferedImage image, int[] bandOffset){
        if(image.getType()==BufferedImage.TYPE_3BYTE_BGR){
            if(image.getData().getSampleModel() instanceof ComponentSampleModel){
                ComponentSampleModel sampleModel = (ComponentSampleModel)image.getData().getSampleModel();
                if(Arrays.equals(sampleModel.getBandOffsets(), bandOffset)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断图像是否为BGR格式
     * @return
     */
    public static boolean isBGR3Byte(BufferedImage image){
        return equalBandOffsetWith3Byte(image,new int[]{0, 1, 2});
    }
    /**
     * 判断图像是否为RGB格式
     * @return
     */
    public static boolean isRGB3Byte(BufferedImage image){
        return equalBandOffsetWith3Byte(image,new int[]{2, 1, 0});
    }
    /**
     * 对图像解码返回RGB格式矩阵数据
     * @param image
     * @return
     */
    public static int[] getMatrixRGB(BufferedImage image) {
        if(null==image)
            throw new NullPointerException();
        int[] matrixRGB;
        if(isRGB3Byte(image)){
            matrixRGB= (int[]) image.getData().getDataElements(0, 0, image.getWidth(), image.getHeight(), null);
        }else{
            // 转RGB格式
            BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR);
            new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(image, rgbImage);
            matrixRGB= (int[]) rgbImage.getData().getDataElements(0, 0, image.getWidth(), image.getHeight(), null);
        }
        return matrixRGB;
    }
    /**
     * 对图像解码返回BGR格式矩阵数据
     * @param image
     * @return
     */
    public static byte[] getMatrixBGR(BufferedImage image){
        if(null==image)
            throw new NullPointerException();
        byte[] matrixBGR;
        if(isBGR3Byte(image)){
            matrixBGR= (byte[]) image.getData().getDataElements(0, 0, image.getWidth(), image.getHeight(), null);
        }else{
            // ARGB格式图像数据
            int intrgb[]=image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            matrixBGR=new byte[image.getWidth() * image.getHeight()*3];
            // ARGB转BGR格式
            for(int i=0,j=0;i<intrgb.length;++i,j+=3){
                matrixBGR[j]=(byte) (intrgb[i]&0xff);
                matrixBGR[j+1]=(byte) ((intrgb[i]>>8)&0xff);
                matrixBGR[j+2]=(byte) ((intrgb[i]>>16)&0xff);
            }
        }
        return matrixBGR;
    }

    public static int transByteToInt(byte num) {
        return num < 0 ? num + 256 : num;
    }
    public static int[] getImgChannel(BufferedImage image, int channel) {
        int[] matrixRGB = getMatrixRGB(image);
        int[] result = new int[matrixRGB.length / 3];
        int j = 0;
        for (int i = channel; i < matrixRGB.length; i+=3) {
            result[j] = matrixRGB[i];
            j++;
        }
        return result;
    }

    public static BufferedImage combineImgByChannel(BufferedImage firstImage, BufferedImage secondImage) {
        BufferedImage img = new BufferedImage(firstImage.getWidth(), firstImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < firstImage.getWidth(); x++) {
            for (int y = 0; y < firstImage.getHeight(); y++) {
                int pixel = firstImage.getRGB(x, y);
                int pixel2 = secondImage.getRGB(x, y);
                int r = (pixel & 0xff0000) >> 16;
                int g = (pixel2 & 0xff00) >> 8;
                int b = pixel & 0xff;
                int rgb = (((r)<<16)|(g<<8)|(b));
                img.setRGB(x, y, rgb);
            }
        }

        return img;
    }
    public static BufferedImage removeChannel(BufferedImage image, int channel) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getRGB(x, y);
                int r = (pixel & 0xff0000) >> 16;
                int g = (pixel & 0xff00) >> 8;
                int b = pixel & 0xff;
                if (channel == 1) {
                    g = 255;
                } else if (channel == 2) {
                    b = 255;
                }
                int rgb = (((r)<<16)|(g<<8)|(b));
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }
}
