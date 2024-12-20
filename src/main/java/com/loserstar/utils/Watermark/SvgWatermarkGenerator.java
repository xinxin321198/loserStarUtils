package com.loserstar.utils.Watermark;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Base64;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * 生成水印
 * author: loserStar
 * date: 2024年5月20日上午11:17:45
 * remarks:
 */
public class SvgWatermarkGenerator {
	/**
	 * 生成水印
	 * 
	 * @param text
	 * @param outputStream
	 * @throws IOException
	 */
	public static void genWateMark(String text,OutputStream outputStream) throws IOException {
		DOMImplementation domImpl=GenericDOMImplementation.getDOMImplementation();
		String svgURI="http://www.w3.org/2000/svg";
		//创建SVG文件
		Document doc=domImpl.createDocument(svgURI, "svg", null);
		SVGGraphics2D svggener=new SVGGraphics2D(doc);
		// Set the canvas size
		svggener.setSVGCanvasSize(new Dimension(800, 600));
		// 字体颜色
		svggener.setPaint(Color.gray);
	    // Set the font
		svggener.setFont(new Font("Serif", Font.BOLD, 64));
		//旋转角度
		svggener.rotate(Math.toRadians(45));
		//绘制的坐标
		svggener.drawString(text, 50, 0);
		boolean useCSS=true;
		//输出SVG文件
		Writer out =new OutputStreamWriter(outputStream,"UTF-8");
		svggener.stream(out,useCSS);
		// Close the Writer
		out.close();
	}
	
	public static void main(String[] args) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		SvgWatermarkGenerator.genWateMark("黄浦晓军0221", outputStream);
		System.out.println("data:image/svg+xml;base64,"+Base64.getEncoder().encodeToString(outputStream.toByteArray()));
	}
}
