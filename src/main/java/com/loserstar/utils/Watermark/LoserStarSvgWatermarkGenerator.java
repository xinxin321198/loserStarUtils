package com.loserstar.utils.Watermark;

import com.loserstar.utils.encodes.LoserStarEncodes;
import com.loserstar.utils.file.LoserStarFileUtil;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.*;

/**
 * 生成水印
 * author: loserStar
 * date: 2024年12月26日 16:37
 * remarks:
 */
public class LoserStarSvgWatermarkGenerator {

    /**
     * 获得一个默认配置的SVGGraphics2D对象
     * @return
     */
    private static SVGGraphics2D getDefaultSVGGraphics2D(){
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgURI = "http://www.w3.org/2000/svg";
        //创建SVG文件
        Document doc = domImpl.createDocument(svgURI, "svg", null);
        SVGGraphics2D svggener = new SVGGraphics2D(doc);
        // Set the canvas size
        svggener.setSVGCanvasSize(new Dimension(800, 600));
        // 字体颜色
        svggener.setPaint(Color.gray);
        // Set the font
        svggener.setFont(new Font("Serif", Font.BOLD, 64));
        //旋转角度
        svggener.rotate(Math.toRadians(45));

        return svggener;
    }
    /**
     * 根据文本生成svg水印的字节码
     *
     * @param text  生成的文本
     * @throws IOException
     */
    public static byte[] genWateMarkToBytes(String text) {
        SVGGraphics2D defaultSVGGraphics2D = getDefaultSVGGraphics2D();
        //绘制的坐标
        defaultSVGGraphics2D.drawString(text, 50, 0);
        return genWateMarkToBytes(defaultSVGGraphics2D);
    }

    /**
     * 根据文本生成svg水印的字节码，支持传入自定义的SVGGraphics2D对象，SVG绘图对象中可自定义画布大小、字体、颜色等相关参数
     * @param svggener 自定义的SVGGraphics2D对象
     * @return
     */
    public static byte[] genWateMarkToBytes(SVGGraphics2D svggener){
        boolean useCSS = true;
        ByteArrayOutputStream outputStream = null;
        Writer out = null;
        byte[] bytes = null;
        try {
            //输出SVG文件
            outputStream = new ByteArrayOutputStream();
            out = new OutputStreamWriter(outputStream, "UTF-8");
            svggener.stream(out, useCSS);
            bytes = outputStream.toByteArray();
            // Close the Writer
            out.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }
    /**
     * 根据文本生成水印图片base64
     * @param text 生成的文本
     * @return
     * @throws IOException
     */
	public static String genWateMarkToBase64(String text) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = LoserStarSvgWatermarkGenerator.genWateMarkToBytes(text);
        return LoserStarEncodes.encodeBase64(bytes);
	}

    /**
     *  根据文本生成可直接供前端image标签使用的图片base64
     * @param text
     * @return
     * @throws IOException
     */
	public static String genWateMarkToWebImageBase64(String text) throws IOException {
		return "data:image/svg+xml;base64,"+genWateMarkToBase64(text);
	}

    /**
     * 根据文本生成水印图片文件
     *
     * @param text
     * @param filePath
     * @throws IOException
     */
    public static void genWateMarkToFilePath(String text, String filePath) throws IOException {
        LoserStarFileUtil.WriteBytesToFilePath(genWateMarkToBytes(text), filePath, false);
    }

    /**
     * 根据文本生成水印svg文件，输出到一个输出流中
     * @param text
     * @param outputStream
     */
    public static void genWateMarkToOutputStream(String text, OutputStream outputStream){
        LoserStarFileUtil.WriteBytesToOutputStream(genWateMarkToBytes(text), outputStream);
    }


    public static void main(String[] args) throws IOException {
        String filePath = "/home/watermark.svg";
        System.out.println("----------------------生成水印图片，路径：" + filePath);
        genWateMarkToFilePath("loserStar", filePath);
		System.out.println("----------------------生成基本的base64");
		System.out.println(genWateMarkToBase64("loserStar"));
		System.out.println("----------------------生成可直接供前端image标签使用的base64");
		System.out.println(genWateMarkToWebImageBase64("loserStar"));
    }
}
